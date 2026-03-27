package com.medschedule.api.domain.model;

import com.medschedule.api_agendamento.model.Profissional;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendas")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHora;

    private boolean disponivel = true;

    @ManyToOne
    @JoinColumn(name = "profissional_id")
    private Profissional profissional;
}