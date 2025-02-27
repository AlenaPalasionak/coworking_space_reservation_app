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

    public String getUserChoice(BufferedReader reader) throws IOException {
        return reader.readLine();
    }
}
