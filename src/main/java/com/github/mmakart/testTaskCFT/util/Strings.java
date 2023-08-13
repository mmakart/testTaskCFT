package com.github.mmakart.testTaskCFT.util;

import java.util.regex.Pattern;

public class Strings {
    private static final Pattern numberPattern = Pattern.compile("[-+]?\\d+");

    public static boolean isNumber(String s) {
        return numberPattern.matcher(s).matches();
    }
}
