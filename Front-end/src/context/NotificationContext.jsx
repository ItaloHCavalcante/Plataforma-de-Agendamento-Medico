import React, { createContext, useContext, useState, useEffect } from 'react';
import { useAuth } from './AuthContext';
import { connect, disconnect } from '../services/notificationService';

const NotificationContext = createContext();

export const NotificationProvider = ({ children }) => {
  const { user } = useAuth();
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    if (user) {
      const handleNewNotification = (notification) => {
        setNotifications(prev => [notification, ...prev]);
        // Opcional: mostrar um alerta ou toast
        alert(`Nova Notificação: ${notification.message}`);
      };

      connect(user.role, handleNewNotification);

      return () => disconnect();
    }
  }, [user]);

  const value = {
    notifications,
    count: notifications.length,
  };

  return (
    <NotificationContext.Provider value={value}>
      {children}
    </NotificationContext.Provider>
  );
};

export const useNotifications = () => useContext(NotificationContext);