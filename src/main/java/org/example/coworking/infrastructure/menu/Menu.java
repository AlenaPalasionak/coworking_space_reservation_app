package org.example.coworking.infrastructure.menu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public interface Menu {
    void showMenu(BufferedReader reader, BufferedWriter writer) throws IOException;
    String getUserChoice(BufferedReader reader) throws IOException;


}
