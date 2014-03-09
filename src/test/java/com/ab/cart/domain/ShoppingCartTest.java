package com.ab.cart.domain;

import com.ab.cart.domain.productcatalogue.ProductCatalogue;
import org.joda.money.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.joda.money.CurrencyUnit.EUR;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShoppingCartTest {

    @Mock
    ProductCatalogue productCatalogue;

    @Test
    public void shouldAddFirstItem() {
        ShoppingCart cart = new ShoppingCart(productCatalogueContainingEveryPossibleProduct());
        cart.addItem("Some-id-123", 1);
        assertThat(cart.getItems(), hasSize(1));
        assertThat(cart.getItem("Some-id-123").getQuantity(), is(1));
    }

    @Test
    public void shouldAddAdditionalItem() {
        ShoppingCart cart = new ShoppingCart(productCatalogueContainingEveryPossibleProduct());
        cart.addItem("Some-id-123", 1);
        cart.addItem("Another-id-566", 1);
        assertThat(cart.getItems(), hasSize(2));

        CartItem someCartItem = cart.getItem("Some-id-123");
        assertThat(someCartItem.getProductId(), is("Some-id-123"));
        assertThat(someCartItem.getQuantity(), is(1));
        CartItem anotherCartItem = cart.getItem("Another-id-566");
        assertThat(anotherCartItem.getProductId(), is("Another-id-566"));
        assertThat(anotherCartItem.getQuantity(), is(1));
    }

    @Test
    public void shouldAggregateItemsForTheSameProduct() {
        //given
        ShoppingCart cart = new ShoppingCart(productCatalogueContainingEveryPossibleProduct());
        cart.addItem("Id-of-product-to-be-added-twice-123", 1);
        cart.addItem("Another-id-566", 1);
        //when
        cart.addItem("Id-of-product-to-be-added-twice-123", 1);
        //then
        CartItem someCartItem = cart.getItem("Id-of-product-to-be-added-twice-123");
        assertThat(someCartItem.getProductId(), is("Id-of-product-to-be-added-twice-123"));
        assertThat(someCartItem.getQuantity(), is(2));
        CartItem anotherCartItem = cart.getItem("Another-id-566");
        assertThat(anotherCartItem.getProductId(), is("Another-id-566"));
        assertThat(anotherCartItem.getQuantity(), is(1));
    }

    private ProductCatalogue productCatalogueContainingEveryPossibleProduct() {
        ProductCatalogue catalogueContainingEveryPossibleProduct = productCatalogue;
        when(catalogueContainingEveryPossibleProduct.getProduct(anyString())).thenReturn(mock(Product.class));
        return catalogueContainingEveryPossibleProduct;
    }

    @Test(expected = ProductDoesNotExistException.class)
    public void shouldThrowExceptionWhenTryingToAddNonExistingProduct() {
        String idOfNonExistingProduct = "this-product-does-not-exist";
        when(productCatalogue.getProduct(idOfNonExistingProduct)).thenReturn(null);
        ShoppingCart cart = new ShoppingCart(productCatalogue);
        cart.addItem(idOfNonExistingProduct, 1);
    }
    @Test
    public void shouldReturnSubTotalForSingleItem() {
        //given
        Money productPrice = Money.of(EUR, 12.27);
        String productId = "product1";
        when(productCatalogue.getProduct(productId)).thenReturn(new Product(productId, "some product name", productPrice));
        ShoppingCart cart = new ShoppingCart(productCatalogue);
        cart.addItem(productId, 1);
        //then
        assertThat(cart.getSubTotal(), is(productPrice));
    }

    @Test
    public void shouldReturnSubTotalForMultipleItemsOfSameProduct() {
        //given
        Money productPrice = Money.of(EUR, 12.27);
        String productId = "product1";
        when(productCatalogue.getProduct(productId)).thenReturn(new Product(productId, "some product name", productPrice));
        ShoppingCart cart = new ShoppingCart(productCatalogue);
        cart.addItem(productId, 3);
        //then
        assertThat(cart.getSubTotal(), is(Money.of(EUR, 36.81)));
    }

    @Test
    public void shouldReturnSubTotalForMultipleItemsOfDifferentProducts() {
        //given
        Money product1Price = Money.of(EUR, 6.56);
        Money product2Price = Money.of(EUR, 29.99);
        Money product3Price = Money.of(EUR, 1);
        Product product1 = new Product("product-id-1", "some product name", product1Price);
        Product product2 = new Product("product-id-2", "some product name", product2Price);
        Product product3 = new Product("product-id-3", "some product name", product3Price);
        when(productCatalogue.getProduct(product1.getProductId())).thenReturn(product1);
        when(productCatalogue.getProduct(product2.getProductId())).thenReturn(product2);
        when(productCatalogue.getProduct(product3.getProductId())).thenReturn(product3);
        ShoppingCart cart = new ShoppingCart(productCatalogue);
        cart.addItem(product1.getProductId(), 3);
        cart.addItem(product2.getProductId(), 1);
        cart.addItem(product3.getProductId(), 7);
        //then
        assertThat(cart.getSubTotal(), is(Money.of(EUR, 56.67)));  // 6.56*3 + 29.99 + 1*7 = 56.67
    }

    @Test
    public void shouldDeleteItem() {
        fail();
    }

}
