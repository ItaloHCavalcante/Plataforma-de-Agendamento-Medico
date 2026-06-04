package com.medschedule.api_agendamento.service;

import com.medschedule.api_agendamento.model.Consulta;
import com.medschedule.api_agendamento.model.Notificacao;
import com.medschedule.api_agendamento.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void enviarNotificacao(Consulta consulta, String mensagem, String destino) {
        Notificacao notificacao = new Notificacao();
        notificacao.setConsulta(consulta);
        notificacao.setMensagem(mensagem);
        notificacao.setDataEnvio(LocalDateTime.now());

        Notificacao notificacaoSalva = notificacaoRepository.save(notificacao);

        messagingTemplate.convertAndSend(destino, notificacaoSalva);
    }
}