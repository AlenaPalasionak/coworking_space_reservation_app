package org.example.coworking.dao;

import org.example.coworking.config.JdbcConfig;
import org.example.coworking.dao.exception.DaoErrorCode;
import org.example.coworking.dao.exception.DataExcessException;
import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.Admin;
import org.example.coworking.model.Customer;
import org.example.coworking.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;

/**
 * Implementation of {@link UserDao} that interacts with the database
 * to manage user retrieval operations.
 */
public class JdbcUserDao implements UserDao {
    private final DataSource dataSource;

    public JdbcUserDao() {
        this.dataSource = JdbcConfig.getDataSource();
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
                    throw new EntityNotFoundException("Failure to find user with the name: " + name, DaoErrorCode.USER_IS_NOT_FOUND);
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

}