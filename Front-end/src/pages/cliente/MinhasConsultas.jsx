import React, { useState, useEffect } from 'react';
import { listarConsultasPorPaciente } from '../../services/consultaService';
import { useAuth } from '../../context/AuthContext';
import EmptyState from '../../components/EmptyState';

export default function MinhasConsultas() {
  const { user } = useAuth();
  const [consultas, setConsultas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    if (user?.id) {
      async function carregarConsultas() {
        try {
          // Este endpoint precisa existir e retornar as consultas do usuário logado.
          // O ideal é que o back-end use o token para identificar o usuário,
          // em vez de depender de um ID na URL que pode ser inseguro.
          // Ex: GET /api/consultas/minhas-consultas
          const data = await listarConsultasPorPaciente(user.id); // Supondo que user.id é o ID do PACIENTE
          setConsultas(data);
        } catch (err) {
          setError('Não foi possível carregar suas consultas. O endpoint pode não estar pronto.');
        } finally {
          setLoading(false);
        }
      }
      carregarConsultas();
    }
  }, [user]);

  const getStatusClass = (status) => {
    switch (status) {
      case 'CONFIRMADA': return 'status-confirmed';
      case 'CANCELADA': return 'status-cancelled';
      case 'PENDENTE': return 'status-pending';
      default: return '';
    }
  };

  if (loading) {
    return <div>Carregando seus agendamentos...</div>;
  }

  if (error) {
    return <div className="alert error">{error}</div>;
  }

  return (
    <div>
      <div className="page-title">
        <h2>Meus Agendamentos</h2>
        <p>Acompanhe o status e o histórico de suas consultas.</p>
      </div>

      {consultas.length === 0 ? (
        <EmptyState message="Você ainda não tem nenhuma consulta agendada." />
      ) : (
        <div className="panel">
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Profissional</th>
                  <th>Especialidade</th>
                  <th>Data e Hora</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {consultas.map(consulta => (
                  <tr key={consulta.id}>
                    <td>{consulta.agenda?.profissional?.nome || 'N/A'}</td>
                    <td>{consulta.agenda?.profissional?.especialidade?.nome || 'N/A'}</td>
                    <td>{new Date(consulta.agenda?.data).toLocaleString()}</td>
                    <td>
                      <span className={`status-badge ${getStatusClass(consulta.status)}`}>
                        {consulta.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}