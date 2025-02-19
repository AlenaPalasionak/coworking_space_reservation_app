package org.example.coworking.util;

import java.util.HashMap;
import java.util.Map;

public class PasswordValidator {
    public static Map<String, String> adminNameAndPass = new HashMap<>();
    public static Map<String, String> customerNameAndPass = new HashMap<>();

    public static void loadLoginData() {
        adminNameAndPass.put("ad1", "11");
        adminNameAndPass.put("ad2", "22");
        customerNameAndPass.put("cus1", "33");
        customerNameAndPass.put("cus2", "44");
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
