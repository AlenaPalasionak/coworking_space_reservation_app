package org.example.coworking.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Customer extends User {
    
    public Customer(Long id, String name, String password) {
        super(id, name, password);
    }
}
