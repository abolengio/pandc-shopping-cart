package com.ab.cart.rest.controller;

import com.ab.cart.domain.CartItem;
import com.ab.cart.repository.ShoppingCartRepository;
import com.ab.cart.rest.resource.ShoppingCartResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ShoppingCartController {

    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    public ShoppingCartController( ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @RequestMapping(value = UriFor.cart, method = RequestMethod.GET)
    @ResponseBody
    public ShoppingCartResource shoppingCart() {
        List<CartItem> cartItems = shoppingCartRepository.listItems();
        return new ShoppingCartResource(cartItems); //todo think if the resource creation should be done here from list of items
                                                    //or I should be getting domain object cart from somewhere and convert to resource here

    }
}
