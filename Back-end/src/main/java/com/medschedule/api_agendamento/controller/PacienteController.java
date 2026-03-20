package com.medschedule.api_agendamento.controller;

import com.medschedule.api_agendamento.domain.repository.PacienteRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity<Paciente> cadastrar(@RequestBody @Valid Paciente paciente) {
        var salvo = repository.save(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);

    }

    @GetMapping
    public ResponseEntity<List<Paciente>> listar(){
        return ResponseEntity.ok(repository.findAll());
    }



}
