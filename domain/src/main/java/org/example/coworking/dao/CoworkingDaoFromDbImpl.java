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
                    } else {
                        throw new ObjectFieldNotFoundException("Failure to find coworking id " + coworkingSpaceId);
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
            throw new DataExcessException("Failure to establish connection when adding coworking.");
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
            throw new DataExcessException("Failure to establish connection when deleting coworking space: " + coworkingSpace);
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
            connection.setAutoCommit(false);

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

                connection.commit();

                return new CoworkingSpace(coworkingId, admin, price, coworkingType, facilities, reservationPeriods);
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Failure to establish connection when getting coworking space by id: "
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
            throw new DataExcessException("Failure to establish connection when getting coworking spaces");
        }
    }


    private List<Facility> getFacilitiesForCoworkingSpace(Long coworkingSpaceId, Connection connection) {
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
            throw new DataExcessException("Failure to establish connection when getting facilities List of coworking with id: "
                    + coworkingSpaceId);
        }
        return facilities;
    }

    private Set<ReservationPeriod> getReservationPeriodsForCoworkingSpace(Long coworkingSpaceId, Connection connection) {
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
            throw new DataExcessException("Failure to establish connection when getting Reservation Periods for coworking with id: " +
                    coworkingSpaceId);
        }
        return reservationPeriods;
    }

    private long getCoworkingTypeId(CoworkingType type, Connection connection) {
        String selectCoworkingTypeIdQuery = "SELECT id FROM public.coworking_types WHERE description = ?";
        try (PreparedStatement selectCoworkingTypeIdStatement = connection.prepareStatement(selectCoworkingTypeIdQuery)) {
            selectCoworkingTypeIdStatement.setString(1, type.getDescription());
            try (ResultSet coworkingTypeIdResultSet = selectCoworkingTypeIdStatement.executeQuery()) {
                if (coworkingTypeIdResultSet.next()) {
                    return coworkingTypeIdResultSet.getLong("id");
                } else {
                    throw new ObjectFieldNotFoundException("Failure to find coworking type with description " + type.getDescription());
                }
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Failure to establish connection when getting coworking type id for description: "
                    + type.getDescription());
        }
    }

    private long getFacilityId(Facility facility, Connection connection) {
        String selectFacilityIdQuery = "SELECT id FROM facilities WHERE description = ?";
        try (PreparedStatement selectFacilityIdStatement = connection.prepareStatement(selectFacilityIdQuery)) {
            selectFacilityIdStatement.setString(1, facility.getDescription());
            try (ResultSet facilityIdResultSet = selectFacilityIdStatement.executeQuery()) {
                if (facilityIdResultSet.next()) {
                    return facilityIdResultSet.getLong("id");
                } else {
                    throw new ObjectFieldNotFoundException("Failure to find facility id: " + facility.getDescription());
                }
            }
        } catch (SQLException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new DataExcessException("Failure to establish connection when getting facility id: "
                    + facility);
        }
    }
}
