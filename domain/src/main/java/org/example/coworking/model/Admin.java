package org.example.coworking.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class Admin extends User {

    public Admin(Long id, String name, String password) {
        super(id, name, password);
    }
}
