package com.ab.cart.rest.validator;

import com.ab.cart.domain.Product;
import com.ab.cart.domain.productcatalogue.ProductCatalogue;
import com.ab.cart.rest.controller.CartItemParameter;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartItemValidatorTest {

    @Mock
    ProductCatalogue productCatalogue;

    @Test
    public void shouldAddErrorIfQuantityIsNegative() {
        when(productCatalogue.getProduct("something")).thenReturn(new Product("something", "name", somePrice()));
        CartItemValidator validator = new CartItemValidator(productCatalogue);
        CartItemParameter cartItemParameter = new CartItemParameter();
        cartItemParameter.setQuantity(-1);
        cartItemParameter.setProductId("something");
        Errors errors = new BeanPropertyBindingResult(cartItemParameter, "cartItemParameter");
        validator.validate(cartItemParameter, errors);
        assertThat(errors.hasErrors(), is(true));
        assertThat(errors.getErrorCount(), is(1));
        assertThat(errors.getFieldError("quantity").getRejectedValue(), is((Object)new Integer(-1)));
        assertThat(errors.getFieldError("quantity").getCode(), is("Quantity should not be negative"));
    }

    @Test
    public void shouldAddErrorIfProductDoesNotExist() {
        when(productCatalogue.getProduct("something")).thenReturn(new Product("something", "name", somePrice()));
        CartItemValidator validator = new CartItemValidator(productCatalogue);
        CartItemParameter cartItemParameter = new CartItemParameter();
        cartItemParameter.setQuantity(1);
        cartItemParameter.setProductId("something-else");
        Errors errors = new BeanPropertyBindingResult(cartItemParameter, "cartItemParameter");
        validator.validate(cartItemParameter, errors);
        assertThat(errors.hasErrors(), is(true));
        assertThat(errors.getErrorCount(), is(1));
        assertThat(errors.getFieldError("productId").getRejectedValue(), is((Object)"something-else"));
        assertThat(errors.getFieldError("productId").getCode(), is("Product with id 'something-else' does not exist in the product catalogue"));
    }

    @Test
    public void shouldAddTwoErrorsIfProductDoesNotExistAndQuantityIsNegative() {
        when(productCatalogue.getProduct("something")).thenReturn(new Product("something", "name", somePrice()));
        CartItemValidator validator = new CartItemValidator(productCatalogue);
        CartItemParameter cartItemParameter = new CartItemParameter();
        cartItemParameter.setQuantity(-1);
        cartItemParameter.setProductId("something-else");
        Errors errors = new BeanPropertyBindingResult(cartItemParameter, "cartItemParameter");
        validator.validate(cartItemParameter, errors);
        assertThat(errors.hasErrors(), is(true));
        assertThat(errors.getErrorCount(), is(2));
        assertThat(errors.getFieldError("productId").getRejectedValue(), is((Object)"something-else"));
        assertThat(errors.getFieldError("productId").getCode(), is("Product with id 'something-else' does not exist in the product catalogue"));
        assertThat(errors.getFieldError("quantity").getRejectedValue(), is((Object)new Integer(-1)));
        assertThat(errors.getFieldError("quantity").getCode(), is("Quantity should not be negative"));
    }

    @Test
    public void shouldNotAddErrorIfQuantityIsZeroAndProductExist() {
        when(productCatalogue.getProduct("something")).thenReturn(new Product("something", "name", somePrice()));
        CartItemValidator validator = new CartItemValidator(productCatalogue);
        CartItemParameter cartItemParameter = new CartItemParameter();
        cartItemParameter.setQuantity(0);
        cartItemParameter.setProductId("something");
        Errors errors = new BeanPropertyBindingResult(cartItemParameter, "cartItemParameter");
        validator.validate(cartItemParameter, errors);
        assertThat(errors.hasErrors(), is(false));
    }

    private Money somePrice() {
        return Money.of(CurrencyUnit.EUR, 37.28);
    }

}
