package org.example.coworking.dao;

import org.example.coworking.config.JdbcConfig;
import org.example.coworking.dao.exception.DaoErrorCode;
import org.example.coworking.dao.exception.DataExcessException;
import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.*;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;

/**
 * Class provides functionality for interaction with database and processing Reservation object
 */
public class JdbcReservationDao implements ReservationDao {
    private final DataSource dataSource;

    public JdbcReservationDao() {
        this.dataSource = JdbcConfig.getDataSource();
    }

    @Override
    public void create(Reservation reservation) {
        Long customerId = reservation.getCustomer().getId();
        Long coworkingId = reservation.getCoworkingSpace().getId();
        LocalDateTime startTime = reservation.getPeriod().getStartTime();
        LocalDateTime endTime = reservation.getPeriod().getEndTime();

        String insertReservationQuery = """
                INSERT INTO public.reservations (customer_id, coworking_space_id, start_time, end_time)
                VALUES (?, ?, ?, ?) RETURNING id""";

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement insertReservationStatement = connection.prepareStatement(insertReservationQuery
                    , Statement.RETURN_GENERATED_KEYS)) {

                insertReservationStatement.setLong(1, customerId);
                insertReservationStatement.setLong(2, coworkingId);
                insertReservationStatement.setTimestamp(3, Timestamp.valueOf(startTime));
                insertReservationStatement.setTimestamp(4, Timestamp.valueOf(endTime));

                insertReservationStatement.executeUpdate();

                try (ResultSet generatedKeys = insertReservationStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        Long reservationId = generatedKeys.getLong(1);
                        reservation.setId(reservationId);
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                TECHNICAL_LOGGER.error("Database error occurred while creating reservation: {}", reservation, e);
                throw new DataExcessException(String.format("Database error occurred while creating reservation: %s", reservation), e);
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error("Failure to establish connection while creating a new reservation: {}.", reservation, e);
            throw new DataExcessException(String.format("Failure to establish connection while creating a new reservation: %s", reservation), e);
        }
    }

    @Override
    public void delete(Long reservationId) throws EntityNotFoundException {
        String deleteReservationQuery = """
                DELETE FROM public.reservations
                WHERE ID = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement deleteReservationStatement = connection.prepareStatement(deleteReservationQuery)) {

            deleteReservationStatement.setLong(1,reservationId);
            int rowsAffected = deleteReservationStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new EntityNotFoundException(String.format("Failure to delete Reservation with ID: %d. Reservation is not found."
                        , reservationId), DaoErrorCode.RESERVATION_IS_NOT_FOUND);
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error("Database error occurred while deleting reservation with ID: {}.", reservationId, e);
            throw new DataExcessException(String.format("Database error occurred while deleting reservation with ID: %s.", reservationId), e);
        }
    }

    @Override
    public Reservation getById(Long reservationId) throws EntityNotFoundException {
        String selectReservationQuery = """
                SELECT id, customer_id, coworking_space_id, start_time, end_time
                FROM public.reservations
                WHERE id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectReservationStatement = connection.prepareStatement(selectReservationQuery)) {
            selectReservationStatement.setLong(1, reservationId);

            try (ResultSet reservationResultSet = selectReservationStatement.executeQuery()) {
                if (!reservationResultSet.next()) {
                    throw new EntityNotFoundException(String.format("Failure to get Reservation by ID: %d."
                            , reservationId), DaoErrorCode.RESERVATION_IS_NOT_FOUND);
                }

                User customer = new Customer();
                CoworkingSpace coworkingSpace = new CoworkingSpace();
                ReservationPeriod reservationPeriod;

                Long customerId = reservationResultSet.getLong("customer_id");
                Long coworkingId = reservationResultSet.getLong("coworking_space_id");
                LocalDateTime startTime = reservationResultSet.getTimestamp("start_time").toLocalDateTime();
                LocalDateTime endTime = reservationResultSet.getTimestamp("end_time").toLocalDateTime();

                customer.setId(customerId);
                reservationPeriod = new ReservationPeriod(startTime, endTime);
                coworkingSpace.setId(coworkingId);

                return new Reservation(reservationId, customer, reservationPeriod, coworkingSpace);
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error("Database error occurred while getting reservation by ID: {}.", reservationId, e);
            throw new DataExcessException(String.format("Database error occurred while getting reservation by ID: %d.", reservationId), e);
        }
    }

    @Override
    public List<Reservation> getAll() {
        List<Reservation> reservations = new ArrayList<>();
        String selectReservationsQuery = """
                SELECT id, customer_id, coworking_space_id, start_time, end_time
                FROM public.reservations
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectReservationsStatement = connection.prepareStatement(selectReservationsQuery);
             ResultSet reservationsResultSet = selectReservationsStatement.executeQuery()) {

            while (reservationsResultSet.next()) {
                User customer = new Customer();
                CoworkingSpace coworkingSpace = new CoworkingSpace();
                User admin = new Admin();
                ReservationPeriod reservationPeriod;

                Long reservationId = reservationsResultSet.getLong("id");
                Long customerId = reservationsResultSet.getLong("customer_id");
                Long coworkingSpaceId = reservationsResultSet.getLong("coworking_space_id");
                LocalDateTime startTime = reservationsResultSet.getTimestamp("start_time").toLocalDateTime();
                LocalDateTime endTime = reservationsResultSet.getTimestamp("end_time").toLocalDateTime();

                coworkingSpace.setAdmin(admin);
                customer.setId(customerId);
                coworkingSpace.setId(coworkingSpaceId);
                reservationPeriod = new ReservationPeriod(startTime, endTime);
                Reservation reservation = new Reservation(reservationId, customer, reservationPeriod, coworkingSpace);
                reservations.add(reservation);
            }

        } catch (SQLException e) {
            TECHNICAL_LOGGER.error("Database error occurred while getting all reservations.", e);
            throw new DataExcessException("Database error occurred while getting all reservations.", e);
        }
        return reservations;
    }


    @Override
    public Set<ReservationPeriod> getAllReservationPeriodsByCoworking(Long coworkingSpaceId) {
        Set<ReservationPeriod> reservations = new TreeSet<>();
        String selectReservationsQuery = """
                SELECT start_time, end_time
                FROM public.reservations
                WHERE coworking_space_id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectReservationStatement = connection.prepareStatement(selectReservationsQuery)) {
            selectReservationStatement.setLong(1, coworkingSpaceId);

            ResultSet reservationResultSet = selectReservationStatement.executeQuery();
            while (reservationResultSet.next()) {
                LocalDateTime startTime = reservationResultSet.getTimestamp("start_time").toLocalDateTime();
                LocalDateTime endTime = reservationResultSet.getTimestamp("end_time").toLocalDateTime();

                ReservationPeriod reservationPeriod = new ReservationPeriod(startTime, endTime);
                reservations.add(reservationPeriod);
            }
            return reservations;
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error("Database error occurred while getting reservation periods by coworking ID: {}."
                    , coworkingSpaceId, e);
            throw new DataExcessException(String.format("Database error occurred while getting reservation periods by coworking ID: %d."
                    , coworkingSpaceId), e);
        }
    }

    @Override
    public List<Reservation> getAllReservationsByCustomer(Long customerId) {
        List<Reservation> reservations = new ArrayList<>();
        String selectReservationsQuery = """
                SELECT id, coworking_space_id, start_time, end_time
                FROM public.reservations
                WHERE customer_id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectReservationStatement = connection.prepareStatement(selectReservationsQuery)) {
            selectReservationStatement.setLong(1, customerId);

            ResultSet reservationResultSet = selectReservationStatement.executeQuery();
            while (reservationResultSet.next()) {
                User customer = new Customer();
                CoworkingSpace coworkingSpace = new CoworkingSpace();
                ReservationPeriod reservationPeriod;

                Long reservationId = reservationResultSet.getLong("id");
                Long coworkingSpaceId = reservationResultSet.getLong("coworking_space_id");
                LocalDateTime startTime = reservationResultSet.getTimestamp("start_time").toLocalDateTime();
                LocalDateTime endTime = reservationResultSet.getTimestamp("end_time").toLocalDateTime();

                customer.setId(customerId);
                coworkingSpace.setId(coworkingSpaceId);
                reservationPeriod = new ReservationPeriod(startTime, endTime);
                reservations.add(new Reservation(reservationId, customer, reservationPeriod, coworkingSpace));
            }
            return reservations;
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error("Database error occurred while getting reservations by customer ID: {}."
                    , customerId, e);
            throw new DataExcessException(String.format("Database error occurred while getting reservations by customer ID: %d."
                    , customerId), e);
        }
    }

    @Override
    public List<Reservation> getAllReservationsByAdmin(Long adminId) {
        List<Reservation> reservations = new ArrayList<>();
        String selectReservationsQuery = """
                SELECT r.id, r.customer_id, r.coworking_space_id, r.start_time, r.end_time
                FROM public.reservations r
                LEFT JOIN public.coworking_spaces cs ON r.coworking_space_id=cs.id
                WHERE admin_id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectReservationStatement = connection.prepareStatement(selectReservationsQuery)) {
            selectReservationStatement.setLong(1, adminId);

            ResultSet reservationResultSet = selectReservationStatement.executeQuery();
            while (reservationResultSet.next()) {
                User admin = new Admin();
                CoworkingSpace coworkingSpace = new CoworkingSpace();
                ReservationPeriod reservationPeriod;
                User customer = new Customer();

                Long reservationId = reservationResultSet.getLong("id");
                Long coworkingSpaceId = reservationResultSet.getLong("coworking_space_id");
                Long customerId = reservationResultSet.getLong("customer_id");
                LocalDateTime startTime = reservationResultSet.getTimestamp("start_time").toLocalDateTime();
                LocalDateTime endTime = reservationResultSet.getTimestamp("end_time").toLocalDateTime();

                admin.setId(adminId);
                coworkingSpace.setId(coworkingSpaceId);
                customer.setId(customerId);
                reservationPeriod = new ReservationPeriod(startTime, endTime);
                reservations.add(new Reservation(reservationId, admin, reservationPeriod, coworkingSpace));
            }
            return reservations;
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error("Database error occurred while getting reservations reservations by admin ID: {}."
                    , adminId, e);
            throw new DataExcessException(String.format("Database error occurred while getting reservations by admin id: %d ",
                    adminId), e);
        }
    }

    @Override
    public Long getCustomerIdByReservationId(Long reservationId) throws EntityNotFoundException {
        Long customerId;
        String selectCustomerIdQuery = """
                SELECT customer_id
                FROM public.reservations
                WHERE id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectCustomerIdStatement = connection.prepareStatement(selectCustomerIdQuery)) {
            selectCustomerIdStatement.setLong(1, reservationId);

            try (ResultSet customerIdResultSet = selectCustomerIdStatement.executeQuery()) {
                if (customerIdResultSet.next()) {
                    customerId = customerIdResultSet.getLong("customer_id");
                } else {
                    throw new EntityNotFoundException(String.format("Failure to find Customer id by reservation ID: %d"
                            , reservationId), DaoErrorCode.USER_IS_NOT_FOUND);
                }
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error("Database error occurred while getting Customer ID by Reservation ID: %d: {}", reservationId, e);
            throw new DataExcessException(String.format("Database error occurred while getting Customer ID by Reservation ID: %d ", reservationId), e);
        }
        return customerId;
    }
}
