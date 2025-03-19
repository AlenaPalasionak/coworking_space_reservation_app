package org.example.coworking.model;

import lombok.Getter;
@Getter
public class Customer extends User {
    public Customer(Long id, String name, String password) {
        super(id, name, password);
    }
}
