package com.medschedule.api_agendamento.repository;

import com.medschedule.api_agendamento.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    List<Profissional> findByEspecialidade(Especialidade especialidade);
}