package org.example.coworking.dao;

import org.example.coworking.model.Coworking;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CoworkingDaoImpl implements CoworkingDao {
    private List<Coworking> coworkingSpaces = new ArrayList<>();

    @Override
    public void addSpace(Coworking space) {
        coworkingSpaces.add(space);
    }

    @Override
    public List<Coworking> getAllSpaces() {
        return coworkingSpaces;
    }

    @Override
    public void removeSpace(int id) {
        coworkingSpaces = coworkingSpaces.stream()
                .filter(coworking -> coworking.getId() != id)
                .collect(Collectors.toList());
    }

    @Override
    public void updateSpace(Coworking newCoworking, int id) {
        coworkingSpaces = coworkingSpaces.stream()
                .map(oldCoworking -> oldCoworking.getId() == newCoworking.getId() ? newCoworking : oldCoworking)
                .collect(Collectors.toList());
    }

    @Override
    public Coworking getById(int id) {
        return coworkingSpaces.get(id);
    }
}
