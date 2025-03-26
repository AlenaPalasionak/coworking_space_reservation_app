package org.example.coworking.dao;

import org.example.coworking.config.DataSourceConfig;
import org.example.coworking.dao.exception.DaoErrorCode;
import org.example.coworking.dao.exception.DataExcessException;
import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.Reservation;
import org.example.coworking.model.ReservationPeriod;
import org.example.coworking.model.User;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;

public class ReservationDaoFromDbImpl implements ReservationDao {
    private final DataSource dataSource;
    private final UserDao userDaoFromDb;
    private final CoworkingDao coworkingDaoFromDb;

    public ReservationDaoFromDbImpl(UserDao userDaoFromDb, CoworkingDao coworkingDaoFromDb) {
        this.userDaoFromDb = userDaoFromDb;
        this.coworkingDaoFromDb = coworkingDaoFromDb;
        this.dataSource = DataSourceConfig.getDataSource();
    }

    @Override
    public void add(Reservation reservation) {
        Long customerId = reservation.getCustomer().getId();
        Long coworkingId = reservation.getCoworkingSpace().getId();
        addPeriodToCoworking(reservation.getPeriod(), reservation.getCoworkingSpace());
        Long periodId = reservation.getPeriod().getId();

        String insertReservationQuery = "INSERT INTO public.reservations" +
                "(customer_id, coworking_space_id, period_id) " +
                "VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertReservationStatement = connection.prepareStatement(insertReservationQuery)) {
            insertReservationStatement.setLong(1, customerId);
            insertReservationStatement.setLong(2, coworkingId);
            insertReservationStatement.setLong(3, periodId);

            insertReservationStatement.executeUpdate();
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Failure to establish connection when adding a new reservation: " + reservation);
        }
    }

    @Override
    public void addPeriodToCoworking(ReservationPeriod period, CoworkingSpace coworkingSpace) {
        String insertPeriodQuery = "INSERT INTO public.reservation_periods (coworking_space_id, start_time, end_time) " +
                "VALUES (?, ?, ?) RETURNING id";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertPeriodStatement = connection.prepareStatement(insertPeriodQuery)) {

            insertPeriodStatement.setLong(1, coworkingSpace.getId());
            insertPeriodStatement.setTimestamp(2, Timestamp.valueOf(period.getStartTime()));
            insertPeriodStatement.setTimestamp(3, Timestamp.valueOf(period.getEndTime()));

            try (ResultSet periodIdResultSet = insertPeriodStatement.executeQuery()) {
                if (periodIdResultSet.next()) {
                    period.setId(periodIdResultSet.getLong("id"));
                }
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Failure to establish connection when adding a new reservation period." + period);
        }
    }


    @Override
    public void delete(Reservation reservation) throws EntityNotFoundException {
        String deleteReservationQuery = "DELETE FROM public.reservations WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement deleteReservationStatement = connection.prepareStatement(deleteReservationQuery)) {

            deleteReservationStatement.setLong(1, reservation.getId());
            int rowsAffected = deleteReservationStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new EntityNotFoundException("Reservation with ID " + reservation.getId() + " not found"
                        , DaoErrorCode.RESERVATION_IS_NOT_FOUND);
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Failure to establish connection when adding reservation: " + reservation);
        }
    }


    @Override
    public Reservation getById(Long reservationId) throws EntityNotFoundException {
        String selectReservationQuery = "SELECT id, customer_id, coworking_space_id, period_id " +
                "FROM public.reservations  " +
                "WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectReservationStatement = connection.prepareStatement(selectReservationQuery)) {

            selectReservationStatement.setLong(1, reservationId);

            try (ResultSet reservationResultSet = selectReservationStatement.executeQuery()) {
                if (!reservationResultSet.next()) {
                    throw new EntityNotFoundException("Reservation with ID " + reservationId + " not found",
                            DaoErrorCode.RESERVATION_IS_NOT_FOUND);
                }

                Long customerId = reservationResultSet.getLong("customer_id");
                Long coworkingId = reservationResultSet.getLong("coworking_space_id");
                Long periodId = reservationResultSet.getLong("period_id");

                User customer = userDaoFromDb.getById(customerId);
                CoworkingSpace coworkingSpace = coworkingDaoFromDb.getById(coworkingId);
                ReservationPeriod period = getReservationPeriodById(periodId, connection);

                return new Reservation(reservationId, customer, period, coworkingSpace);
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Failure to establish connection when getting reservation by  id : " + reservationId);
        }
    }

    @Override
    public List<Reservation> getAll() {
        List<Reservation> reservations = new ArrayList<>();
        String selectReservationsQuery = "SELECT id, customer_id, coworking_space_id, period_id " +
                "FROM public.reservations ";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectReservationsStatement = connection.prepareStatement(selectReservationsQuery);
             ResultSet reservationsResultSet = selectReservationsStatement.executeQuery()) {

            while (reservationsResultSet.next()) {
                Long reservationId = reservationsResultSet.getLong("id");
                Long customerId = reservationsResultSet.getLong("customer_id");
                Long coworkingSpaceId = reservationsResultSet.getLong("coworking_space_id");
                Long periodId = reservationsResultSet.getLong("period_id");

                User customer = userDaoFromDb.getById(customerId, connection);
                CoworkingSpace coworkingSpace = coworkingDaoFromDb.getById(coworkingSpaceId);
                ReservationPeriod reservationPeriod = getReservationPeriodById(periodId, connection);

                Reservation reservation = new Reservation(reservationId, customer, reservationPeriod, coworkingSpace);
                reservations.add(reservation);
            }
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Failure to establish connection when getting all reservations.");
        }
        return reservations;
    }

    private ReservationPeriod getReservationPeriodById(Long periodId, Connection connection) {
        ReservationPeriod reservationPeriod = null;
        String selectPeriodQuery = "SELECT id, coworking_space_id, start_time, end_time " +
                "FROM public.reservation_periods " +
                "WHERE id = ?";

        try (PreparedStatement selectPeriodStatement = connection.prepareStatement(selectPeriodQuery)) {

            selectPeriodStatement.setLong(1, periodId);
            ResultSet periodsResultSet = selectPeriodStatement.executeQuery();

            if (periodsResultSet.next()) {
                LocalDateTime startTime = periodsResultSet.getTimestamp("start_time").toLocalDateTime();
                LocalDateTime endTime = periodsResultSet.getTimestamp("end_time").toLocalDateTime();

                reservationPeriod = new ReservationPeriod(periodId, startTime, endTime);
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Failure to establish connection when getting a reservation period.");
        }
        return reservationPeriod;
    }
}
