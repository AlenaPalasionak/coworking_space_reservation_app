package org.example.coworking.dao;

import org.example.coworking.config.DataSourceConfig;
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
import java.util.List;

public class UserDaoFromDbImpl implements UserDao {

    private final DataSource dataSource;

    public UserDaoFromDbImpl() {
        this.dataSource = DataSourceConfig.getDataSource();
    }

    @Override
    public void add(User object) {

    }

    @Override
    public void delete(User object) {

    }

    @Override
    public User getById(Long id) {
        return null;
    }

    @Override
    public User getById(Long id, Connection connection) {
        User user = null;
        String sqlQuery = "SELECT name, password, role " +
                "FROM public.users  " +
                "WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new EntityNotFoundException("Failure to find user with id " + id, DaoErrorCode.USER_IS_NOT_FOUND);
                }
                String userName = resultSet.getString(1);
                String password = resultSet.getString(2);
                String role = resultSet.getString(3);
                switch (role) {
                    case "ADMIN" -> user = new Admin(id, userName, password);
                    case "CUSTOMER" -> user = new Customer(id, userName, password);
                }
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }
            return user;
        } catch (SQLException e) {
            throw new DataExcessException("Failure to establish connection when getting user by id: " + id);
        }
    }

    @Override
    public User getUserByNamePasswordAndRole(String name, String password, Class<? extends User> roleClass) throws EntityNotFoundException {
        String role = roleClass == Admin.class ? "ADMIN" : "CUSTOMER";
        String sqlQuery = "SELECT id, name, password, role " +
                "FROM users " +
                "WHERE name = ? AND password = crypt(?, password) AND role = CAST(? AS user_role)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, role);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (!rs.next()) {
                    throw new EntityNotFoundException("User with the name  " + name + "is not found", DaoErrorCode.USER_IS_NOT_FOUND);
                } else {
                    return role.equals("ADMIN") ?
                            new Admin(rs.getLong("id"), rs.getString("name")
                                    , rs.getString("password")) :
                            new Customer(rs.getLong("id"), rs.getString("name")
                                    , rs.getString("password"));
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAll() {
        return null;
    }
}
