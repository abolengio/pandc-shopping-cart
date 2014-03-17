package com.ab.cart.rest.resource;

import com.ab.cart.rest.controller.UriFor;
import org.springframework.web.bind.annotation.RequestMethod;

public class DeletedItemResource extends BaseResource {

    private static Link cartLink = new Link(UriFor.cart, "container", RequestMethod.GET);

    public DeletedItemResource() {
        super(cartLink);
    }
}
