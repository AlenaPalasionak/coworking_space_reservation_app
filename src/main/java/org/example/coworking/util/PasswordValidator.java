package org.example.coworking.util;

import java.util.HashMap;
import java.util.Map;

public class PasswordValidator {
    public static Map<String, String> adminNameAndPass = new HashMap<>();
    public static Map<String, String> customerNameAndPass = new HashMap<>();

    public static void loadLoginData() {
        adminNameAndPass.put("a", "1");
        adminNameAndPass.put("a2", "2");
        customerNameAndPass.put("c", "3");
        customerNameAndPass.put("c2", "4");
    }

    public static boolean isAdminLoginDataValid(String name, String passWord) {
      return adminNameAndPass.entrySet().stream()
                .anyMatch(entry -> entry.getKey().equals(name) && entry.getValue().equals(passWord));
    }

    public static boolean isCustomerLoginDataValid(String name, String passWord) {
        return customerNameAndPass.entrySet().stream()
                .anyMatch(entry -> entry.getKey().equals(name) && entry.getValue().equals(passWord));
    }
}
