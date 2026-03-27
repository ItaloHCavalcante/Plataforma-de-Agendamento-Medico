package com.medschedule.api_agendamento.repository;

import com.medschedule.api_agendamento.domain.model.Consulta; // Ajustado aqui
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
}