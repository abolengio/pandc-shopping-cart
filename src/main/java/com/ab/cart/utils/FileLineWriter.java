package com.ab.cart.utils;

import java.io.BufferedWriter;
import java.io.IOException;

public class FileLineWriter {

    private final FileWriterProvider fileWriterProvider;
    private final String filePath;

    public FileLineWriter(FileWriterProvider fileWriterProvider, String filePath) {

        this.fileWriterProvider = fileWriterProvider;
        this.filePath = filePath;
    }

    public void addLine(String line) {
        BufferedWriter fileWriter = null;
        try {
            fileWriter = fileWriterProvider.getFileWriter(filePath);
            fileWriter.write(line);
            fileWriter.newLine();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        } finally {
            if (fileWriter != null)
                try {
                    fileWriter.close();
                } catch (Exception ignore) {
                }
        }
    }
}
