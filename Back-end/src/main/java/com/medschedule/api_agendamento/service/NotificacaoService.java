package com.medschedule.api_agendamento.service;

import com.medschedule.api_agendamento.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void enviarNotificacao(Long consultaId, String mensagem, String destino) {
        NotificationDTO notificacao = new NotificationDTO(consultaId, mensagem);
        messagingTemplate.convertAndSend(destino, notificacao);
    }
}
