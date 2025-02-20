package org.example.coworking.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class BaseHelper {
    public static boolean shouldExit(BufferedReader reader, BufferedWriter writer) throws IOException {
        writer.write("""
                                            
                Cansel the program?
                No - press 0
                Yes - press 1
                                                        
                """);
        writer.flush();
        String exitNotification2 = reader.readLine();
        if (exitNotification2.equals("1")) {
            return true;
        } else {
            return false;
        }
    }

}
