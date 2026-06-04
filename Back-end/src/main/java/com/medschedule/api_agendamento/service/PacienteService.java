package com.medschedule.api_agendamento.service;

import com.medschedule.api_agendamento.model.Paciente;
import com.medschedule.api_agendamento.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository repository;

    @Transactional
    public Paciente salvar(Paciente paciente) {
        // Regra de negócio: Verifica se o CPF já existe
        if (repository.existsByCpf(paciente.getCpf())) {
            throw new RuntimeException("Já existe um paciente cadastrado com este CPF!");
        }
        return repository.save(paciente);
    }
}