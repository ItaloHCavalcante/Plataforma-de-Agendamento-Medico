package com.medschedule.api_agendamento.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consulta")
public class ConsultaController {

    @Autowired
    private ConsultaRepository consuiltaRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    @PostMapping
    @Transactional
    public ReponseEntity<?> agendar(@RequestBody Consulta consulta){
        var agenda = agendaRepository.findById(consulta.getAgendaId())
                .orElseThrow(() -> new RuntimeException("Horário não encontrado"));
        if(!agenda.isDisponivel()) {
            return ResponseEntity.badRequest().body("Este horário já está ocupado!");
        }

        agenda.setDisponivel(false);
        var agendamento = consultaRepository.save(consulta);

        return ResponseEntity.ok(agendamento);
    }
}
