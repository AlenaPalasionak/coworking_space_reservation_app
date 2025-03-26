package org.example.coworking.dao;

import org.example.coworking.config.DataSourceConfig;
import org.example.coworking.dao.exception.DaoErrorCode;
import org.example.coworking.dao.exception.DataExcessException;
import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.dao.exception.ObjectFieldNotFoundException;
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

        String insertReservationQuery = "INSERT INTO public.reservations " +
                "(customer_id, coworking_space_id) " +
                "VALUES (?, ?) RETURNING id";

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement insertReservationStatement = connection.prepareStatement(
                    insertReservationQuery,
                    Statement.RETURN_GENERATED_KEYS)) {

                insertReservationStatement.setLong(1, customerId);
                insertReservationStatement.setLong(2, coworkingId);
                insertReservationStatement.executeUpdate();

                try (ResultSet generatedKeys = insertReservationStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        Long reservationId = generatedKeys.getLong(1);
                        reservation.setId(reservationId);

                        addPeriodToReservation(reservation, connection);
                    } else {
                        throw new DataExcessException("Failed to get generated reservation ID for reservation: " + reservation);
                    }
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                TECHNICAL_LOGGER.error(e.getMessage());
                throw new DataExcessException("Database error occurred while adding a new reservation: " + reservation);
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Database error occurred while adding a new reservation: " + reservation);
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
                throw new EntityNotFoundException("Failure to find reservation with ID " + reservation.getId()
                        , DaoErrorCode.RESERVATION_IS_NOT_FOUND);
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Database error occurred while adding reservation: " + reservation);
        }
    }

    @Override
    public Reservation getById(Long reservationId, Connection connection) throws EntityNotFoundException {
        String selectReservationQuery = "SELECT id, customer_id, coworking_space_id, period_id " +
                "FROM public.reservations  " +
                "WHERE id = ?";

        try (PreparedStatement selectReservationStatement = connection.prepareStatement(selectReservationQuery)) {
            selectReservationStatement.setLong(1, reservationId);

            try (ResultSet reservationResultSet = selectReservationStatement.executeQuery()) {
                if (!reservationResultSet.next()) {
                    throw new EntityNotFoundException("Failure to find reservation with ID " + reservationId
                            , DaoErrorCode.RESERVATION_IS_NOT_FOUND);
                }

                Long customerId = reservationResultSet.getLong("customer_id");
                Long coworkingId = reservationResultSet.getLong("coworking_space_id");
                Long periodId = reservationResultSet.getLong("period_id");

                User customer = userDaoFromDb.getById(customerId, connection);
                CoworkingSpace coworkingSpace = coworkingDaoFromDb.getById(coworkingId, connection);
                ReservationPeriod period = getReservationPeriodById(periodId, connection);

                return new Reservation(reservationId, customer, period, coworkingSpace);
            } catch (EntityNotFoundException e) {
                throw new EntityNotFoundException("Failure to find Reservation with ID " + reservationId
                        , DaoErrorCode.RESERVATION_IS_NOT_FOUND);
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Database error occurred while fetching reservation by  id : " + reservationId);
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
                    throw new EntityNotFoundException("Failure to find reservation with ID " + reservationId
                            , DaoErrorCode.RESERVATION_IS_NOT_FOUND);
                }

                Long customerId = reservationResultSet.getLong("customer_id");
                Long coworkingId = reservationResultSet.getLong("coworking_space_id");
                Long periodId = reservationResultSet.getLong("period_id");

                User customer = userDaoFromDb.getById(customerId, connection);
                CoworkingSpace coworkingSpace = coworkingDaoFromDb.getById(coworkingId, connection);
                ReservationPeriod period = getReservationPeriodById(periodId, connection);

                return new Reservation(reservationId, customer, period, coworkingSpace);
            } catch (EntityNotFoundException e) {
                throw new EntityNotFoundException("Failure to find Reservation with ID " + reservationId
                        , DaoErrorCode.RESERVATION_IS_NOT_FOUND);
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Database error occurred while fetching reservation by id : " + reservationId);
        }
    }

    @Override
    public List<Reservation> getAll() {
        List<Reservation> reservations = new ArrayList<>();
        String selectReservationsQuery = "SELECT id, customer_id, coworking_space_id, period_id " +
                "FROM public.reservations ";
        Long coworkingSpaceId = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectReservationsStatement = connection.prepareStatement(selectReservationsQuery);
             ResultSet reservationsResultSet = selectReservationsStatement.executeQuery()) {

            while (reservationsResultSet.next()) {
                Long reservationId = reservationsResultSet.getLong(1);
                Long customerId = reservationsResultSet.getLong("customer_id");
                coworkingSpaceId = reservationsResultSet.getLong("coworking_space_id");
                Long periodId = reservationsResultSet.getLong("period_id");

                User customer = userDaoFromDb.getById(customerId, connection);
                CoworkingSpace coworkingSpace = coworkingDaoFromDb.getById(coworkingSpaceId, connection);
                ReservationPeriod reservationPeriod = getReservationPeriodById(periodId, connection);

                Reservation reservation = new Reservation(reservationId, customer, reservationPeriod, coworkingSpace);
                reservations.add(reservation);
            }
        } catch (EntityNotFoundException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new ObjectFieldNotFoundException("Failure to find coworkingSpace with ID " + coworkingSpaceId);
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Database error occurred while fetching all reservations.");
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
            throw new DataExcessException("Database error occurred while fetching a reservation period.");
        }
        return reservationPeriod;
    }

    private void addPeriodToReservation(Reservation reservation, Connection connection) {
        String insertPeriodQuery = "INSERT INTO public.reservation_periods " +
                "(reservation_id, coworking_space_id, start_time, end_time) " +
                "VALUES (?, ?, ?, ?) RETURNING id";

        try (PreparedStatement insertPeriodStatement = connection.prepareStatement(insertPeriodQuery)) {
            ReservationPeriod period = reservation.getPeriod();

            insertPeriodStatement.setLong(1, reservation.getId());
            insertPeriodStatement.setLong(2, reservation.getCoworkingSpace().getId());
            insertPeriodStatement.setTimestamp(3, Timestamp.valueOf(period.getStartTime()));
            insertPeriodStatement.setTimestamp(4, Timestamp.valueOf(period.getEndTime()));

            try (ResultSet resultSet = insertPeriodStatement.executeQuery()) {
                if (resultSet.next()) {
                    period.setId(resultSet.getLong("id"));
                    updateReservationWithPeriodId(reservation.getId(), period.getId(), connection);
                }
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Database error occurred while adding reservation period: " + reservation.getPeriod());
        }
    }

    private void updateReservationWithPeriodId(Long reservationId, Long periodId, Connection connection) {
        String updateQuery = "UPDATE public.reservations SET period_id = ? WHERE id = ?";

        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setLong(1, periodId);
            updateStatement.setLong(2, reservationId);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Database error occurred while updating reservation with period ID: " + periodId);
        }
    }
}
