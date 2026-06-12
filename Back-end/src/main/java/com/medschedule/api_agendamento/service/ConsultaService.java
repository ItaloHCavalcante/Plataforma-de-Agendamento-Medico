package com.medschedule.api_agendamento.service;

import com.medschedule.api_agendamento.dto.SolicitarConsultaDTO;
import com.medschedule.api_agendamento.infra.exception.BusinessException;
import com.medschedule.api_agendamento.infra.exception.ResourceNotFoundException;
import com.medschedule.api_agendamento.model.*;
import com.medschedule.api_agendamento.repository.AgendaRepository;
import com.medschedule.api_agendamento.repository.ConsultaRepository;
import com.medschedule.api_agendamento.repository.ConvenioRepository;
import com.medschedule.api_agendamento.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConvenioRepository convenioRepository;

    @Autowired
    private NotificacaoService notificacaoService;

    @Transactional
    public Consulta solicitarAgendamento(SolicitarConsultaDTO dto) {
        Agenda agenda = agendaRepository.findById(dto.agendaId())
                .orElseThrow(() -> new ResourceNotFoundException("Horário de agenda não encontrado."));

        if (!agenda.isDisponivel()) {
            throw new BusinessException("Este horário já está ocupado ou não está disponível.");
        }

        Optional<Paciente> pacienteExistente = pacienteRepository.findByCpf(dto.pacienteCpf());
        Paciente paciente;
        if (pacienteExistente.isPresent()) {
            paciente = pacienteExistente.get();
            paciente.setNome(dto.pacienteNome());
            paciente.setEmail(dto.pacienteEmail());
            paciente.setTelefone(dto.pacienteTelefone());
            pacienteRepository.save(paciente);
        } else {
            paciente = new Paciente(null, dto.pacienteNome(), dto.pacienteCpf(), dto.pacienteEmail(), dto.pacienteTelefone());
            pacienteRepository.save(paciente);
        }

        Convenio convenio = null;
        if (dto.formaPagamento() == FormaPagamento.CONVENIO) {
            if (dto.convenioId() == null) {
                throw new BusinessException("Convênio é obrigatório para forma de pagamento CONVENIO.");
            }
            convenio = convenioRepository.findById(dto.convenioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Convênio não encontrado."));
        }

        Consulta novaConsulta = new Consulta();
        novaConsulta.setAgenda(agenda);
        novaConsulta.setPaciente(paciente);
        novaConsulta.setProfissional(agenda.getProfissional());
        // Correção: agenda.getHorario() já é um LocalTime, não precisa de parse.
        novaConsulta.setDataHora(LocalDateTime.of(agenda.getData(), agenda.getHorario()));
        novaConsulta.setFormaPagamento(dto.formaPagamento());
        novaConsulta.setConvenio(convenio);
        novaConsulta.setTipoConsulta(dto.tipoConsulta());
        novaConsulta.setStatus(StatusConsulta.EM_ANDAMENTO);
        novaConsulta.setDataSolicitacao(LocalDateTime.now());

        Consulta consultaSalva = consultaRepository.save(novaConsulta);

        agenda.setDisponivel(false);
        agendaRepository.save(agenda);

        String mensagemAdmin = "Nova solicitação de consulta de " + paciente.getNome() + " para " + agenda.getProfissional().getNome() + ".";
        notificacaoService.enviarNotificacao(consultaSalva, mensagemAdmin, "/topic/solicitacoes");

        return consultaSalva;
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
        notificacaoService.enviarNotificacao(consultaConfirmada, mensagem, "/user/" + consultaConfirmada.getPaciente().getId() + "/queue/notificacoes");

        return consultaConfirmada;
    }

    @Transactional
    public Consulta cancelarConsulta(Long consultaId) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada!"));

        if (consulta.getStatus() == StatusConsulta.AGENDADA) {
            throw new BusinessException("Consultas já agendadas não podem ser canceladas diretamente por aqui.");
        }

        consulta.setStatus(StatusConsulta.CANCELADA);
        
        var agenda = consulta.getAgenda();
        agenda.setDisponivel(true);
        agendaRepository.save(agenda);

        Consulta consultaCancelada = consultaRepository.save(consulta);

        String mensagem = "Sua consulta para o dia " + consulta.getDataHora().toLocalDate() + " foi cancelada.";
        notificacaoService.enviarNotificacao(consultaCancelada, mensagem, "/user/" + consultaCancelada.getPaciente().getId() + "/queue/notificacoes");

        return consultaCancelada;
    }

    public List<Consulta> listarTodasConsultas() {
        return consultaRepository.findAll();
    }

    public List<Consulta> listarConsultasPorPaciente(Long pacienteId) {
        return consultaRepository.findByPacienteId(pacienteId);
    }
}
