package org.example.coworking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class User {
    protected int id;
    protected String name;
    protected String password;
}
