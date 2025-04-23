package org.example.coworking.mapper;

import org.example.coworking.entity.Admin;
import org.example.coworking.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public Admin getAdminEntity(Long adminId) {
        Admin admin = new Admin();
        admin.setId(adminId);
        return admin;
    }

    public Customer getCustomerEntity(Long customerId) {
        Customer customer = new Customer();
        customer.setId(customerId);
        return customer;
    }
}
