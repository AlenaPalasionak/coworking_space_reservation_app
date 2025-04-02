package org.example.coworking.dao;

import org.example.coworking.config.DataSourceConfig;
import org.example.coworking.dao.exception.DaoErrorCode;
import org.example.coworking.dao.exception.DataExcessException;
import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.dao.exception.ObjectFieldNotFoundException;
import org.example.coworking.model.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;

/**
 * Class provides functionality for interaction with database and processing Coworking object
 */
public class JdbcCoworkingDao implements CoworkingDao {
    private final DataSource dataSource;

    public JdbcCoworkingDao() {
        this.dataSource = DataSourceConfig.getDataSource();
    }

    @Override
    public void create(CoworkingSpace coworkingSpace) {

        String insertCoworkingSpaceQuery = """
                INSERT INTO public.coworking_spaces (admin_id, price, type)
                VALUES (?, ?, ?) RETURNING id
                """;
        String insertFacilityLinkQuery = """
                INSERT INTO public.coworking_space_facilities (coworking_space_id, facility_id)
                VALUES (?, ?)
                """;

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            Long coworkingSpaceId;
            try (PreparedStatement insertCoworkingSpaceStatement = connection.prepareStatement(insertCoworkingSpaceQuery
                    , Statement.RETURN_GENERATED_KEYS)) {

                Long adminId = coworkingSpace.getAdmin().getId();
                Double price = coworkingSpace.getPrice();
                CoworkingType coworkingType = coworkingSpace.getCoworkingType();

                insertCoworkingSpaceStatement.setLong(1, adminId);
                insertCoworkingSpaceStatement.setDouble(2, price);
                insertCoworkingSpaceStatement.setString(3, coworkingType.getDescription());

                insertCoworkingSpaceStatement.executeUpdate();

                try (ResultSet coworkingIdResultSet = insertCoworkingSpaceStatement.getGeneratedKeys()) {
                    if (coworkingIdResultSet.next()) {
                        coworkingSpaceId = coworkingIdResultSet.getLong(1);
                    } else {
                        throw new DataExcessException("Failure to create Coworking space, no ID obtained.");
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
                TECHNICAL_LOGGER.error("Database error occurred while creating Coworking: {}", coworkingSpace, e);
                throw new DataExcessException(String.format("Database error occurred while creating Coworking: %s", coworkingSpace), e);
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error("Failure to establish connection while creating a new Coworking: {}", coworkingSpace, e);
            throw new DataExcessException(String.format("Failure to establish connection while creating a new Coworking: %s", coworkingSpace), e);
        }
    }

    @Override
    public void delete(CoworkingSpace coworkingSpace) throws EntityNotFoundException {
        String deleteCoworkingQuery = """
                DELETE FROM public.coworking_spaces
                WHERE id = ?
                """;
        Long coworkingSpaceId = coworkingSpace.getId();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement deleteCoworkingStatement = connection.prepareStatement(deleteCoworkingQuery)) {
            deleteCoworkingStatement.setLong(1, coworkingSpaceId);
            int rowsAffected = deleteCoworkingStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new EntityNotFoundException(String.format("Failure to delete Coworking with ID: %d. Coworking is not found", coworkingSpace.getId())
                        , DaoErrorCode.COWORKING_IS_NOT_FOUND);
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error("Database error occurred while deleting Coworking space: {}", coworkingSpace, e);
            throw new DataExcessException(String.format("Database error occurred while deleting Coworking space: %s", coworkingSpace), e);
        }
    }

    @Override
    public CoworkingSpace getById(Long coworkingId) throws EntityNotFoundException {
        String selectCoworkingSpaceQuery = """
                SELECT admin_id, price, type
                FROM public.coworking_spaces
                WHERE id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectCoworkingStatement = connection.prepareStatement(selectCoworkingSpaceQuery)) {
            selectCoworkingStatement.setLong(1, coworkingId);

            try (ResultSet coworkingResultSet = selectCoworkingStatement.executeQuery()) {
                if (!coworkingResultSet.next()) {
                    throw new EntityNotFoundException(String.format("Failure to get Coworking space with ID: %d"
                            , coworkingId), DaoErrorCode.COWORKING_IS_NOT_FOUND);
                }
                CoworkingSpace coworkingSpace = new CoworkingSpace();
                Long adminId;
                User admin = new Admin();
                double price;
                CoworkingType coworkingType;

                adminId = coworkingResultSet.getLong("admin_id");
                price = coworkingResultSet.getDouble("price");
                String type = coworkingResultSet.getString("type");
                coworkingType = CoworkingType.valueOf(
                        type.replace(" ", "_").toUpperCase());

                List<Facility> facilities = getFacilitiesForCoworkingSpace(coworkingId, connection);

                coworkingSpace.setId(coworkingId);
                admin.setId(adminId);
                coworkingSpace.setAdmin(admin);
                coworkingSpace.setPrice(price);
                coworkingSpace.setCoworkingType(coworkingType);
                coworkingSpace.setFacilities(facilities);
                return coworkingSpace;
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error("Database error occurred while getting coworking space by ID: {}", coworkingId, e);
            throw new DataExcessException(String.format("Database error occurred while getting coworking space by ID: %d ", coworkingId), e);
        }
    }

    @Override
    public List<CoworkingSpace> getAll() {
        List<CoworkingSpace> coworkingSpaces = new ArrayList<>();

        String selectCoworkingSpacesQuery = """
                SELECT id, admin_id, price, type
                FROM public.coworking_spaces
                """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectCoworkingStatement = connection.prepareStatement(selectCoworkingSpacesQuery)) {

            ResultSet coworkingSpacesResultSet = selectCoworkingStatement.executeQuery();
            while (coworkingSpacesResultSet.next()) {
                CoworkingSpace coworkingSpace = new CoworkingSpace();
                User admin = new Admin();

                Long coworkingId = coworkingSpacesResultSet.getLong("id");
                Long adminId = coworkingSpacesResultSet.getLong("admin_id");
                double price = coworkingSpacesResultSet.getDouble("price");
                String type = coworkingSpacesResultSet.getString("type");
                CoworkingType coworkingType = CoworkingType.valueOf(
                        type.replace(" ", "_").toUpperCase());

                List<Facility> facilities = getFacilitiesForCoworkingSpace(coworkingId, connection);

                coworkingSpace.setId(coworkingId);
                admin.setId(adminId);
                coworkingSpace.setAdmin(admin);
                coworkingSpace.setPrice(price);
                coworkingSpace.setCoworkingType(coworkingType);
                coworkingSpace.setFacilities(facilities);
                coworkingSpaces.add(coworkingSpace);

            }
            return coworkingSpaces;
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Database error occurred while getting coworking spaces. ", e);
        }
    }

    @Override
    public List<CoworkingSpace> getAllCoworkingSpacesByAdmin(Long adminId) {
        List<CoworkingSpace> coworkingSpaces = new ArrayList<>();
        String selectCoworkingSpacesQuery = """
                SELECT id, price, type
                FROM public.coworking_spaces
                WHERE admin_id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectCoworkingStatement = connection.prepareStatement(selectCoworkingSpacesQuery)) {
            selectCoworkingStatement.setLong(1, adminId);

            try (ResultSet coworkingResultSet = selectCoworkingStatement.executeQuery()) {
                while (coworkingResultSet.next()) {
                    CoworkingSpace coworkingSpace = new CoworkingSpace();
                    User admin = new Admin();
                    admin.setId(adminId);

                    long coworkingId = coworkingResultSet.getLong("id");
                    double price = coworkingResultSet.getDouble("price");
                    String type = coworkingResultSet.getString("type");
                    CoworkingType coworkingType = CoworkingType.valueOf(type.replace(" ", "_").toUpperCase());

                    List<Facility> facilities = getFacilitiesForCoworkingSpace(coworkingId, connection);

                    coworkingSpace.setId(coworkingId);
                    coworkingSpace.setAdmin(admin);
                    coworkingSpace.setPrice(price);
                    coworkingSpace.setCoworkingType(coworkingType);
                    coworkingSpace.setFacilities(facilities);

                    coworkingSpaces.add(coworkingSpace);
                }
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error("Database error occurred while getting Coworking spaces by admin ID: %d: {}", adminId, e);
            throw new DataExcessException(String.format("Database error occurred while getting Coworking spaces by admin ID: %d ", adminId), e);
        }
        return coworkingSpaces;
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
        String selectFacilitiesQuery = """
                SELECT f.description
                FROM public.coworking_space_facilities cf
                JOIN public.facilities f ON cf.facility_id = f.id
                WHERE cf.coworking_space_id = ?
                """;

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
            TECHNICAL_LOGGER.error("Database error occurred while getting facilities List of Coworking with ID: %d: {}"
                    , coworkingSpaceId, e);
            throw new DataExcessException(String.format("Database error occurred while getting facilities List of Coworking with ID: %d "
                    , coworkingSpaceId), e);
        }
        return facilities;
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
        String selectFacilityIdQuery = """
                SELECT id FROM facilities
                WHERE description = ?
                """;
        try (PreparedStatement selectFacilityIdStatement = connection.prepareStatement(selectFacilityIdQuery)) {
            selectFacilityIdStatement.setString(1, facility.getDescription());
            try (ResultSet facilityIdResultSet = selectFacilityIdStatement.executeQuery()) {
                if (facilityIdResultSet.next()) {
                    return facilityIdResultSet.getLong("id");
                } else {
                    TECHNICAL_LOGGER.error("Failure to find Facility ID for facility: " + facility.getDescription());
                    throw new ObjectFieldNotFoundException(String.format("Failure to find Facility ID for Facility: %s"
                            , facility.getDescription()));
                }
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error("Database error occurred while getting facilities List of Coworking with ID: %d: {}"
                    , facility.getDescription(), e);
            throw new DataExcessException(String.format("Database error occurred while getting ID of Facility: %s."
                    , facility), e);
        }
    }
}
