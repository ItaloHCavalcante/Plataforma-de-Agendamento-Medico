package com.medschedule.api_agendamento.dto;

import com.medschedule.api_agendamento.model.FormaPagamento;
import com.medschedule.api_agendamento.model.TipoConsulta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Este DTO representa todos os dados que o front-end enviará ao solicitar uma consulta.
public record SolicitarConsultaDTO(
        @NotNull
        Long agendaId,
        
        // Dados do Paciente
        @NotBlank
        String pacienteNome,
        @NotBlank
        String pacienteCpf,
        @NotBlank
        String pacienteEmail,
        @NotBlank
        String pacienteTelefone,

        @NotNull
        FormaPagamento formaPagamento,
        
        Long convenioId, // Opcional, apenas se formaPagamento for CONVENIO
        
        @NotNull
        TipoConsulta tipoConsulta
) {
}