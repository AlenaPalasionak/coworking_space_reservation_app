package org.example.coworking.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class Admin extends User {
    public Admin(Long id, String name, String password) {
        super(id, name, password);
    }

}
