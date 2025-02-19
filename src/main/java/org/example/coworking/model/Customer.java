package org.example.coworking.model;

import java.util.List;

public class Customer extends User {
    List<Reservation> reservations;

    public Customer(int id, String name, String password) {
        super(id, name, password);
    }
}
