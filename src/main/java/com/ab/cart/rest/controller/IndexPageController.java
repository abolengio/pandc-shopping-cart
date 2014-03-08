package com.ab.cart.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexPageController {

    @RequestMapping("/")
    public String loadHomePage() {
        return "home";
    }

}
