package org.example.coworking.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.*;
import org.example.coworking.dao.exception.DaoErrorCode;
import org.example.coworking.dao.exception.DataExcessException;
import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;


@Repository("jpaUserDao")
public class JpaUserDao implements UserDao {
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public JpaUserDao(EntityManagerFactory entityManagerFactory) {
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
            throw new EntityNotFoundException("Failure to find user with the name: " + name, DaoErrorCode.USER_IS_NOT_FOUND);
        } catch (PersistenceException e) {
            TECHNICAL_LOGGER.error("Database error occurred while fetching user with the name: {}.", name, e);
            throw new DataExcessException(String.format("Database error occurred while fetching user with the name: %s.", name), e);
        } finally {
            entityManager.close();
        }
    }
}
