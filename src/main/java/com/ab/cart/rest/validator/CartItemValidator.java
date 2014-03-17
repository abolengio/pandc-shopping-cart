package com.ab.cart.rest.validator;

import com.ab.cart.domain.productcatalogue.ProductCatalogue;
import com.ab.cart.rest.controller.CartItemParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static java.lang.String.format;

public class CartItemValidator implements Validator {

    private ProductCatalogue productCatalogue;

    @Autowired
    public CartItemValidator(ProductCatalogue productCatalogue) {
        this.productCatalogue = productCatalogue;
    }

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
        validateProductId(cartItemParameter.getProductId(), errors);
    }

    public void validateProductId(String productId, Errors errors) {
        if (productCatalogue.getProduct(productId) == null){
            errors.rejectValue("productId", format("Product with id '%s' does not exist in the product catalogue",productId));
        }
    }
}
