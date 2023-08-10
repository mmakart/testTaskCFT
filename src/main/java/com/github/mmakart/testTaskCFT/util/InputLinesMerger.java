package com.github.mmakart.testTaskCFT.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InputLinesMerger implements Iterable<String> {

    private final List<? extends BufferedReader> readers;
    private final Predicate<? super String> isLineValid;
    private final Comparator<? super String> comparator;

    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            private final List<Boolean> listEOF;
            private final List<String> lastLines;

            {
                listEOF = new ArrayList<>(Collections.nCopies(readers.size(), false));
                lastLines = new ArrayList<>(Collections.nCopies(readers.size(), (String) null));
                firstIteration();
            }

            @Override
            public boolean hasNext() {
                return isMoreLines();
            }

            @Override
            public String next() {
                if (!isMoreLines()) {
                    throw new NoSuchElementException();
                }

                String minOrMax = lastLines.stream().min(comparator).get();

                int minOrMaxIndex = lastLines.indexOf(minOrMax);

                readNextValidLine(minOrMaxIndex);

                return minOrMax;
            }

            private boolean isMoreLines() {
                return listEOF.stream().anyMatch(isEOF -> !isEOF);
            }

            private void firstIteration() {
                for (int i = 0; i < readers.size(); i++) {
                    readNextValidLine(i);
                }
            }

            private void readNextValidLine(int index) {
                String line = null;
                do {
                    try {
                        line = readers.get(index).readLine();
                    } catch (IOException e) {
                        System.err.println(
                                "Warning: error occurred while reading an input file");
                    }
                } while (line != null && !isLineValid.test(line));

                lastLines.set(index, line);
                listEOF.set(index, line == null);
            }
        };
    }
}
