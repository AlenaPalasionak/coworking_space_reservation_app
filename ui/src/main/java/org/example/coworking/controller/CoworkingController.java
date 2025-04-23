package org.example.coworking.controller;

import org.example.coworking.dto.CoworkingSpaceDto;
import org.example.coworking.mapper.CoworkingMapper;
import org.example.coworking.mapper.UserMapper;
import org.example.coworking.entity.Admin;
import org.example.coworking.entity.CoworkingSpace;
import org.example.coworking.service.CoworkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CoworkingController {
    private final CoworkingService coworkingService;
    private final CoworkingMapper coworkingMapper;
    private final UserMapper userMapper;

    @Autowired
    public CoworkingController(CoworkingService coworkingService, CoworkingMapper mapper, UserMapper userMapper) {
        this.coworkingService = coworkingService;
        this.coworkingMapper = mapper;
        this.userMapper = userMapper;
    }

    @PostMapping("/coworking-spaces")
    public ResponseEntity<Void> add(@RequestBody CoworkingSpaceDto coworkingSpaceDto) {
        CoworkingSpace coworkingSpace = coworkingMapper.coworkingSpaceDtoToEntity(coworkingSpaceDto);
        coworkingService.add(coworkingSpace);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/coworking/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestParam Long adminId) {
        Admin admin = userMapper.getAdminEntity(adminId);
        coworkingService.delete(admin, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/coworking-spaces")
    public ResponseEntity<List<CoworkingSpaceDto>> getAllSpaces() {
        List<CoworkingSpace> coworkingSpaces = coworkingService.getAll();
        List<CoworkingSpaceDto> coworkingSpaceDtoList = coworkingMapper.coworkingSpacesToDtoList(coworkingSpaces);
        return ResponseEntity.ok(coworkingSpaceDtoList);
    }
}


