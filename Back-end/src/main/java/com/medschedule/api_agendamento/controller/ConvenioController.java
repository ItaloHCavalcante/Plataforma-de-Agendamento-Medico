package com.medschedule.api_agendamento.controller;

import com.medschedule.api_agendamento.model.Convenio;
import com.medschedule.api_agendamento.repository.ConvenioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/convenios")
public class ConvenioController {

    @Autowired
    private ConvenioRepository convenioRepository;

    @GetMapping
    public ResponseEntity<List<Convenio>> listar() {
        return ResponseEntity.ok(convenioRepository.findAll());
    }
}
