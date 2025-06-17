package org.example.coworking.repository;

import org.example.coworking.repository.exception.RepositoryErrorCode;
import org.example.coworking.repository.exception.DataExcessException;
import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.entity.Admin;
import org.example.coworking.entity.Customer;
import org.example.coworking.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;

/**
 * Implementation of {@link UserRepository} that interacts with the database
 * to manage user retrieval operations.
 */
@Repository("jdbcUserRepository")
public class JdbcUserRepository implements UserRepository {
    private final DataSource dataSource;

    @Autowired
    public JdbcUserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public <T extends User> T getUserByNamePasswordAndRole(String name, String password, Class<T> role) throws EntityNotFoundException {
        String userRole = role == Admin.class ? "ADMIN" : "CUSTOMER";
        String selectUserQuery = "SELECT id, name, password, role " +
                "FROM users " +
                "WHERE name = ? AND password = crypt(?, password) AND role = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectUserStatement = connection.prepareStatement(selectUserQuery)) {

            selectUserStatement.setString(1, name);
            selectUserStatement.setString(2, password);
            selectUserStatement.setString(3, userRole);

            try (ResultSet selectUserResultSet = selectUserStatement.executeQuery()) {
                if (!selectUserResultSet.next()) {
                    throw new EntityNotFoundException("Failure to find user with the name: " + name, RepositoryErrorCode.USER_IS_NOT_FOUND);
                }

                Long id = selectUserResultSet.getLong("id");
                String userName = selectUserResultSet.getString("name");
                String userPassword = selectUserResultSet.getString("password");
                User user = userRole.equals("ADMIN")
                        ? new Admin(id, userName, userPassword)
                        : new Customer(id, userName, userPassword);

                return role.cast(user);
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error("Database error occurred while fetching user with the name: {}.", name, e);
            throw new DataExcessException(String.format("Database error occurred while fetching user with the name: %s.", name), e);
        }
    }

    @Override
    public User getUserById(Long id) throws EntityNotFoundException {
        String selectUserQuery = """
                SELECT
                FROM users
                WHERE id = ?
                """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectUserStatement = connection.prepareStatement(selectUserQuery)) {

            selectUserStatement.setLong(1, id);

            try (ResultSet selectUserResultSet = selectUserStatement.executeQuery()) {
                if (!selectUserResultSet.next()) {
                    throw new EntityNotFoundException(String.format("Failure to find user with the ID %d: ",
                            id),
                            RepositoryErrorCode.USER_IS_NOT_FOUND);
                }

                User user = new User();
                user.setId(id);
                return user;
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error("Database error occurred while fetching user with the ID: {}.", id, e);
            throw new DataExcessException(String.format("Database error occurred while fetching user with the ID %d: ", id), e);
        }
    }
}