package org.example.coworking.repository;

import org.example.coworking.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("""
            SELECT r
            FROM Reservation r
            WHERE r.coworkingSpace.id = :coworkingSpaceId
            """)
    Set<Reservation> getAllReservationsByCoworking(Long coworkingSpaceId);

    @Query("""
            SELECT r
            FROM Reservation r
            JOIN FETCH r.coworkingSpace cs
            JOIN FETCH cs.admin
            WHERE r.customer.id = :customerId
            """)
    List<Reservation> getAllReservationsByCustomer(Long customerId);

    @Query("""
            SELECT r
            FROM Reservation r
            JOIN FETCH r.coworkingSpace cs
            JOIN FETCH cs.admin
            WHERE r.coworkingSpace.admin.id = :adminId
            """)
    List<Reservation> getAllReservationsByAdmin(Long adminId);
}
