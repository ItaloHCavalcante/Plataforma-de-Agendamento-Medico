package com.medschedule.api_agendamento.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
