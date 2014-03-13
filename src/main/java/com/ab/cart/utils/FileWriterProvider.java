package com.ab.cart.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriterProvider {  //todo rename to factory

    public BufferedWriter getFileWriter(String filePath) throws IOException {
        return new BufferedWriter(new FileWriter(filePath));
    }
}
