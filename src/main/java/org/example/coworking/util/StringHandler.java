package org.example.coworking.util;

import java.util.regex.Pattern;

public class StringHandler {

    public static boolean containsDigits(String str) {
        return Pattern.compile("\\d").matcher(str).find();
    }
}
