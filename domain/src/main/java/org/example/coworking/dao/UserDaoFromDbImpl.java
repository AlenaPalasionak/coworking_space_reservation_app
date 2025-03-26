package org.example.coworking.dao;

import org.example.coworking.config.DataSourceConfig;
import org.example.coworking.dao.exception.DaoErrorCode;
import org.example.coworking.dao.exception.DataExcessException;
import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.dao.exception.ObjectFieldNotFoundException;
import org.example.coworking.model.Admin;
import org.example.coworking.model.Customer;
import org.example.coworking.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;

public class UserDaoFromDbImpl implements UserDao {
    private final DataSource dataSource;

    public UserDaoFromDbImpl() {
        this.dataSource = DataSourceConfig.getDataSource();
    }

    @Override
    public User getById(Long id, Connection connection) {
        User user = null;
        String selectUserQuery = "SELECT name, password, role " +
                "FROM public.users  " +
                "WHERE id = ?";

        try (PreparedStatement selectUserStatement = connection.prepareStatement(selectUserQuery)) {
            selectUserStatement.setLong(1, id);
            try (ResultSet UserResultSet = selectUserStatement.executeQuery()) {
                if (!UserResultSet.next()) {
                    throw new ObjectFieldNotFoundException("Failure to find user with id " + id);
                }
                String userName = UserResultSet.getString(1);
                String password = UserResultSet.getString(2);
                String role = UserResultSet.getString(3);
                switch (role) {
                    case "ADMIN" -> user = new Admin(id, userName, password);
                    case "CUSTOMER" -> user = new Customer(id, userName, password);
                }
            }
            return user;
        } catch (SQLException e) {
            throw new DataExcessException("Database error occurred while fetching user by id: " + id);
        }
    }

    @Override
    public User getUserByNamePasswordAndRole(String name, String password, Class<? extends User> roleClass) throws EntityNotFoundException {
        String role = roleClass == Admin.class ? "ADMIN" : "CUSTOMER";
        String selectUserQuery = "SELECT id, name, password, role " +
                "FROM users " +
                "WHERE name = ? AND password = crypt(?, password) AND role = CAST(? AS user_role)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectUserStatement = connection.prepareStatement(selectUserQuery)) {

            selectUserStatement.setString(1, name);
            selectUserStatement.setString(2, password);
            selectUserStatement.setString(3, role);

            try (ResultSet selectUserResultSet = selectUserStatement.executeQuery()) {
                if (!selectUserResultSet.next()) {
                    throw new EntityNotFoundException("Failure to find user with the name: " + name, DaoErrorCode.USER_IS_NOT_FOUND);
                } else {
                    return role.equals("ADMIN") ?
                            new Admin(selectUserResultSet.getLong("id"), selectUserResultSet.getString("name")
                                    , selectUserResultSet.getString("password")) :
                            new Customer(selectUserResultSet.getLong("id"), selectUserResultSet.getString("name")
                                    , selectUserResultSet.getString("password"));
                }
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Database error occurred while fetching user with the name: " + name);
        }
    }

    @Override
    public User getById(Long id) {
        throw new UnsupportedOperationException("Use getById(Long id, Connection connection)");
    }
}
