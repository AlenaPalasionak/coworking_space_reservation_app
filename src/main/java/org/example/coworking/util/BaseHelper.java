package org.example.coworking.util;

import org.example.coworking.model.Reservation;
import org.example.coworking.model.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public interface BaseHelper {

    String showMenu(BufferedReader reader, BufferedWriter writer) throws IOException;

    //Optional<User> authenticate(int id, String name, String password, Class<? extends User> roleClass) throws IOException;

    void add(BufferedReader reader, BufferedWriter writer, User user) throws IOException;

    List<Reservation> getAllReservations(BufferedWriter writer, User customer) throws IOException;

    void delete(BufferedReader reader, BufferedWriter writer, User user) throws IOException;
    }