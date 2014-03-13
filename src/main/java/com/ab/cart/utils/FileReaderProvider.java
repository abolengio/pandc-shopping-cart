package com.ab.cart.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class FileReaderProvider {   //todo rename to factory and return BufferedReader or Scanner

    public Reader getFileReader(String filePath) throws FileNotFoundException {
        return new FileReader(filePath);
    }

}
