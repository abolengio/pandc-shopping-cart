package com.ab.cart.rest.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseResource {

    protected List<Link> links = new ArrayList<>();

    protected BaseResource(Link... links) {
        this.links.addAll(Arrays.asList(links));
    }

    public List<Link> getLinks(){
        return links;
    }


}
