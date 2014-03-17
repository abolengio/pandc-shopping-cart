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
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

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
    public void handleException(Exception ex) {
        ex.printStackTrace();   //todo
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
    public View addItem(@RequestBody CartItemParameter cartItem) {
        writableShoppingCart.add(cartItem.getProductId(), cartItem.getQuantity());
        return new RedirectView(UriFor.cart, true, false);
    }


    @RequestMapping(value = UriFor.cartItem, method = RequestMethod.GET)
    @ResponseBody
    public ShoppingCartItemResource getCartItem(@PathVariable String productId) {
        ExpandedCartItem shoppingCartItem = readableShoppingCartProvider.getShoppingCartItem(productId);
        return new ShoppingCartItemResource(shoppingCartItem);
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
    public ShoppingCartResource updateQuantity(@PathVariable String productId, @RequestBody CartItemParameter cartItem) {
        //todo validate that parameters match
        writableShoppingCart.updateQuantity(cartItem.getProductId(), cartItem.getQuantity());

        //todo what is the response code ?
        ReadableShoppingCart readableShoppingCart = readableShoppingCartProvider.getReadableShoppingCart();
        return new ShoppingCartResource(readableShoppingCart); //todo refactor to avoid duplication
    }

}
