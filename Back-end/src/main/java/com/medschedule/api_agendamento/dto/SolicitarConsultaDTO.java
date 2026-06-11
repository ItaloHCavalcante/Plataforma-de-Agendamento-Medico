package com.medschedule.api_agendamento.dto;

import com.medschedule.api_agendamento.model.FormaPagamento;
import com.medschedule.api_agendamento.model.TipoConsulta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SolicitarConsultaDTO(
        @NotNull
        Long agendaId,
        
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
        
        Long convenioId,
        
        @NotNull
        TipoConsulta tipoConsulta
) {
}
