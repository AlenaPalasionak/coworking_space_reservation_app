package org.example.coworking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    private String menuName;
    private String menuText;
    private String [] possibleChoices;

}
