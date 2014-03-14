package com.ab.cart.utils;

import java.io.BufferedWriter;
import java.io.IOException;

public class FileLineWriter {

    private final FileWriterFactory fileWriterFactory;
    private final String filePath;

    public FileLineWriter(FileWriterFactory fileWriterFactory, String filePath) {

        this.fileWriterFactory = fileWriterFactory;
        this.filePath = filePath;
    }

    public void addLine(String line) {
        BufferedWriter fileWriter = null;
        try {
            fileWriter = fileWriterFactory.getFileWriter(filePath);
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
