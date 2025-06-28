package org.example.coworking.mapper;

import org.example.coworking.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User getUserEntity(Long adminId) {
        User admin = new User();
        admin.setId(adminId);
        return admin;
    }
}
