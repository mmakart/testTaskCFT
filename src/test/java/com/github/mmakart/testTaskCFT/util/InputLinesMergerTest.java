package com.github.mmakart.testTaskCFT.util;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InputLinesMergerTest {
    private final Predicate<String> DEFAULT_PREDICATE = element -> true;
    private final Comparator<String> DEFAULT_STRING_COMPARATOR = Comparator.nullsLast(Comparator.naturalOrder());
    private final Comparator<String> DEFAULT_NUMBER_COMPARATOR = Comparator.nullsLast(
            (a, b) -> new BigInteger(a).compareTo(new BigInteger(b)));

    @Test
    void next_noReaders_hasNoNextElement() {
        List<BufferedReader> readers = Collections.emptyList();

        InputLinesMerger merger = new InputLinesMerger(readers, DEFAULT_PREDICATE,
                DEFAULT_STRING_COMPARATOR);

        Assertions.assertFalse(merger.iterator().hasNext());
    }

    @Test
    void next_noInput_hasNoNextElement() {
        List<BufferedReader> readers = Collections.singletonList(new BufferedReader(Reader.nullReader()));

        InputLinesMerger merger = new InputLinesMerger(readers, DEFAULT_PREDICATE,
                DEFAULT_STRING_COMPARATOR);

        Assertions.assertFalse(merger.iterator().hasNext());
    }

    @Test
    void next_allInputIsIncorrect_hasNoNextElement() {
        List<BufferedReader> readers = Arrays.asList(
                new BufferedReader(new StringReader("NOTANUMBER\nANOTHER")),
                new BufferedReader(new StringReader("INCORRECT LINE\nANOTHER LINE")));

        InputLinesMerger merger = new InputLinesMerger(
                readers, s -> StringUtils.isNumeric(s), DEFAULT_NUMBER_COMPARATOR);

        Assertions.assertFalse(merger.iterator().hasNext());
    }

    @Test
    void next_sortedInput_sortedOutput() {
        List<BufferedReader> readers = Arrays.asList(
                new BufferedReader(
                        new StringReader(String.join("\n", "AB", "CD", "EF", "GH"))),
                new BufferedReader(
                        new StringReader(String.join("\n", "DE", "FG", "HI", "JK"))));

        InputLinesMerger merger = new InputLinesMerger(readers, DEFAULT_PREDICATE,
                DEFAULT_STRING_COMPARATOR);

        List<String> result = new ArrayList<>();
        for (String str : merger) {
            result.add(str);
        }

        List<String> sorted = List.of("AB", "CD", "DE", "EF", "FG", "GH", "HI", "JK");

        Assertions.assertEquals(sorted, result);
    }

    @Test
    void next_descdendingSortedInput_sortedOutput() {
        List<BufferedReader> readers = Arrays.asList(
                new BufferedReader(
                        new StringReader(String.join("\n", "ZY", "YX", "XW", "WV"))),
                new BufferedReader(
                        new StringReader(String.join("\n", "X", "W", "V", "U"))));

        InputLinesMerger merger = new InputLinesMerger(
                readers, DEFAULT_PREDICATE,
                Comparator.nullsLast(DEFAULT_STRING_COMPARATOR.reversed()));

        List<String> result = new ArrayList<>();
        for (String str : merger) {
            result.add(str);
        }

        List<String> sorted = List.of("ZY", "YX", "XW", "X", "WV", "W", "V", "U");

        Assertions.assertEquals(sorted, result);
    }
}
