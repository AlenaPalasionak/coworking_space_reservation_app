package org.example.coworking.dao;

import org.example.coworking.model.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuDao {
    List<Menu> getMenusFromStorage();
    Optional<Menu> getMenuByName(String name);
}
