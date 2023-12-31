package com.github.mmakart.testTaskCFT.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;

/**
 * Class filtering out wrong order lines from iterable source.
 *
 * @author Mikhail Makartsev "m.p.makartsev@gmail.com"
 * @since 1.0
 */
@RequiredArgsConstructor
public class WrongOrderFilter implements Iterable<String> {
    private final Iterable<String> iterable;
    private final Comparator<String> comparator;

    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            private final Iterator<String> iterator = iterable.iterator();
            private String currentLine;
            private String previousLine;
            private boolean isFirstIteration = true;
            private boolean hasNextLine;

            @Override
            public boolean hasNext() {
                while (iterator.hasNext()) {
                    hasNextLine = true;
                    currentLine = iterator.next();
                    boolean isRightOrder = comparator.compare(currentLine, previousLine) >= 0;

                    if (isFirstIteration || isRightOrder) {
                        isFirstIteration = false;
                        previousLine = currentLine;

                        return true;
                    }
                }
                hasNextLine = false;
                return false;
            }

            @Override
            public String next() {
                if (!hasNextLine) {
                    throw new NoSuchElementException();
                }

                return currentLine;
            }
        };
    }
}
