package com.medschedule.api_agendamento.repository;

import com.medschedule.api.domain.model.Profissional;
import com.medschedule.api.domain.model.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    List<Profissional> findByEspecialidade(Especialidade especialidade);
}