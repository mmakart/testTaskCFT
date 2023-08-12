package com.github.mmakart.testTaskCFT.util;

import com.github.mmakart.testTaskCFT.exceptions.NoInputFilesException;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing several BufferedReaders to implement AutoCloseable
 * interface to use this in try-with-resources blocks.
 *
 * @author Mikhail Makartsev "m.p.makartsev@gmail.com"
 * @since 1.0
 */
public class ReaderList
        extends ArrayList<BufferedReader> implements AutoCloseable {
    public ReaderList(List<String> inputFilenames) throws NoInputFilesException {
        int counter = 0;
        for (String filename : inputFilenames) {
            try {
                BufferedReader reader = Files.newBufferedReader(Paths.get(filename),
                        StandardCharsets.UTF_8);
                add(reader);
                counter++;
            } catch (IOException e) {
                System.err.println(
                        "Warning: error occured while opening an input file " +
                                e.getMessage() + ". Skipping.");
            }
        }
        if (counter == 0) {
            throw new NoInputFilesException();
        }
    }

    @Override
    public void close() {
        for (BufferedReader reader : this) {
            try {
                reader.close();
            } catch (IOException e) {
                System.err.println(
                        "Warning: error occured while closing an input file " +
                                e.getMessage());
            }
        }
    }
}
