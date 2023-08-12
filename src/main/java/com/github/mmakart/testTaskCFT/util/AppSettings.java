package com.github.mmakart.testTaskCFT.util;

import com.github.mmakart.testTaskCFT.enums.DataType;
import com.github.mmakart.testTaskCFT.enums.SortMode;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Class describing the application settings.
 *
 * @author Mikhail Makartsev "m.p.makartsev@gmail.com"
 * @since 1.0
 */
@AllArgsConstructor
@Getter
public class AppSettings {
    /** Field specifying whether is sort order ascending or descending. */
    private final SortMode sortMode;
    /** Field specifying how to treat sort data (e.g. number or string). */
    private final DataType dataType;
    /** Field specifying to what file sorted data should be sent. */
    private final String outputFilename;
    /**
     * List specifying what files should be read to sort.
     * The files are considered to contain only non-whitespace sorted lines.
     */
    private final List<String> inputFilenames;
}
