package org.example.coworking.dao;

import org.example.coworking.model.Coworking;

import java.util.List;

public interface CoworkingDao {
    void addSpace(Coworking space);

    List<Coworking> getAllSpaces();

    void removeSpace(int id);

    void updateSpace(Coworking coworking, int id);

    Coworking getById(int id);
}
