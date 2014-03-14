package com.ab.cart.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class FileReaderFactory {

    public Reader getFileReader(String filePath) throws FileNotFoundException {
        return new FileReader(filePath);
    }

}
