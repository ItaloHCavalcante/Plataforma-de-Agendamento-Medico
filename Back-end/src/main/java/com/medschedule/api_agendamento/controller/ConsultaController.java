package com.medschedule.api_agendamento.controller;

import com.medschedule.api_agendamento.model.Consulta;
import com.medschedule.api_agendamento.service.AgendaService;
import com.medschedule.api_agendamento.service.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consulta")
public class ConsultaController {

    @Autowired
    private AgendaService agendaService;

    @Autowired
    private ConsultaService consultaService;

    @PostMapping("/solicitar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Consulta> solicitarAgendamento(@RequestParam Long agendaId, @RequestParam Long pacienteId) {
        Consulta agendamento = agendaService.solicitarAgendamento(agendaId, pacienteId);
        return ResponseEntity.ok(agendamento);
    }

    @PutMapping("/{id}/confirmar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Consulta> confirmarConsulta(@PathVariable Long id) {
        Consulta consulta = consultaService.confirmarConsulta(id);
        return ResponseEntity.ok(consulta);
    }

    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Consulta> cancelarConsulta(@PathVariable Long id) {
        Consulta consulta = consultaService.cancelarConsulta(id);
        return ResponseEntity.ok(consulta);
    }

    @GetMapping("/todas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Consulta>> listarTodasConsultas() {
        List<Consulta> consultas = consultaService.listarTodasConsultas();
        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<Consulta>> listarConsultasPorPaciente(@PathVariable Long pacienteId) {
        List<Consulta> consultas = consultaService.listarConsultasPorPaciente(pacienteId);
        return ResponseEntity.ok(consultas);
    }
}