package com.ab.cart.repository.impl;

import com.ab.cart.domain.Product;
import com.ab.cart.utils.FileReaderProvider;
import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

//todo rename to Reader
public class CsvFileProductProvider {

    public static final String PRODUCT_CSV_FILE_PATH_PROPERTY = "product.catalogue.file";
    private final Environment environment;
    private final FileReaderProvider fileReaderProvider;
    private final ProductCsvEntryParser productCsvEntryParser;

    public CsvFileProductProvider(Environment environment,
                                  FileReaderProvider fileReaderProvider,
                                  ProductCsvEntryParser productCsvEntryParser){

        this.environment = environment;
        this.fileReaderProvider = fileReaderProvider;
        this.productCsvEntryParser = productCsvEntryParser;
    }

    //todo rename to read()
    public List<Product> parse() throws IOException {
        String productCsvFilePath = environment.getProperty(PRODUCT_CSV_FILE_PATH_PROPERTY);
        Reader reader = fileReaderProvider.getFileReader(productCsvFilePath);
        CSVReader<Product> csvPersonReader = new CSVReaderBuilder<Product>(reader).strategy(CSVStrategy.UK_DEFAULT)
                .entryParser(productCsvEntryParser).build();
        List<Product> products = csvPersonReader.readAll();
        //todo close the reader and test for closure
        return products;
    }
}
