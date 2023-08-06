package com.github.mmakart.testTaskCFT.util;

import java.util.List;

import com.github.mmakart.testTaskCFT.enums.DataType;
import com.github.mmakart.testTaskCFT.enums.SortMode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppSettings {
    private final SortMode sortMode;
    private final DataType dataType;
    private final String outputFilename;
    private final List<String> inputFilenames;
}
