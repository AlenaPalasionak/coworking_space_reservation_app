package org.example.coworking.dao;

import jakarta.persistence.*;
import org.example.coworking.config.JpaConfig;
import org.example.coworking.dao.exception.DaoErrorCode;
import org.example.coworking.dao.exception.DataExcessException;
import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.Reservation;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;

public class JpaReservationDao implements ReservationDao {
    private final EntityManagerFactory entityManagerFactory;

    public JpaReservationDao() {
        this.entityManagerFactory = JpaConfig.getEntityManagerFactory();
    }

    @Override
    public void create(Reservation reservation) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(reservation);
            transaction.commit();

        } catch (PersistenceException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            TECHNICAL_LOGGER.error("Database error occurred while creating a new Reservation: {}", reservation, e);
            throw new DataExcessException(String.format("Database error occurred while creating a new Reservation: %s", reservation), e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void delete(Reservation reservation) {
        Long reservationId = reservation.getId();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            String deleteReservationQuery = """
                    DELETE FROM Reservation
                    WHERE id = :reservationId
                    """;
            entityManager
                    .createQuery(deleteReservationQuery)
                    .setParameter("reservationId", reservationId)
                    .executeUpdate();

            transaction.commit();
        } catch (PersistenceException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            TECHNICAL_LOGGER.error("Database error occurred while deleting Reservation with ID: {}.",
                    reservationId, e);
            throw new DataExcessException(String.format("Database error occurred while deleting Reservation with ID: %d.",
                    reservationId), e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Reservation getById(Long reservationId) throws EntityNotFoundException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String reservationQuery = """
                    SELECT r
                    FROM Reservation r
                    WHERE id = :reservationId
                    """;

            return entityManager
                    .createQuery(reservationQuery, Reservation.class)
                    .setParameter("reservationId", reservationId)
                    .getSingleResult();

        } catch (NoResultException e) {
            throw new EntityNotFoundException(String.format("Failure to get Reservation with ID: %d",
                    reservationId), DaoErrorCode.COWORKING_IS_NOT_FOUND);
        } catch (PersistenceException e) {
            TECHNICAL_LOGGER.error("Database error while getting Reservation by ID: {}", reservationId, e);
            throw new DataExcessException(
                    String.format("Database error while getting Reservation by ID: %d", reservationId), e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Reservation> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String reservationsQuery = """
                     SELECT r
                     FROM Reservation r
                    """;
            return entityManager.createQuery(reservationsQuery, Reservation.class)
                    .getResultList();
        } catch (PersistenceException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Database error occurred while getting Reservations. ", e);
        } finally {
            entityManager.close();
        }

    }

    public Set<Reservation> getAllReservationsByCoworking(Long coworkingSpaceId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String reservationsQuery = """
                            SELECT r
                            FROM Reservation r
                            WHERE r.coworkingSpace.id = :coworkingSpaceId
                    """;

            return new TreeSet<>(entityManager
                    .createQuery(reservationsQuery, Reservation.class)
                    .setParameter("coworkingSpaceId", coworkingSpaceId)
                    .getResultList());
        } catch (PersistenceException e) {
            TECHNICAL_LOGGER.error("Database error occurred while getting reservations by Coworking Space ID: {}.",
                    coworkingSpaceId, e);
            throw new DataExcessException(String.format("Database error occurred while getting reservations by Coworking Space ID: %d.",
                    coworkingSpaceId), e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Reservation> getAllReservationsByCustomer(Long customerId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String reservationsQuery = """
                            SELECT r
                            FROM Reservation r
                            WHERE r.customer.id = :customerId
                    """;

            return entityManager
                    .createQuery(reservationsQuery, Reservation.class)
                    .setParameter("customerId", customerId)
                    .getResultList();
        } catch (PersistenceException e) {
            TECHNICAL_LOGGER.error("Database error occurred while getting reservations by customer ID: {}.",
                    customerId, e);
            throw new DataExcessException(String.format("Database error occurred while getting reservations by customer ID: %d.",
                    customerId), e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Reservation> getAllReservationsByAdmin(Long adminId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String reservationsQuery = """
                            SELECT r
                            FROM Reservation r
                            WHERE r.coworkingSpace.admin.id = :adminId
                    """;

            return entityManager
                    .createQuery(reservationsQuery, Reservation.class)
                    .setParameter("adminId", adminId)
                    .getResultList();
        } catch (PersistenceException e) {
            TECHNICAL_LOGGER.error("Database error occurred while getting reservations by customer ID: {}.",
                    adminId, e);
            throw new DataExcessException(String.format("Database error occurred while getting reservations by customer ID: %d.",
                    adminId), e);
        } finally {
            entityManager.close();
        }
    }
}
