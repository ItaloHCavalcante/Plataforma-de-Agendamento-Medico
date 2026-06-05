package com.medschedule.api_agendamento.repository;

import com.medschedule.api_agendamento.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    @Query("SELECT p FROM Profissional p JOIN p.especialidades e WHERE e.nome = :nomeEspecialidade")
    List<Profissional> findByEspecialidadeNome(@Param("nomeEspecialidade") String nomeEspecialidade);
}
