package org.example.coworking.entity;

public enum UserRole {
    ADMIN(Admin.class),
    CUSTOMER(Customer.class);
    private final Class<? extends User> userClass;

    UserRole(Class<? extends User> userClass) {
        this.userClass = userClass;
    }

    public Class<? extends User> getUserClass() {
        return userClass;
    }
}

