package com.ab.cart.repository.impl;

import com.ab.cart.domain.Product;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ProductCsvEntryParserTest {

    @Mock
    ProductRebateTimeFrameParser rebateTimeFrameParser;

    @Test
    public void shouldConvertToProductWithoutRebate() {
        String[] data = {"789", "some name", "29.99"};
        ProductCsvEntryParser parser = new ProductCsvEntryParser(rebateTimeFrameParser);
        Product result = parser.parseEntry(data);
        assertThat(result.getPrice(), is(Money.of(CurrencyUnit.EUR, 29.99)));
        assertThat(result.getName(), is("some name"));
        assertThat(result.getProductId(), is("789"));
    }

    @Test
    public void shouldIgnoreWhitespacesAroundDataFields() {
        String[] data = {"789 ", "   some name ", " 29.99"};
        ProductCsvEntryParser parser = new ProductCsvEntryParser(rebateTimeFrameParser);
        Product result = parser.parseEntry(data);
        assertThat(result.getPrice(), is(Money.of(CurrencyUnit.EUR, 29.99)));
        assertThat(result.getName(), is("some name"));
        assertThat(result.getProductId(), is("789"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenPriceIsNotANumber() {
        String[] data = {"789", "some name", "notanumber"};
        ProductCsvEntryParser parser = new ProductCsvEntryParser(rebateTimeFrameParser);
        parser.parseEntry(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenOnlyTwoPiecesOfDataPassed() {
        String[] data = {"789", "some name"};
        ProductCsvEntryParser parser = new ProductCsvEntryParser(rebateTimeFrameParser);
        parser.parseEntry(data);
    }

}
