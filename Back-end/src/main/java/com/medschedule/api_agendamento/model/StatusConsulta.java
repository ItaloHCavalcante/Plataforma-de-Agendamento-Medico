package com.medschedule.api_agendamento.model;

public enum StatusConsulta {
    EM_ANDAMENTO, // Cliente solicitou, aguardando confirmação do Admin
    AGENDADA,     // Admin confirmou
    CANCELADA,    // Admin ou sistema cancelou
    FINALIZADA    // Consulta foi realizada
}
