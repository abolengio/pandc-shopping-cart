package com.ab.cart.rest.controller;

import com.ab.cart.domain.ExpandedCartItem;
import com.ab.cart.domain.ProductNotInShoppingCartException;
import com.ab.cart.domain.ReadableShoppingCart;
import com.ab.cart.domain.ReadableShoppingCartProvider;
import com.ab.cart.domain.WritableShoppingCart;
import com.ab.cart.rest.resource.RestError;
import com.ab.cart.rest.resource.ShoppingCartItemResource;
import com.ab.cart.rest.resource.ShoppingCartResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class ShoppingCartController {

    private ReadableShoppingCartProvider readableShoppingCartProvider;
    private WritableShoppingCart writableShoppingCart;

    @Autowired
    public ShoppingCartController( ReadableShoppingCartProvider readableShoppingCartProvider, WritableShoppingCart writableShoppingCart) {
        this.readableShoppingCartProvider = readableShoppingCartProvider;
        this.writableShoppingCart = writableShoppingCart;
    }

    @RequestMapping(value = UriFor.cart, method = RequestMethod.GET)
    @ResponseBody
    public ShoppingCartResource shoppingCart() {
        ReadableShoppingCart readableShoppingCart = readableShoppingCartProvider.getReadableShoppingCart();
        return new ShoppingCartResource(readableShoppingCart);

    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RestError handleException(Exception exc) {
        return new RestError(500, exc.getMessage());
    }

    @ExceptionHandler(ProductNotInShoppingCartException.class)
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ResponseBody
    public RestError handleNoProductInShoppingCartException(Exception exc) {
        return new RestError(404, exc.getMessage());
    }

    /*
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestError resolveBindingException ( MethodArgumentNotValidException methodArgumentNotValidException, Locale locale )
    {
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
        return getRestError(bindingResult, locale);
    }

    @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such Order")  // 404
    public class OrderNotFoundException extends RuntimeException {
        // ...
    }

     */

    @RequestMapping(value = UriFor.cartItems, method = RequestMethod.POST)
    @ResponseBody
    public ShoppingCartItemResource addItem(@RequestBody CartItemParameter cartItem) {
        //todo handle product does not exist and negative quantity
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
    public ShoppingCartResource removeItem(@PathVariable String productId) {
        writableShoppingCart.remove(productId);

        //todo what is the response code ?
        ReadableShoppingCart readableShoppingCart = readableShoppingCartProvider.getReadableShoppingCart();
        return new ShoppingCartResource(readableShoppingCart); //todo refactor to avoid duplication
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
