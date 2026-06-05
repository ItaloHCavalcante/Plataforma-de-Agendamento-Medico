package com.medschedule.api_agendamento.controller;

import com.medschedule.api_agendamento.model.Especialidade;
import com.medschedule.api_agendamento.repository.EspecialidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/especialidades")
public class EspecialidadeController {

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    @GetMapping
    public ResponseEntity<List<Especialidade>> listar() {
        return ResponseEntity.ok(especialidadeRepository.findAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Especialidade> criar(@RequestBody Especialidade especialidade) {
        Especialidade novaEspecialidade = especialidadeRepository.save(especialidade);
        return ResponseEntity.status(201).body(novaEspecialidade);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!especialidadeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        especialidadeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}