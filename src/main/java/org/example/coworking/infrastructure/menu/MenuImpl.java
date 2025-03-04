package org.example.coworking.infrastructure.menu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class MenuImpl implements Menu {
    protected String menu;

    public MenuImpl(String menu) {
        this.menu = menu;
    }

    public void showMenu(BufferedReader reader, BufferedWriter writer) throws IOException {
        writer.write(menu);
        writer.flush();
    }

    public String getUserChoice(BufferedReader reader, BufferedWriter writer, String[] possibleChoices) throws IOException {
        String userChoice = "wrong choice";
        boolean matchOneOfPossibleChoices = false;

        while (!matchOneOfPossibleChoices) {
            userChoice = reader.readLine();

            for (String possibleChoice : possibleChoices) {
                if (possibleChoice.equals(userChoice)) {
                    matchOneOfPossibleChoices = true;
                    break;
                }
            }

            if (!matchOneOfPossibleChoices) {
                writer.write("Wrong number: " + userChoice + "\nTry again\n");
                writer.flush(); // Сбрасываем буфер, чтобы сообщение отобразилось
            }
        }

        return userChoice;
    }
}
