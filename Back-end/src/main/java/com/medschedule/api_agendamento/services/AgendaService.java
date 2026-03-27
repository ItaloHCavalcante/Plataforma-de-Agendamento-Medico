package com.medschedule.api.domain.service;

import com.medschedule.api.domain.model.Agenda;
import com.medschedule.api.domain.model.Consulta;
import com.medschedule.api.domain.repository.AgendaRepository;
import com.medschedule.api.domain.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Transactional
    public Consulta agendar(Long agendaId, Long pacienteId) {
        // 1. Busca o horário na agenda ou dá erro se não existir
        var agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> new RuntimeException("Horário não encontrado na agenda!"));

        // 2. Verifica se o horário está livre
        if (!agenda.isDisponivel()) {
            throw new RuntimeException("Este horário já está ocupado por outro paciente!");
        }

        // 3. Bloqueia o horário (vira indisponível)
        agenda.setDisponivel(false);
        agendaRepository.save(agenda);

        // 4. Cria o registro da Consulta
        Consulta consulta = new Consulta();
        consulta.setAgenda(agenda);
        consulta.setPacienteId(pacienteId);
        // Aqui você pode definir um status inicial
        // consulta.setStatus("AGENDADO");

        return consultaRepository.save(consulta);
    }
}