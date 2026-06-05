package com.medschedule.api_agendamento.repository;

import com.medschedule.api_agendamento.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByCpf(String cpf);

    // Adicionando o método que estava faltando
    boolean existsByCpf(String cpf);
}
