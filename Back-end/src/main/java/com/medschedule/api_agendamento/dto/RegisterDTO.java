package com.medschedule.api_agendamento.dto;

import com.medschedule.api_agendamento.model.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}