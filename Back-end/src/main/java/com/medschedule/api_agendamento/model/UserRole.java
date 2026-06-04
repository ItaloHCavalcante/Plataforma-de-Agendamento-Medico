package com.medschedule.api_agendamento.model;

public enum UserRole {
    ADMIN("ADMIN"),
    CLIENTE("CLIENTE");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}