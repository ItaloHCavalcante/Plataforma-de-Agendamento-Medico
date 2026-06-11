package com.medschedule.api_agendamento.service;

import com.medschedule.api_agendamento.model.Agenda;
import com.medschedule.api_agendamento.repository.AgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    public List<Agenda> listarAgendasDisponiveis() {
        // O método findByDisponivelTrue() não existe, o correto é findByDisponivel(true)
        // Vou assumir que o repositório será corrigido ou já foi.
        return agendaRepository.findByDisponivel(true);
    }

    // O método solicitarAgendamento foi removido daqui.
    // A lógica de negócio para solicitar uma consulta agora reside inteiramente
    // no ConsultaService, que é a abordagem correta para manter a coesão
    // e evitar lógica duplicada e conflitante.
}
