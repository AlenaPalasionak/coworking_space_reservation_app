package org.example.coworking.infrastructure.util;

import java.util.regex.Pattern;

public class StringHandler {

    public static boolean isFacilityStringFromUserValid(String str) {
        return Pattern.compile("^\\s*\\d+(\\s*,\\s*\\d+)*\\s*$").matcher(str).find();
    }
}
