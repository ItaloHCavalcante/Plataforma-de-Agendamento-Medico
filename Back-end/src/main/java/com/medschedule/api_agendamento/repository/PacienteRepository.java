package com.medschedule.api_agendamento.repository;

import com.medschedule.api_agendamento.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    // Adicione esta linha para o Service parar de dar erro:
    boolean existsByCpf(String cpf);
}