package com.ab.cart.repository.impl;

import com.ab.cart.domain.Product;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.googlecode.jcsv.reader.CSVEntryParser;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.BigDecimal;

import static java.lang.String.format;

public class ProductCsvEntryParser implements CSVEntryParser<Product> {

    private ProductRebateTimeFrameParser rebateTimeFrameParser;

    public ProductCsvEntryParser(ProductRebateTimeFrameParser rebateTimeFrameParser) {
        this.rebateTimeFrameParser = rebateTimeFrameParser;
    }

    @Override
    public Product parseEntry(String... data) {
        if(data.length < 3) {
            throw new IllegalArgumentException(format("Error parsing product data, too few data fields supplied; " +
                                                "at least product_id, name and price are expected. Supplied fields are %s",
                                                Joiner.on(", ").join(data)));
        }
        String productId = CharMatcher.WHITESPACE.trimFrom(data[0]);
        String productName = CharMatcher.WHITESPACE.trimFrom(data[1]);
        Money productPrice;
        try {
            productPrice = Money.of(CurrencyUnit.EUR, new BigDecimal(CharMatcher.WHITESPACE.trimFrom(data[2])));
        } catch(Exception exc){
            throw new IllegalArgumentException(format("Error parsing product data, failed to parse price field; " +
                    "Supplied price data is '%s' in the line containing: %s",
                    data[2],
                    Joiner.on(", ").join(data)), exc);
        }
        return new Product(productId, productName, productPrice);
    }
}
