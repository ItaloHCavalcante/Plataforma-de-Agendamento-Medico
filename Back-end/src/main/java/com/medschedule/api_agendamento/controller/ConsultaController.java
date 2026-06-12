package com.medschedule.api_agendamento.controller;

import com.medschedule.api_agendamento.dto.SolicitarConsultaDTO;
import com.medschedule.api_agendamento.model.Consulta;
import com.medschedule.api_agendamento.service.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consulta")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @PostMapping("/solicitar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Consulta> solicitarAgendamento(@RequestBody @Valid SolicitarConsultaDTO dto) {
        Consulta agendamento = consultaService.solicitarAgendamento(dto);
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

    @PutMapping("/{id}/finalizar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Consulta> finalizarConsulta(@PathVariable Long id) {
        Consulta consulta = consultaService.finalizarConsulta(id);
        return ResponseEntity.ok(consulta);
    }

    @GetMapping("/todas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Consulta>> listarTodasConsultas() {
        List<Consulta> consultas = consultaService.listarTodasConsultas();
        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/minhas-consultas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<Consulta>> listarMinhasConsultas() {
        List<Consulta> consultas = consultaService.listarMinhasConsultas();
        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/minhas-consultas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<Consulta>> listarMinhasConsultas() {
        List<Consulta> consultas = consultaService.listarMinhasConsultas();
        return ResponseEntity.ok(consultas);
    }
}
