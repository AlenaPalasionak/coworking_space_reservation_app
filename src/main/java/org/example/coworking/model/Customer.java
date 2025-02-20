package org.example.coworking.model;

import lombok.ToString;

import java.util.List;
@ToString
public class Customer extends User {
    List<Reservation> reservations;

    public Customer(int id, String name, String password) {
        super(id, name, password);
    }
}
