package com.github.mmakart.testTaskCFT.util;

import com.github.mmakart.testTaskCFT.enums.DataType;
import com.github.mmakart.testTaskCFT.enums.SortMode;
import com.github.mmakart.testTaskCFT.exceptions.WrongArgumentsException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/**
 * Command line arguments parser.
 *
 * @author Mikhail Makartsev "m.p.makartsev@gmail.com"
 * @since 1.0
 */
public class AppSettingsParser {
    private List<String> options = new ArrayList<>();
    private String outputFilename = "";
    private List<String> inputFilenames = new ArrayList<>();

    /**
     * @param args Command line arguments passed to main().
     * @return Application settings to be used in the program.
     * @throws WrongArgumentsException when the program is started without
     *                                 required arguments.
     */
    public AppSettings parseSettings(String[] args)
            throws WrongArgumentsException {
        final List<String> argsList = Arrays.asList(args);

        ListIterator<String> iterator = argsList.listIterator();
        while (iterator.hasNext()) {
            String arg = iterator.next();
            if (!arg.startsWith("-")) {
                if (iterator.hasPrevious()) {
                    iterator.previous();
                }
                break;
            }
            options.add(arg);
        }

        if (iterator.hasNext()) {
            outputFilename = iterator.next();
        }

        while (iterator.hasNext()) {
            inputFilenames.add(iterator.next());
        }

        if (!isArgsCorrect()) {
            throw new WrongArgumentsException();
        }

        SortMode sortMode = SortMode.ASC;
        if (options.contains("-d")) {
            sortMode = SortMode.DESC;
        }

        DataType dataType = options.contains("-i") ? DataType.NUMBER : DataType.STRING;

        return new AppSettings(sortMode, dataType, outputFilename, inputFilenames);
    }

    private boolean isOptionsCorrect() {
        return options.size() == 1 &&
                (options.contains("-s") ^ options.contains("-i")) ||
                options.size() == 2 &&
                        (options.contains("-s") ^ options.contains("-i")) &&
                        (options.contains("-a") || options.contains("-d"));
    }

    private boolean isOutputFilenamePresent() {
        return !outputFilename.isBlank();
    }

    private boolean isInputFilenamesPresent() {
        return inputFilenames.size() >= 1;
    }

    private boolean isArgsCorrect() {
        return isOptionsCorrect() && isOutputFilenamePresent() &&
                isInputFilenamesPresent();
    }
}
