package org.example.coworking.infrastructure.util;

import java.util.regex.Pattern;

public class StringHandler {

    public static boolean isFacilityStringFromUserValid(String input) {
        return Pattern.compile("^\\s*\\d+(\\s*,\\s*\\d+)*\\s*$").matcher(input).find();
    }
    public static boolean containsOnlyNumbers(String input) {
        return input.matches("\\d+");
    }
}
