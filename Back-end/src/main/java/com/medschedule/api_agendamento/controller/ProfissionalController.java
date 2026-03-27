package com.medschedule.api_agendamento.controller;


import com.medschedule.api_agendamento.model.Profissional;
import com.medschedule.api_agendamento.repository.ProfissionalRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {

    @Autowired
    private ProfissionalRepository repository;

    @PostMapping
    @Transactional
    public ReponseEntity<Profissional> cadastrar(@RequestBody @Valid Profissional profissional) {
        var salvo = repository.save(profissional);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping
    public ResponseEntity<List<Profissional>> listarPorEspecialidade(@RequestParam(required = false) Especialidade especialidade ){
        if (especialidade != null) {
            return ResponseEntity.ok(repository.findByEspecialidade(especialidade));
        }
        return ResponseEntity.ok(repository.FindAll());
    }


}
