package com.medschedule.api_agendamento.repository;

import com.medschedule.api_agendamento.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    List<Agenda> findByDisponivelTrue();
}