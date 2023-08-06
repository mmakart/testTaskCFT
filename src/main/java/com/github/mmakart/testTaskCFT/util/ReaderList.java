package com.github.mmakart.testTaskCFT.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ReaderList extends ArrayList<BufferedReader> implements AutoCloseable {
    public ReaderList(List<String> inputFilenames) throws IOException {
        for (String filename : inputFilenames) {
            add(Files.newBufferedReader(Paths.get(filename), StandardCharsets.UTF_8));
        }
    }

    @Override
    public void close() {
        for (BufferedReader reader : this) {
            try {
                reader.close();
            } catch (IOException e) {
                System.err.println("Warning: error occured while closing an input file");
            }
        }
    }
}
