package com.ab.cart.repository.impl;

import com.ab.cart.domain.Product;
import com.ab.cart.repository.ProductListReader;
import com.ab.cart.utils.FileReaderFactory;
import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class ProductCsvFileReader implements ProductListReader {

    public static final String PRODUCT_CSV_FILE_PATH_PROPERTY = "product.catalogue.file";
    private final Environment environment;
    private final FileReaderFactory fileReaderFactory;
    private final ProductCsvEntryParser productCsvEntryParser;

    public ProductCsvFileReader(Environment environment,
                                FileReaderFactory fileReaderFactory,
                                ProductCsvEntryParser productCsvEntryParser){

        this.environment = environment;
        this.fileReaderFactory = fileReaderFactory;
        this.productCsvEntryParser = productCsvEntryParser;
    }

    @Override
    public List<Product> read() {
        String productCsvFilePath = environment.getProperty(PRODUCT_CSV_FILE_PATH_PROPERTY);
        Reader reader = null;
        try {
            reader = fileReaderFactory.getFileReader(productCsvFilePath);
            CSVReader<Product> csvPersonReader = new CSVReaderBuilder<Product>(reader).strategy(CSVStrategy.UK_DEFAULT)
                    .entryParser(productCsvEntryParser).build();
            return csvPersonReader.readAll();
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch(IOException ignore) {
                }
        }
    }
}
