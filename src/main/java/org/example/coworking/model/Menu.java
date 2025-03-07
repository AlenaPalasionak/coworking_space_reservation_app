package org.example.coworking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    protected String menuName;
    protected String menuText;
    protected String [] possibleChoices;

}
