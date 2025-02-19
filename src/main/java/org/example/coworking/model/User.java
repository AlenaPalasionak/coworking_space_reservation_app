package org.example.coworking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
    protected int id;
    protected String name;
    protected String password;
}
