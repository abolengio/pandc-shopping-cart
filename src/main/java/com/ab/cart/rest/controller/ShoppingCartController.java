package com.ab.cart.rest.controller;

import com.ab.cart.domain.ExpandedCartItem;
import com.ab.cart.domain.ProductNotInShoppingCartException;
import com.ab.cart.domain.ReadableShoppingCart;
import com.ab.cart.domain.ReadableShoppingCartProvider;
import com.ab.cart.domain.WritableShoppingCart;
import com.ab.cart.rest.resource.DeletedItemResource;
import com.ab.cart.rest.resource.RestError;
import com.ab.cart.rest.resource.ShoppingCartItemResource;
import com.ab.cart.rest.resource.ShoppingCartResource;
import com.ab.cart.rest.resource.ValidationError;
import com.ab.cart.rest.validator.CartItemValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

@Controller
public class ShoppingCartController {

    private ReadableShoppingCartProvider readableShoppingCartProvider;
    private WritableShoppingCart writableShoppingCart;
    private CartItemValidator cartItemValidator;

    @Autowired
    public ShoppingCartController( ReadableShoppingCartProvider readableShoppingCartProvider, WritableShoppingCart writableShoppingCart, CartItemValidator cartItemValidator) {
        this.readableShoppingCartProvider = readableShoppingCartProvider;
        this.writableShoppingCart = writableShoppingCart;
        this.cartItemValidator = cartItemValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(cartItemValidator);
    }

    @RequestMapping(value = UriFor.cart, method = RequestMethod.GET)
    @ResponseBody
    public ShoppingCartResource shoppingCart() {
        ReadableShoppingCart readableShoppingCart = readableShoppingCartProvider.getReadableShoppingCart();
        return new ShoppingCartResource(readableShoppingCart);

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestError handleMessageNotReadableException(Exception exc) {
        return new RestError(400, exc.getMessage());
    }

    @ExceptionHandler(ProductNotInShoppingCartException.class)
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ResponseBody
    public RestError handleNoProductInShoppingCartException(Exception exc) {
        return new RestError(404, exc.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationError resolveBindingException (MethodArgumentNotValidException methodArgumentNotValidException)
    {
        return new ValidationError(400, "Validation failed", methodArgumentNotValidException.getBindingResult());
    }

    @RequestMapping(value = UriFor.cartItems, method = RequestMethod.POST)
    @ResponseBody
    public ShoppingCartItemResource addItem(@RequestBody @Valid CartItemParameter cartItem) {
        writableShoppingCart.add(cartItem.getProductId(), cartItem.getQuantity());
        return getCartItemResourceFor(cartItem.getProductId());
    }

    @RequestMapping(value = UriFor.cartItem, method = RequestMethod.GET)
    @ResponseBody
    public ShoppingCartItemResource getCartItem(@PathVariable String productId) {
        return getCartItemResourceFor(productId);
    }

    @RequestMapping(value = UriFor.cartItem, method = RequestMethod.DELETE)
    @ResponseBody
    public DeletedItemResource removeItem(@PathVariable String productId) {
        //todo handle product does not exist - HOW ???
        writableShoppingCart.remove(productId);
        return new DeletedItemResource();
    }

    @RequestMapping(value = UriFor.cartItem, method = RequestMethod.PUT)
    @ResponseBody
    public ShoppingCartItemResource updateQuantity(@PathVariable String productId, @RequestBody CartItemParameter cartItem) {
        //todo validate that parameters match
        //todo handle product does not exist and negative quantity
        writableShoppingCart.updateQuantity(cartItem.getProductId(), cartItem.getQuantity());
        return getCartItemResourceFor(productId);
    }

    private ShoppingCartItemResource getCartItemResourceFor(String productId) {
        ExpandedCartItem shoppingCartItem = readableShoppingCartProvider.getShoppingCartItem(productId);
        return new ShoppingCartItemResource(shoppingCartItem);
    }

}
