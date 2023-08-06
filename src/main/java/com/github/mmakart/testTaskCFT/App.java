package com.github.mmakart.testTaskCFT;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

import com.github.mmakart.testTaskCFT.enums.DataType;
import com.github.mmakart.testTaskCFT.enums.SortMode;
import com.github.mmakart.testTaskCFT.exceptions.NoInputFilesException;
import com.github.mmakart.testTaskCFT.exceptions.WrongArgumentsException;
import com.github.mmakart.testTaskCFT.util.AppSettings;
import com.github.mmakart.testTaskCFT.util.AppSettingsParser;
import com.github.mmakart.testTaskCFT.util.InputLinesMerger;
import com.github.mmakart.testTaskCFT.util.ReaderList;

public class App {
    private static final String usage = "Usage: java -jar App.jar [-a|-d] {-s|-i} <output file> <input file(s)>...\n" +
            "    -a    sort in ascending order (optional, default value)\n" +
            "    -d    sort in descending order (optional)\n" +
            "    -s    sort input files content as strings (required)\n" +
            "    -i    sort input files content as integers (required)";

    private static AppSettings settings;

    public static void main(String[] args) {
        AppSettingsParser parser = new AppSettingsParser();

        try {
            settings = parser.parseSettings(args);
        } catch (WrongArgumentsException e) {
            System.err.println(usage);
            System.exit(1);
        }

        String outputFilename = settings.getOutputFilename();
        List<String> inputFilenames = settings.getInputFilenames();

        Comparator<String> comparator = computeComparator();
        Predicate<String> isLineValid = computePredicate();

        try (PrintWriter writer = new PrintWriter(outputFilename, StandardCharsets.UTF_8);
                ReaderList readers = new ReaderList(inputFilenames)) {

            InputLinesMerger merger = new InputLinesMerger(readers, isLineValid, comparator);

            String previousStr = null;
            long iteration = 0;
            for (String str : merger) {
                if (iteration++ == 0 || Objects.compare(str, previousStr, comparator) >= 0) {
                    writer.println(str);
                }
                previousStr = str;
            }

        } catch (NoInputFilesException e) {
            System.err.println("Error: none of input files can be opened. Quitting.");
            System.exit(2);
        } catch (IOException e) {
            System.err.printf("%s: Input/output error occurred. Quitting.%n", e.getMessage());
            System.exit(3);
        }
    }

    private static Comparator<String> computeComparator() {
        // nullsLast because Stream.min() throws NPE if the minimum element is null
        Comparator<String> ascStrComparator = Comparator.nullsLast(Comparator.naturalOrder());
        Comparator<String> descStrComparator = Comparator.nullsLast(Comparator.reverseOrder());
        Comparator<String> strComparator = settings.getSortMode() == SortMode.ASC ? ascStrComparator
                : descStrComparator;

        Comparator<String> ascNumComparator = Comparator
                .nullsLast((a, b) -> new BigInteger(a).compareTo(new BigInteger(b)));
        Comparator<String> descNumComparator = Comparator.nullsLast(ascNumComparator.reversed());
        Comparator<String> numComparator = settings.getSortMode() == SortMode.ASC ? ascNumComparator
                : descNumComparator;

        return settings.getDataType() == DataType.NUMBER ? numComparator : strComparator;
    }

    private static Predicate<String> computePredicate() {
        Predicate<String> isLineWithoutSpaces = str -> str == null || !StringUtils.containsWhitespace(str);
        Predicate<String> isNumber = isLineWithoutSpaces.and(StringUtils::isNumeric);

        return settings.getDataType() == DataType.NUMBER ? isNumber : isLineWithoutSpaces;
    }
}
