package org.example.coworking.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


public class JpaConfig {
    private static final EntityManagerFactory entityManagerFactory;

    static {
        entityManagerFactory = Persistence.createEntityManagerFactory("coworking-space-reservation_db");
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}
