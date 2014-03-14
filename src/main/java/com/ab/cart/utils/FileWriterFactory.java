package com.ab.cart.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriterFactory {

    public BufferedWriter getFileWriter(String filePath) throws IOException {
        return new BufferedWriter(new FileWriter(filePath, true));
    }
}
