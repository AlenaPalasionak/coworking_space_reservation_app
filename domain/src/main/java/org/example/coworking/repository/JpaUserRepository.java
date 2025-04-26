package org.example.coworking.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.*;
import org.example.coworking.repository.exception.DaoErrorCode;
import org.example.coworking.repository.exception.DataExcessException;
import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;

@Repository("jpaUserRepository")
public class JpaUserRepository implements UserRepository {
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public JpaUserRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public <T extends User> T getUserByNamePasswordAndRole(String name, String password, Class<T> role) throws EntityNotFoundException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<T> cq = cb.createQuery(role);
        Root<T> root = cq.from(role);

        Predicate namePredicate = cb.equal(root.get("name"), name);
        Expression<String> cryptExpr = cb.function(
                "crypt",
                String.class,
                cb.literal(password),
                root.get("password")
        );

        Predicate passwordPredicate = cb.equal(cryptExpr, root.get("password"));
        cq.select(root).where(cb.and(namePredicate, passwordPredicate));

        try {
            return entityManager.createQuery(cq).getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException(String.format("Failure to find user with the name: %s", name), DaoErrorCode.USER_IS_NOT_FOUND);
        } catch (PersistenceException e) {
            TECHNICAL_LOGGER.error("Database error occurred while fetching user with the name: {}.", name, e);
            throw new DataExcessException(String.format("Database error occurred while fetching user with the name: %s.", name), e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public User getUserById(Long id) throws EntityNotFoundException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        User user;
        try {
            String checkUserQuery = """
                     SELECT u
                     FROM  User u
                     WHERE id=:id
                    """;
            user = entityManager.createQuery(checkUserQuery, User.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException(
                    String.format("Failure to find User with the ID: %d ", id),
                    DaoErrorCode.USER_IS_NOT_FOUND
            );
        } catch (PersistenceException e) {
            TECHNICAL_LOGGER.error("Database error occurred while fetching user with the ID: {}.", id, e);
            throw new DataExcessException(String.format("Database error occurred while fetching user with the ID: %d.", id), e);
        } finally {
            entityManager.close();
        }
        return user;
    }
}

