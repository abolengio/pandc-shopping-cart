package com.ab.cart.rest.validator;

import com.ab.cart.rest.controller.CartItemParameter;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CartItemValidatorTest {

    @Test
    public void shouldAddErrorIfQuantityIsNegative() {
        CartItemValidator validator = new CartItemValidator();
        CartItemParameter cartItemParameter = new CartItemParameter();
        cartItemParameter.setQuantity(-1);
        cartItemParameter.setProductId("something");
        Errors errors = new BeanPropertyBindingResult(cartItemParameter, "cartItemParameter");
        validator.validate(cartItemParameter, errors);
        assertThat(errors.hasErrors(), is(true));
        assertThat(errors.getFieldError("quantity").getRejectedValue(), is((Object)new Integer(-1)));
        assertThat(errors.getFieldError("quantity").getCode(), is("Quantity should not be negative"));
    }

    @Test
    public void shouldNotAddErrorIfQuantityIsZero() {
        CartItemValidator validator = new CartItemValidator();
        CartItemParameter cartItemParameter = new CartItemParameter();
        cartItemParameter.setQuantity(0);
        cartItemParameter.setProductId("something");
        Errors errors = new BeanPropertyBindingResult(cartItemParameter, "cartItemParameter");
        validator.validate(cartItemParameter, errors);
        assertThat(errors.hasErrors(), is(false));
    }
}
