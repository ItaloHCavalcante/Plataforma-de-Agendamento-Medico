package com.medschedule.api_agendamento.repository;

import com.medschedule.api_agendamento.domain.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    //
    boolean existsByCpf(String cpf);
}