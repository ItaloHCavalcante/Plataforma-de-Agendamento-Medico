package com.medschedule.api_agendamento.dto;

import com.medschedule.api_agendamento.model.UserRole;

public record RegisterDTO(String nome, String email, String login, String password, UserRole role) {
}