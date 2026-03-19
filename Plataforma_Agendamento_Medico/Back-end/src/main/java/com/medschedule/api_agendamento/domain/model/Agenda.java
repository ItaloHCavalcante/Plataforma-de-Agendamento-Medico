package com.medschedule.api_agendamento.domain.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "agenda")
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profissional_id", nullable = false)
    private Profissional profissional;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime horario;

    @Column(nullable = false)
    private Boolean disponivel;

    public Agenda() {
    }

    public Agenda(Long id, Profissional profissional, LocalDate data, LocalTime horario, Boolean disponivel) {
        this.id = id;
        this.profissional = profissional;
        this.data = data;
        this.horario = horario;
        this.disponivel = disponivel;
    }

    public Long getId() {
        return id;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }
}