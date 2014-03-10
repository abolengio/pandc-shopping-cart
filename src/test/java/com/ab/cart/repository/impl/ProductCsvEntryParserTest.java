package com.ab.cart.repository.impl;

import com.ab.cart.domain.Product;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.time.ReadableInterval;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        assertThat(result.getRebateTimeFrame(), is(nullValue()));
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

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfProductIdFieldIsEmpty() {
        String[] data = {"", "some name", "1"};
        ProductCsvEntryParser parser = new ProductCsvEntryParser(rebateTimeFrameParser);
        parser.parseEntry(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfProductNameFieldIsEmpty() {
        String[] data = {"blah", " ", "1"};
        ProductCsvEntryParser parser = new ProductCsvEntryParser(rebateTimeFrameParser);
        parser.parseEntry(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfPriceFieldIsEmpty() {
        String[] data = {"some", "some name", ""};
        ProductCsvEntryParser parser = new ProductCsvEntryParser(rebateTimeFrameParser);
        parser.parseEntry(data);
    }

    @Test
    public void shouldPassRebateTimeFrameToParser() {
        String[] data = {"789", "some name", "29.99","rebate timeframe"};
        ReadableInterval rebateTimeFrame = mock(ReadableInterval.class);
        when(rebateTimeFrameParser.parse("rebate timeframe")).thenReturn(rebateTimeFrame);
        ProductCsvEntryParser parser = new ProductCsvEntryParser(rebateTimeFrameParser);
        Product result = parser.parseEntry(data);
        assertThat(result.getPrice(), is(Money.of(CurrencyUnit.EUR, 29.99)));
        assertThat(result.getName(), is("some name"));
        assertThat(result.getProductId(), is("789"));
        assertThat(result.getRebateTimeFrame(), is(rebateTimeFrame));
    }

    @Test
    public void shouldIgnoreRebateTimeFrameIfItsEmpty() {
        String[] data = {"789", "some name", "29.99"," "};
        ReadableInterval rebateTimeFrame = mock(ReadableInterval.class);
        when(rebateTimeFrameParser.parse("")).thenThrow(new IllegalArgumentException());
        ProductCsvEntryParser parser = new ProductCsvEntryParser(rebateTimeFrameParser);
        Product result = parser.parseEntry(data);
        assertThat(result.getPrice(), is(Money.of(CurrencyUnit.EUR, 29.99)));
        assertThat(result.getName(), is("some name"));
        assertThat(result.getProductId(), is("789"));
        assertThat(result.getRebateTimeFrame(), is(nullValue()));
    }

    @Test
    public void shouldIgnoreWhitespacesAroundDataFields() {
        String[] data = {"789 ", "   some name ", " 29.99", "  something here "};
        ReadableInterval rebateTimeframe = mock(ReadableInterval.class);
        when(rebateTimeFrameParser.parse("something here")).thenReturn(rebateTimeframe);
        ProductCsvEntryParser parser = new ProductCsvEntryParser(rebateTimeFrameParser);
        Product result = parser.parseEntry(data);
        assertThat(result.getPrice(), is(Money.of(CurrencyUnit.EUR, 29.99)));
        assertThat(result.getName(), is("some name"));
        assertThat(result.getProductId(), is("789"));
        assertThat(result.getRebateTimeFrame(), is(rebateTimeframe));
    }

}
