package org.example.coworking.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    public Admin(Long id, String name, String password) {
        super(id, name, password);
    }
}
