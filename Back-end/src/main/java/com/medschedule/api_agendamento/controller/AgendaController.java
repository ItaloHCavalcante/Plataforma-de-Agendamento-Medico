package com.medschedule.api_agendamento.controller;

import com.medschedule.api_agendamento.model.Agenda;
import com.medschedule.api_agendamento.repository.AgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendas")
public class AgendaController {

    @Autowired
    private AgendaRepository repository;

    @PostMapping
    public ResponseEntity<Agenda> criarHorario(@RequestBody Agenda agenda){
        agenda.setDisponivel(true);
        return ResponseEntity.ok(repository.save(agenda));
    }

    @GetMapping("/disponiveis")
    public List<Agenda> listarDisponiveis(){
        return repository.findByDisponivelTrue();
    }

}
