package com.medschedule.api_agendamento.repository;

import com.medschedule.api_agendamento.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByPacienteId(Long pacienteId);

    // Novo método para buscar consultas pelo login do usuário associado ao paciente
    @Query("SELECT c FROM Consulta c WHERE c.paciente.usuario.login = :login")
    List<Consulta> findByPacienteUsuarioLogin(String login);
}
