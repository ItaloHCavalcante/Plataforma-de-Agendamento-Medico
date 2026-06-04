package com.medschedule.api_agendamento.service;

import com.medschedule.api_agendamento.infra.exception.BusinessException;
import com.medschedule.api_agendamento.infra.exception.ResourceNotFoundException;
import com.medschedule.api_agendamento.model.Consulta;
import com.medschedule.api_agendamento.model.StatusConsulta;
import com.medschedule.api_agendamento.repository.AgendaRepository;
import com.medschedule.api_agendamento.repository.ConsultaRepository;
import com.medschedule.api_agendamento.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private NotificacaoService notificacaoService;

    @Transactional
    public Consulta solicitarAgendamento(Long agendaId, Long pacienteId) {
        var agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> new ResourceNotFoundException("Horário não encontrado na agenda!"));

        var paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado!"));

        if (!agenda.isDisponivel()) {
            throw new BusinessException("Este horário já está ocupado por outro paciente!");
        }

        agenda.setDisponivel(false);
        agendaRepository.save(agenda);

        Consulta consulta = new Consulta();
        consulta.setAgenda(agenda);
        consulta.setPaciente(paciente);
        consulta.setProfissional(agenda.getProfissional());
        consulta.setDataHora(LocalDateTime.of(agenda.getData(), agenda.getHorario()));
        consulta.setStatus(StatusConsulta.EM_ANDAMENTO);

        Consulta novaConsulta = consultaRepository.save(consulta);

        String mensagem = "Nova solicitação de consulta de " + paciente.getNome();
        notificacaoService.enviarNotificacao(novaConsulta, mensagem, "/topic/admin/solicitacoes");

        return novaConsulta;
    }
}