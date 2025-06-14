package org.example.coworking.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import org.example.coworking.repository.exception.RepositoryErrorCode;
import org.example.coworking.repository.exception.DataExcessException;
import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.model.CoworkingSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;

@Repository("jpaCoworkingRepository")
public class JpaCoworkingRepository implements CoworkingRepository {
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public JpaCoworkingRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void create(CoworkingSpace coworkingSpace) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(coworkingSpace);
            transaction.commit();
        } catch (PersistenceException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            TECHNICAL_LOGGER.error("Database error occurred while creating a new Coworking: {}.", coworkingSpace, e);
            throw new DataExcessException(String.format("Database error occurred while creating a new Coworking: %s.", coworkingSpace), e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void delete(CoworkingSpace coworkingSpace) {
        Long coworkingSpaceId = coworkingSpace.getId();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            String deleteCoworkingQuery = """
                    DELETE FROM CoworkingSpace
                    WHERE id = :id
                    """;

            entityManager
                    .createQuery(deleteCoworkingQuery)
                    .setParameter("id", coworkingSpaceId)
                    .executeUpdate();

            transaction.commit();
        } catch (PersistenceException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            TECHNICAL_LOGGER.error("Database error occurred while deleting Coworking space with ID: {} and related reservations.",
                    coworkingSpaceId, e);
            throw new DataExcessException(String.format("Database error occurred while deleting Coworking space with ID: %d and related reservations.",
                    coworkingSpaceId), e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public CoworkingSpace getById(Long coworkingId) throws EntityNotFoundException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            CoworkingSpace coworkingSpace = entityManager.find(CoworkingSpace.class, coworkingId);
            if (coworkingSpace == null) {
                throw new EntityNotFoundException(String.format("Failure to get Coworking space with ID: %d",
                        coworkingId), RepositoryErrorCode.COWORKING_IS_NOT_FOUND);
            }
            return coworkingSpace;
        } catch (PersistenceException e) {
            TECHNICAL_LOGGER.error("Database error while getting coworking space by ID: {}", coworkingId, e);
            throw new DataExcessException(
                    String.format("Database error while getting coworking space by ID: %d", coworkingId), e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<CoworkingSpace> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String coworkingSpaceQuery = """
                    SELECT cs FROM CoworkingSpace cs LEFT JOIN FETCH cs.facilities
                    """;
            return entityManager
                    .createQuery(coworkingSpaceQuery, CoworkingSpace.class)
                    .getResultList();
        } catch (PersistenceException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Database error occurred while getting Coworking spaces. ", e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<CoworkingSpace> getAllCoworkingSpacesByAdmin(Long adminId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String coworkingSpacesQuery = """
                    SELECT cs FROM CoworkingSpace cs
                    LEFT JOIN FETCH cs.facilities
                    WHERE cs.admin.id = :adminId
                    """;
            return entityManager
                    .createQuery(coworkingSpacesQuery, CoworkingSpace.class)
                    .setParameter("adminId", adminId)
                    .getResultList();
        } catch (PersistenceException e) {
            TECHNICAL_LOGGER.error("Database error occurred while getting Coworking spaces by admin ID: %d: {}", adminId, e);
            throw new DataExcessException(String.format("Database error occurred while getting Coworking spaces by admin ID: %d ", adminId), e);
        } finally {
            entityManager.close();
        }
    }
}