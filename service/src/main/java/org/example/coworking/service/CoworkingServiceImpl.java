package org.example.coworking.service;

import org.example.coworking.entity.CoworkingSpace;
import org.example.coworking.entity.User;
import org.example.coworking.repository.CoworkingSpaceRepository;
import org.example.coworking.repository.exception.CustomDataExcessException;
import org.example.coworking.repository.exception.DaoErrorCode;
import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.ServiceErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;

@Service
@Transactional
public class CoworkingServiceImpl implements CoworkingService {
    private final CoworkingSpaceRepository coworkingSpaceRepository;

    @Autowired
    public CoworkingServiceImpl(CoworkingSpaceRepository coworkingSpaceRepository) {
        this.coworkingSpaceRepository = coworkingSpaceRepository;
    }

    @Override
    public void save(CoworkingSpace coworkingSpace) {
        try {
            coworkingSpaceRepository.save(coworkingSpace);
        } catch (DataAccessException e) {
            TECHNICAL_LOGGER.error("Database error occurred while saving a new Coworking: {}.", coworkingSpace, e);
            throw new CustomDataExcessException(String.format("Database error occurred while saving a new Coworking: %s.", coworkingSpace), e);
        }
    }

    @Override
    public void delete(User admin, Long coworkingSpaceId) {
        CoworkingSpace coworkingSpace = findById(coworkingSpaceId);
        if (!coworkingSpace.getAdmin().getId().equals(admin.getId())) {
            throw new ForbiddenActionException(String.format("Action is forbidden for the user with ID %d: ",
                    admin.getId()),
                    ServiceErrorCode.FORBIDDEN_ACTION);
        } else {
            try {
                coworkingSpaceRepository.delete(coworkingSpace);
            } catch (DataAccessException e) {
                TECHNICAL_LOGGER.error("Database error occurred while deleting Coworking space with ID: {}",
                        coworkingSpace.getId(), e);
                throw new CustomDataExcessException(String.format("Database error occurred while deleting Coworking space with ID: %d and related reservations.",
                        coworkingSpaceId), e);
            }
        }
    }

    @Override
    public CoworkingSpace findById(Long coworkingId) throws EntityNotFoundException {
        Optional<CoworkingSpace> possibleCoworkingSpace;
        try {
            possibleCoworkingSpace = coworkingSpaceRepository.findById(coworkingId);
        } catch (DataAccessException e) {
            TECHNICAL_LOGGER.error("Database error while getting Coworking Space by ID: {}", coworkingId, e);
            throw new CustomDataExcessException(
                    String.format("Database error while getting coworking space by ID: %d", coworkingId), e);
        }
        if (possibleCoworkingSpace.isEmpty()) {
            throw new EntityNotFoundException(String.format("Failure to get Coworking Space with ID: %d",
                    coworkingId), DaoErrorCode.COWORKING_IS_NOT_FOUND);
        }
        return possibleCoworkingSpace.get();
    }

    @Override
    public List<CoworkingSpace> findAll() {
        try {
            return coworkingSpaceRepository.findAll();
        } catch (DataAccessException e) {
            TECHNICAL_LOGGER.error("Database error occurred while getting Coworking spaces. ", e);
            throw new CustomDataExcessException("Database error occurred while getting Coworking spaces. ", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoworkingSpace> getAllByAdmin(User admin) {
        Long adminId = admin.getId();
        try {
            return coworkingSpaceRepository.getAllCoworkingSpacesByAdmin(adminId);
        } catch (DataAccessException e) {
            TECHNICAL_LOGGER.error("Database error occurred while getting Coworking spaces by admin ID: %d: {}", adminId, e);
            throw new CustomDataExcessException(String.format("Database error occurred while getting Coworking spaces by admin ID: %d ", adminId), e);
        }
    }
}
