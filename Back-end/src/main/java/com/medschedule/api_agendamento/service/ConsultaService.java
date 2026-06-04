package com.medschedule.api_agendamento.service;

import com.medschedule.api_agendamento.infra.exception.BusinessException;
import com.medschedule.api_agendamento.infra.exception.ResourceNotFoundException;
import com.medschedule.api_agendamento.model.Consulta;
import com.medschedule.api_agendamento.model.StatusConsulta;
import com.medschedule.api_agendamento.repository.AgendaRepository;
import com.medschedule.api_agendamento.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private NotificacaoService notificacaoService;

    @Transactional
    public Consulta agendar(Consulta consulta) {
        var agenda = agendaRepository.findById(consulta.getAgenda().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Horário não encontrado"));

        if (!agenda.isDisponivel()) {
            throw new BusinessException("Este horário já está ocupado!");
        }

        agenda.setDisponivel(false);
        agendaRepository.save(agenda);
        
        consulta.setStatus(StatusConsulta.EM_ANDAMENTO);

        return consultaRepository.save(consulta);
    }
    
    @Transactional
    public Consulta confirmarConsulta(Long consultaId) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada!"));

        if (consulta.getStatus() != StatusConsulta.EM_ANDAMENTO) {
            throw new BusinessException("Apenas consultas em andamento podem ser confirmadas.");
        }

        consulta.setStatus(StatusConsulta.AGENDADA);
        Consulta consultaConfirmada = consultaRepository.save(consulta);

        String mensagem = "Sua consulta para o dia " + consulta.getDataHora().toLocalDate() + " às " + consulta.getDataHora().toLocalTime() + " foi confirmada.";
        String destino = "/topic/paciente/" + consultaConfirmada.getPaciente().getId();
        notificacaoService.enviarNotificacao(consultaConfirmada, mensagem, destino);

        return consultaConfirmada;
    }

    @Transactional
    public Consulta cancelarConsulta(Long consultaId) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada!"));

        consulta.setStatus(StatusConsulta.CANCELADA);
        
        var agenda = consulta.getAgenda();
        agenda.setDisponivel(true);
        agendaRepository.save(agenda);

        Consulta consultaCancelada = consultaRepository.save(consulta);

        String mensagem = "Sua consulta para o dia " + consulta.getDataHora().toLocalDate() + " foi cancelada.";
        String destino = "/topic/paciente/" + consultaCancelada.getPaciente().getId();
        notificacaoService.enviarNotificacao(consultaCancelada, mensagem, destino);

        return consultaCancelada;
    }

    public List<Consulta> listarTodasConsultas() {
        return consultaRepository.findAll();
    }

    public List<Consulta> listarConsultasPorPaciente(Long pacienteId) {
        return consultaRepository.findByPacienteId(pacienteId);
    }
}