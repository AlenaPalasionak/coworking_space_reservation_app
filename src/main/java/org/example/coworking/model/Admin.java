package org.example.coworking.model;

import lombok.ToString;

@ToString
public class Admin extends User {
    public Admin(int id, String name, String password) {
        super(id, name, password);
    }
}
