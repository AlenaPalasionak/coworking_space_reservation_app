package org.example.coworking.service;

import org.example.coworking.model.Coworking;

import java.util.List;

public interface CoworkingServise {
    void addSpace(Coworking space);

    List<Coworking> getAllSpaces();

    void removeSpace(int id);

    void updateSpace(Coworking newCoworkingVersion, int oldCoworkingVersionId);

    Coworking getById(int id);
}
