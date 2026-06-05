package com.medschedule.api_agendamento.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "consulta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agenda_id", nullable = false)
    private Agenda agenda;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "profissional_id") // Campo que estava faltando
    private Profissional profissional;

    @Column(nullable = false)
    private LocalDateTime dataHora; // Campo que estava faltando

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormaPagamento formaPagamento;

    @ManyToOne
    @JoinColumn(name = "convenio_id")
    private Convenio convenio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoConsulta tipoConsulta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusConsulta status;

    @Column(nullable = false)
    private LocalDateTime dataSolicitacao;

    private String observacoes;
}
