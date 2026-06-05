import SockJS from 'sockjs-client';
import { Stomp } from 'stompjs/lib/stomp';

const SOCKET_URL = 'http://localhost:8080/ws-medschedule';

let stompClient = null;

export const connect = (userRole, onNotificationReceived) => {
  const socket = new SockJS(SOCKET_URL);
  stompClient = Stomp.over(socket);

  stompClient.connect({}, () => {
    console.log('Conectado ao WebSocket');

    // O admin se inscreve no tópico de novas solicitações
    if (userRole === 'ADMIN') {
      stompClient.subscribe('/topic/solicitacoes', (notification) => {
        onNotificationReceived(JSON.parse(notification.body));
      });
    }

    // Cada cliente se inscreve em um tópico específico para suas notificações
    // O back-end precisa enviar para /user/{userId}/notificacoes
    if (userRole === 'CLIENTE') {
      // A implementação exata depende de como o Spring Security está configurado com WebSockets
      // Este é um exemplo comum
      stompClient.subscribe('/user/queue/notificacoes', (notification) => {
        onNotificationReceived(JSON.parse(notification.body));
      });
    }
  });
};

export const disconnect = () => {
  if (stompClient) {
    stompClient.disconnect(() => {
      console.log('Desconectado do WebSocket');
    });
  }
};

// Função para enviar uma mensagem (se necessário)
export const sendMessage = (destination, message) => {
  if (stompClient && stompClient.connected) {
    stompClient.send(destination, {}, JSON.stringify(message));
  }
};