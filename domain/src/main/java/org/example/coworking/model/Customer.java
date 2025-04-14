package org.example.coworking.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {

    public Customer(Long id, String name, String password) {
        super(id, name, password);
    }
}
