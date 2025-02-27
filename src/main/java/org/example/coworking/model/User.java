package org.example.coworking.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor // Конструктор без параметров для десериализации
@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type") // Указывает, что тип будет определяться по полю "type"
@JsonSubTypes({
        @JsonSubTypes.Type(value = Admin.class, name = "admin"),
        @JsonSubTypes.Type(value = Customer.class, name = "customer")
})
public class User {
    protected int id;
    protected String name;
    protected String password;
}
