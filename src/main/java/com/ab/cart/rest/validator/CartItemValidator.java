package com.ab.cart.rest.validator;

import com.ab.cart.rest.controller.CartItemParameter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CartItemValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CartItemParameter.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CartItemParameter cartItemParameter = (CartItemParameter)o;
        if (cartItemParameter.getQuantity() < 0) {
            errors.rejectValue("quantity", "Quantity should not be negative");
        }
    }
}
