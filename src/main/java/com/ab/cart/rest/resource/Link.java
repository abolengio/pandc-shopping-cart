package com.ab.cart.rest.resource;

import org.springframework.web.bind.annotation.RequestMethod;

public class Link {

    private final String href;
    private final String rel;
    private final RequestMethod method;

    public Link(String href, String rel, RequestMethod method) {

        this.href = href;
        this.rel = rel;
        this.method = method;
    }

    public String getHref() {
        return href;
    }

    public String getRel() {
        return rel;
    }

    public RequestMethod getMethod() {
        return method;
    }
}
