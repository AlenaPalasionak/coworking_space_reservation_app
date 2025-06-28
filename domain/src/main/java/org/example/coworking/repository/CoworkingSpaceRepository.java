package org.example.coworking.repository;

import org.example.coworking.entity.CoworkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CoworkingSpaceRepository extends JpaRepository<CoworkingSpace, Long> {
    @Query("""
            SELECT cs
            FROM CoworkingSpace cs
            LEFT JOIN FETCH cs.facilities""")
    List<CoworkingSpace> findAll();

    @Query("""
            SELECT cs FROM CoworkingSpace cs
            LEFT JOIN FETCH cs.facilities
            WHERE cs.admin.id = :adminId
            """)
    List<CoworkingSpace> getAllCoworkingSpacesByAdmin(Long adminId);
}
