package org.example.coworking.repository;

import org.example.coworking.repository.exception.DaoErrorCode;
import org.example.coworking.repository.exception.DataExcessException;
import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;

/**
 * Class provides functionality for interaction with database and processing Coworking object
 */
@Repository("jdbcCoworkingDao")
public class JdbcCoworkingRepository implements CoworkingRepository {
    private final DataSource dataSource;

    @Autowired
    public JdbcCoworkingRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(CoworkingSpace coworkingSpace) {
        String insertCoworkingSpaceQuery = """
                INSERT INTO public.coworking_spaces (admin_id, price, type)
                VALUES (?, ?, ?) RETURNING id
                """;
        String insertFacilityLinkQuery = """
                INSERT INTO public.coworking_space_facilities (coworking_space_id, facility)
                VALUES (?, ?)
                """;

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            Long coworkingSpaceId;
            try (PreparedStatement insertCoworkingSpaceStatement = connection.prepareStatement(insertCoworkingSpaceQuery,
                    Statement.RETURN_GENERATED_KEYS)) {

                Long adminId = coworkingSpace.getAdmin().getId();
                double price = coworkingSpace.getPrice();
                CoworkingType coworkingType = coworkingSpace.getCoworkingType();

                insertCoworkingSpaceStatement.setLong(1, adminId);
                insertCoworkingSpaceStatement.setDouble(2, price);
                insertCoworkingSpaceStatement.setString(3, coworkingType.name());

                insertCoworkingSpaceStatement.executeUpdate();

                try (ResultSet coworkingIdResultSet = insertCoworkingSpaceStatement.getGeneratedKeys()) {
                    if (coworkingIdResultSet.next()) {
                        coworkingSpaceId = coworkingIdResultSet.getLong(1);
                    } else {
                        throw new DataExcessException("Failure to create Coworking space, no ID obtained.");
                    }
                }

                try (PreparedStatement insertFacilityStatement = connection.prepareStatement(insertFacilityLinkQuery)) {
                    for (Facility facility : coworkingSpace.getFacilities()) {
                        insertFacilityStatement.setLong(1, coworkingSpaceId);
                        insertFacilityStatement.setString(2, facility.name());
                        insertFacilityStatement.addBatch();
                    }
                    insertFacilityStatement.executeBatch();
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
    public void delete(CoworkingSpace coworkingSpace) {
        Long coworkingSpaceId = coworkingSpace.getId();
        String deleteCoworkingQuery = """
                DELETE FROM public.coworking_spaces
                WHERE id = ?
                """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement deleteCoworkingStatement = connection.prepareStatement(deleteCoworkingQuery)) {
            deleteCoworkingStatement.setLong(1, coworkingSpaceId);
            deleteCoworkingStatement.executeUpdate();

        } catch (SQLException e) {
            TECHNICAL_LOGGER.error("Database error occurred while deleting Coworking space with ID: {}", coworkingSpaceId, e);
            throw new DataExcessException(String.format("Database error occurred while deleting Coworking space with ID: %s", coworkingSpaceId), e);
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
                Admin admin = new Admin();
                double price;
                CoworkingType coworkingType;

                adminId = coworkingResultSet.getLong("admin_id");
                price = coworkingResultSet.getDouble("price");
                String type = coworkingResultSet.getString("type");
                coworkingType = CoworkingType.valueOf(type);

                coworkingSpace.setId(coworkingId);
                admin.setId(adminId);
                coworkingSpace.setAdmin(admin);
                coworkingSpace.setPrice(price);
                coworkingSpace.setCoworkingType(coworkingType);

                return coworkingSpace;
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error("Database error occurred while getting coworking space by ID: {}", coworkingId, e);
            throw new DataExcessException(String.format("Database error occurred while getting coworking space by ID: %d ", coworkingId), e);
        }
    }

    @Override
    public List<CoworkingSpace> getAll() {
        Map<Long, CoworkingSpace> coworkingSpaceMap = new HashMap<>();

        String selectCoworkingSpacesQuery = """
                SELECT cs.id, cs.admin_id, cs.price, cs.type, csf.facility
                FROM public.coworking_spaces cs
                LEFT JOIN coworking_space_facilities csf
                ON cs.id = csf.coworking_space_id
                """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectCoworkingStatement = connection.prepareStatement(selectCoworkingSpacesQuery)) {

            ResultSet coworkingSpacesResultSet = selectCoworkingStatement.executeQuery();

            while (coworkingSpacesResultSet.next()) {
                Long coworkingSpaceId = coworkingSpacesResultSet.getLong("id");
                CoworkingSpace coworkingSpace = coworkingSpaceMap.get(coworkingSpaceId);
                if (coworkingSpace == null) {
                    coworkingSpace = new CoworkingSpace();
                    coworkingSpace.setId(coworkingSpaceId);

                    Admin admin = new Admin();
                    Long coworkingId = coworkingSpacesResultSet.getLong("id");
                    Long adminId = coworkingSpacesResultSet.getLong("admin_id");
                    double price = coworkingSpacesResultSet.getDouble("price");
                    String type = coworkingSpacesResultSet.getString("type");
                    CoworkingType coworkingType = CoworkingType.valueOf(type);

                    coworkingSpace.setId(coworkingId);
                    admin.setId(adminId);
                    coworkingSpace.setAdmin(admin);
                    coworkingSpace.setPrice(price);
                    coworkingSpace.setCoworkingType(coworkingType);
                    coworkingSpace.setFacilities(new HashSet<>());

                    coworkingSpaceMap.put(coworkingSpaceId, coworkingSpace);
                }

                String facilityString = coworkingSpacesResultSet.getString("facility");
                if (facilityString != null) {
                    coworkingSpace.getFacilities().add(Facility.valueOf(facilityString));
                }
            }
            return new ArrayList<>(coworkingSpaceMap.values());
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Database error occurred while getting coworking spaces. ", e);
        }
    }

    @Override
    public List<CoworkingSpace> getAllCoworkingSpacesByAdmin(Long adminId) {
        Map<Long, CoworkingSpace> coworkingSpaceMap = new HashMap<>();

        String selectCoworkingSpacesQuery = """
                SELECT cs.id, cs.price, cs.type, csf.facility
                FROM public.coworking_spaces cs
                LEFT JOIN coworking_space_facilities csf
                ON cs.id = csf.coworking_space_id
                WHERE admin_id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectCoworkingStatement = connection.prepareStatement(selectCoworkingSpacesQuery)) {
            selectCoworkingStatement.setLong(1, adminId);
            ResultSet coworkingSpacesResultSet = selectCoworkingStatement.executeQuery();

            while (coworkingSpacesResultSet.next()) {
                Long coworkingSpaceId = coworkingSpacesResultSet.getLong("id");
                CoworkingSpace coworkingSpace = coworkingSpaceMap.get(coworkingSpaceId);
                if (coworkingSpace == null) {
                    coworkingSpace = new CoworkingSpace();
                    coworkingSpace.setId(coworkingSpaceId);

                    Admin admin = new Admin();
                    double price = coworkingSpacesResultSet.getDouble("price");
                    String type = coworkingSpacesResultSet.getString("type");
                    CoworkingType coworkingType = CoworkingType.valueOf(type);

                    coworkingSpace.setId(coworkingSpaceId);
                    admin.setId(coworkingSpaceId);
                    coworkingSpace.setAdmin(admin);
                    coworkingSpace.setPrice(price);
                    coworkingSpace.setCoworkingType(coworkingType);
                    coworkingSpace.setFacilities(new HashSet<>());

                    coworkingSpaceMap.put(coworkingSpaceId, coworkingSpace);
                }

                String facilityString = coworkingSpacesResultSet.getString("facility");
                if (facilityString != null) {
                    coworkingSpace.getFacilities().add(Facility.valueOf(facilityString));
                }
            }
            return new ArrayList<>(coworkingSpaceMap.values());
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error("Database error occurred while getting Coworking spaces by admin ID: %d: {}", adminId, e);
            throw new DataExcessException(String.format("Database error occurred while getting Coworking spaces by admin ID: %d ", adminId), e);
        }
    }
}