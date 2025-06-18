package org.example.coworking.service;

import org.example.coworking.repository.CoworkingRepository;
import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.entity.Admin;
import org.example.coworking.entity.CoworkingSpace;
import org.example.coworking.entity.CoworkingType;
import org.example.coworking.entity.Facility;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.ServiceErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CoworkingServiceImpl implements CoworkingService {
    private final CoworkingRepository coworkingRepository;

    @Autowired
    public CoworkingServiceImpl(@Qualifier("jpaCoworkingRepository") CoworkingRepository coworkingRepository) {
        this.coworkingRepository = coworkingRepository;
    }

    @Override
    public void add(Admin admin, double price, CoworkingType coworkingType, Set<Facility> facilities) {
        coworkingRepository.create(new CoworkingSpace(admin, price, coworkingType, facilities));
    }

    @Override
    public void delete(Admin admin, Long coworkingSpaceId) throws ForbiddenActionException, EntityNotFoundException {
        CoworkingSpace coworkingSpace = getById(coworkingSpaceId);
        if (coworkingSpace.getAdmin().getId().equals(admin.getId())) {
            coworkingRepository.delete(coworkingSpace);
        } else {
            throw new ForbiddenActionException("Action is forbidden for the user: " + admin.getName(),
                    ServiceErrorCode.FORBIDDEN_ACTION);
        }
    }

    @Override
    public CoworkingSpace getById(Long coworkingId) throws EntityNotFoundException {
        return coworkingRepository.getById(coworkingId);
    }

    @Override
    public List<CoworkingSpace> getAll() {
        return coworkingRepository.getAll();
    }

    @Override
    public List<CoworkingSpace> getAllByAdmin(Admin admin) {
        return coworkingRepository.getAllCoworkingSpacesByAdmin(admin.getId());
    }
}
