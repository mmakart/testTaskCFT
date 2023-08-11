package com.github.mmakart.testTaskCFT.util;

import java.io.BufferedReader;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WrongOrderFilterTest {
    private final Predicate<String> STANDARD_NUMBER_PREDICATE = StringUtils::isNumeric;
    private final Comparator<String> ASCENDING_NUMBER_COMPARATOR = Comparator.nullsLast(
            (a, b) -> new BigInteger(a).compareTo(new BigInteger(b)));
    private final Comparator<String> DESCENDING_NUMBER_COMPARATOR = Comparator
            .nullsLast(ASCENDING_NUMBER_COMPARATOR.reversed());

    @Test
    void next_unsortedInput_ascendingFilteredOutput() {
        String input1 = String.join("\n", "15", "8", "6", "23");
        String input2 = String.join("\n", "2", "3", "1");
        String input3 = String.join("\n", "100", "300", "400", "500", "200");

        List<BufferedReader> readers = List.of(new BufferedReader(new StringReader(input1)),
                new BufferedReader(new StringReader(input2)),
                new BufferedReader(new StringReader(input3)));

        InputLinesMerger merger = new InputLinesMerger(
                readers, STANDARD_NUMBER_PREDICATE, ASCENDING_NUMBER_COMPARATOR);

        WrongOrderFilter filter = new WrongOrderFilter(merger, ASCENDING_NUMBER_COMPARATOR);

        List<String> expectedList = List.of("2", "3", "15", "23", "100", "300", "400", "500");

        List<String> result = new ArrayList<>();
        for (String str : filter) {
            result.add(str);
        }

        Assertions.assertEquals(expectedList, result);
    }

    @Test
    void next_unsortedInput_descendingFilteredOutput() {
        String input1 = String.join("\n", "15", "8", "6", "23");
        String input2 = String.join("\n", "2", "3", "1");
        String input3 = String.join("\n", "100", "300", "400", "500", "200");

        List<BufferedReader> readers = List.of(new BufferedReader(new StringReader(input1)),
                new BufferedReader(new StringReader(input2)),
                new BufferedReader(new StringReader(input3)));

        InputLinesMerger merger = new InputLinesMerger(
                readers, STANDARD_NUMBER_PREDICATE, DESCENDING_NUMBER_COMPARATOR);

        WrongOrderFilter filter = new WrongOrderFilter(merger, DESCENDING_NUMBER_COMPARATOR);

        List<String> expectedList = List.of("100", "15", "8", "6", "2", "1");

        List<String> result = new ArrayList<>();
        for (String str : filter) {
            result.add(str);
        }

        Assertions.assertEquals(expectedList, result);
    }
}
