package org.example.coworking.service;

import org.example.coworking.model.Coworking;

import java.util.List;

public interface CoworkingService {
    void addSpace(Coworking space);

    List<Coworking> getAllSpaces();

    void removeSpace(int id);

    void updateSpace(Coworking coworking, int id);
}
