package org.example.coworking.dao;

import org.example.coworking.config.DataSourceConfig;
import org.example.coworking.dao.exception.*;
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
 * Class provides functionality for interaction with database and processing Coworking object
 */
public class CoworkingDaoFromDbImpl implements CoworkingDao {

    private final DataSource dataSource;
    private final UserDao userDaoFromDb;

    public CoworkingDaoFromDbImpl(UserDao userDaoFromDb) {
        this.userDaoFromDb = userDaoFromDb;
        this.dataSource = DataSourceConfig.getDataSource();
    }

    @Override
    public void add(CoworkingSpace coworkingSpace) {
        String insertCoworkingSpaceQuery = "INSERT INTO public.coworking_spaces (admin_id, price, type_id) VALUES (?, ?, ?) RETURNING id";
        String insertFacilityLinkQuery = "INSERT INTO public.coworking_space_facilities (coworking_space_id, facility_id) VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement insertCoworkingSpaceStatement = connection.prepareStatement(insertCoworkingSpaceQuery, Statement.RETURN_GENERATED_KEYS)) {
                insertCoworkingSpaceStatement.setLong(1, coworkingSpace.getAdmin().getId());
                insertCoworkingSpaceStatement.setDouble(2, coworkingSpace.getPrice());
                CoworkingType coworkingType = coworkingSpace.getCoworkingType();
                Long coworkingTypeId = getCoworkingTypeId(coworkingType, connection);
                insertCoworkingSpaceStatement.setLong(3, coworkingTypeId);
                insertCoworkingSpaceStatement.executeUpdate();

                Long coworkingSpaceId = null;
                try (ResultSet coworkingIdResultSet = insertCoworkingSpaceStatement.getGeneratedKeys()) {
                    if (coworkingIdResultSet.next()) {
                        coworkingSpaceId = coworkingIdResultSet.getLong(1);
                    }
                }

                try (PreparedStatement insertFacilityLinkStatement = connection.prepareStatement(insertFacilityLinkQuery)) {
                    for (Facility facility : coworkingSpace.getFacilities()) {
                        insertFacilityLinkStatement.setLong(1, coworkingSpaceId);
                        Long facilityId = getFacilityId(facility, connection);
                        insertFacilityLinkStatement.setLong(2, facilityId);
                        insertFacilityLinkStatement.addBatch();
                    }
                    insertFacilityLinkStatement.executeBatch();
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                TECHNICAL_LOGGER.error(e.getMessage());
                throw new DataExcessException("Failure to add coworking:  " + coworkingSpace);
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Database error occurred while adding coworking.");
        }
    }

    @Override
    public void delete(CoworkingSpace coworkingSpace) throws EntityNotFoundException {
        String deleteCoworkingQuery = "DELETE FROM public.coworking_spaces WHERE ID = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement deleteCoworkingStatement = connection.prepareStatement(deleteCoworkingQuery)) {
            deleteCoworkingStatement.setLong(1, coworkingSpace.getId());
            int rowsAffected = deleteCoworkingStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new EntityNotFoundException("Failure to find coworking with ID " + coworkingSpace.getId()
                        , DaoErrorCode.COWORKING_IS_NOT_FOUND);
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Database error occurred while deleting coworking space: " + coworkingSpace);
        }
    }

    @Override
    public CoworkingSpace getById(Long coworkingId, Connection connection) throws EntityNotFoundException {
        String selectCoworkingSpaceQuery = "SELECT cs.id, cs.admin_id, cs.price, ct.description " +
                "FROM public.coworking_spaces cs " +
                "JOIN public.coworking_types ct ON cs.type_id = ct.id " +
                "WHERE cs.id = ?";

        try (PreparedStatement selectCoworkingStatement = connection.prepareStatement(selectCoworkingSpaceQuery)) {
            selectCoworkingStatement.setLong(1, coworkingId);

            try (ResultSet coworkingResultSet = selectCoworkingStatement.executeQuery()) {
                if (!coworkingResultSet.next()) {
                    throw new EntityNotFoundException("Failure to find coworking space with id: " + coworkingId,
                            DaoErrorCode.COWORKING_IS_NOT_FOUND);
                }

                Long adminId = coworkingResultSet.getLong("admin_id");
                double price = coworkingResultSet.getDouble("price");
                String typeDescription = coworkingResultSet.getString("description");

                CoworkingType coworkingType = CoworkingType.valueOf(
                        typeDescription.replace(" ", "_").toUpperCase());

                User admin = userDaoFromDb.getById(adminId, connection);
                List<Facility> facilities = getFacilitiesForCoworkingSpace(coworkingId, connection);
                Set<ReservationPeriod> reservationPeriods = getReservationPeriodsForCoworkingSpace(coworkingId, connection);

                return new CoworkingSpace(coworkingId, admin, price, coworkingType, facilities, reservationPeriods);
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Database error occurred while fetching coworking space by id: "
                    + coworkingId);
        }
    }

    @Override
    public CoworkingSpace getById(Long coworkingId) throws EntityNotFoundException {
        String selectCoworkingSpaceQuery = "SELECT cs.id, cs.admin_id, cs.price, ct.description " +
                "FROM public.coworking_spaces cs " +
                "JOIN public.coworking_types ct ON cs.type_id = ct.id " +
                "WHERE cs.id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectCoworkingStatement = connection.prepareStatement(selectCoworkingSpaceQuery)) {
            selectCoworkingStatement.setLong(1, coworkingId);

            try (ResultSet coworkingResultSet = selectCoworkingStatement.executeQuery()) {
                if (!coworkingResultSet.next()) {
                    throw new EntityNotFoundException("Failure to find coworking space with id: " + coworkingId,
                            DaoErrorCode.COWORKING_IS_NOT_FOUND);
                }

                Long adminId = coworkingResultSet.getLong("admin_id");
                double price = coworkingResultSet.getDouble("price");
                String typeDescription = coworkingResultSet.getString("description");

                CoworkingType coworkingType = CoworkingType.valueOf(
                        typeDescription.replace(" ", "_").toUpperCase());

                User admin = userDaoFromDb.getById(adminId, connection);
                List<Facility> facilities = getFacilitiesForCoworkingSpace(coworkingId, connection);
                Set<ReservationPeriod> reservationPeriods = getReservationPeriodsForCoworkingSpace(coworkingId, connection);

                return new CoworkingSpace(coworkingId, admin, price, coworkingType, facilities, reservationPeriods);
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Database error occurred while fetching coworking space by id: "
                    + coworkingId);
        }
    }

    @Override
    public List<CoworkingSpace> getAll() {
        List<CoworkingSpace> coworkingSpaces = new ArrayList<>();
        String selectCoworkingSpacesQuery = "SELECT cs.id, cs.admin_id, cs.price, ct.description " +
                "FROM public.coworking_spaces cs " +
                "JOIN public.coworking_types ct ON cs.type_id = ct.id ";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectCoworkingStatement = connection.prepareStatement(selectCoworkingSpacesQuery)) {

            ResultSet coworkingSpacesResultSet = selectCoworkingStatement.executeQuery();
            while (coworkingSpacesResultSet.next()) {

                Long coworkingId = coworkingSpacesResultSet.getLong("id");
                Long adminId = coworkingSpacesResultSet.getLong("admin_id");
                double price = coworkingSpacesResultSet.getDouble("price");
                String typeDescription = coworkingSpacesResultSet.getString("description");

                CoworkingType coworkingType = CoworkingType.valueOf(
                        typeDescription.replace(" ", "_").toUpperCase());

                User admin = userDaoFromDb.getById(adminId, connection);
                List<Facility> facilities = getFacilitiesForCoworkingSpace(coworkingId, connection);
                Set<ReservationPeriod> reservationPeriods = getReservationPeriodsForCoworkingSpace(coworkingId, connection);
                coworkingSpaces.add(new CoworkingSpace(coworkingId, admin, price, coworkingType, facilities, reservationPeriods));
            }
            return coworkingSpaces;
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Database error occurred while fetching coworking spaces");
        }
    }

    /**
     * Retrieves the list of facilities associated with a given coworking space.
     *
     * @param coworkingSpaceId The ID of the coworking space.
     * @param connection       The database connection.
     * @return A list of {@link Facility} objects.
     * @throws IllegalArgumentException If coworkingSpaceId is null.
     * @throws DataExcessException      If a database error occurs.
     */
    private List<Facility> getFacilitiesForCoworkingSpace(Long coworkingSpaceId, Connection connection) {
        if (coworkingSpaceId == null) {
            TECHNICAL_LOGGER.error("CoworkingSpaceId is null");
            throw new IllegalArgumentException("CoworkingSpaceId cannot be null");
        }
        List<Facility> facilities = new ArrayList<>();
        String selectFacilitiesQuery = "SELECT f.description " +
                "FROM public.coworking_space_facilities cf " +
                "JOIN public.facilities f ON cf.facility_id = f.id " +
                "WHERE cf.coworking_space_id = ?";

        try (PreparedStatement selectFacilitiesStatement = connection.prepareStatement(selectFacilitiesQuery)) {
            selectFacilitiesStatement.setLong(1, coworkingSpaceId);
            try (ResultSet facilitiesResultSet = selectFacilitiesStatement.executeQuery()) {
                while (facilitiesResultSet.next()) {
                    String facilityDescription = facilitiesResultSet.getString("description");
                    Facility facility = Facility.valueOf(facilityDescription.toUpperCase());
                    facilities.add(facility);
                }
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Database error occurred while fetching facilities List of coworking with id: "
                    + coworkingSpaceId);
        }
        return facilities;
    }

    /**
     * Retrieves the set of reservation periods for a given coworking space.
     *
     * @param coworkingSpaceId The ID of the coworking space.
     * @param connection       The database connection.
     * @return A sorted set of {@link ReservationPeriod} objects.
     * @throws IllegalArgumentException If coworkingSpaceId is null.
     * @throws DataExcessException      If a database error occurs.
     */
    private Set<ReservationPeriod> getReservationPeriodsForCoworkingSpace(Long coworkingSpaceId, Connection connection) {
        if (coworkingSpaceId == null) {
            TECHNICAL_LOGGER.error("CoworkingSpaceId is null");
            throw new IllegalArgumentException("CoworkingSpaceId cannot be null");
        }
        Set<ReservationPeriod> reservationPeriods = new TreeSet<>();
        String selectPeriodsQuery = "SELECT rp.id, rp.start_time, rp.end_time " +
                "FROM public.reservation_periods rp " +
                "WHERE rp.coworking_space_id = ?";

        try (PreparedStatement selectPeriodsStatement = connection.prepareStatement(selectPeriodsQuery)) {
            selectPeriodsStatement.setLong(1, coworkingSpaceId);
            try (ResultSet periodsResultSet = selectPeriodsStatement.executeQuery()) {
                while (periodsResultSet.next()) {
                    Long periodId = periodsResultSet.getLong("id");
                    LocalDateTime startTime = periodsResultSet.getTimestamp("start_time").toLocalDateTime();
                    LocalDateTime endTime = periodsResultSet.getTimestamp("end_time").toLocalDateTime();
                    reservationPeriods.add(new ReservationPeriod(periodId, startTime, endTime));
                }
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Database error occurred while fetching Reservation Periods for coworking with id: " +
                    coworkingSpaceId);
        }
        return reservationPeriods;
    }

    /**
     * Retrieves the ID of a coworking type based on its description.
     *
     * @param type       The {@link CoworkingType}.
     * @param connection The database connection.
     * @return The coworking type ID.
     * @throws IllegalArgumentException     If type or its description is null.
     * @throws ObjectFieldNotFoundException If no matching coworking type is found.
     * @throws DataExcessException          If a database error occurs.
     */
    private long getCoworkingTypeId(CoworkingType type, Connection connection) {
        if (type == null || type.getDescription() == null) {
            throw new IllegalArgumentException("CoworkingType or description cannot be null");
        }

        String query = "SELECT id FROM public.coworking_types WHERE description = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, type.getDescription());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("id");
                } else {
                    throw new ObjectFieldNotFoundException("Failure to find coworking type for description: " + type.getDescription());
                }
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error("Database error when fetching coworking type ID for description '{}': {}",
                    type.getDescription(), e.getMessage(), e);
            throw new DataExcessException("Database error occurred while fetching coworking type ID.");
        }
    }

    /**
     * Retrieves the ID of a facility based on its description.
     *
     * @param facility   The {@link Facility}.
     * @param connection The database connection.
     * @return The facility ID.
     * @throws IllegalArgumentException     If facility or its description is null.
     * @throws ObjectFieldNotFoundException If no matching facility is found.
     * @throws DataExcessException          If a database error occurs.
     */
    private long getFacilityId(Facility facility, Connection connection) {
        if (facility == null || facility.getDescription() == null) {
            TECHNICAL_LOGGER.error("Facility or facility description is null");
            throw new IllegalArgumentException("Facility or description cannot be null");
        }
        String selectFacilityIdQuery = "SELECT id FROM facilities WHERE description = ?";
        try (PreparedStatement selectFacilityIdStatement = connection.prepareStatement(selectFacilityIdQuery)) {
            selectFacilityIdStatement.setString(1, facility.getDescription());
            try (ResultSet facilityIdResultSet = selectFacilityIdStatement.executeQuery()) {
                if (facilityIdResultSet.next()) {
                    return facilityIdResultSet.getLong("id");
                } else {
                    TECHNICAL_LOGGER.error("Failure to find facility id for facility: " + facility.getDescription());
                    throw new ObjectFieldNotFoundException("Failure to find facility id for facility: " + facility.getDescription());
                }
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Database error occurred while getting facility id.");
        }
    }
}
