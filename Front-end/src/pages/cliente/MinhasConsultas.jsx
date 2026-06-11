import React, { useState, useEffect } from 'react';
import { listarMinhasConsultas } from '../../services/consultaService'; // Usando a nova função
import EmptyState from '../../components/EmptyState';

export default function MinhasConsultas() {
  const [consultas, setConsultas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    async function carregarConsultas() {
      try {
        const data = await listarMinhasConsultas(); // Chamando a nova função
        setConsultas(data);
      } catch (err) {
        setError('Não foi possível carregar suas consultas.');
      } finally {
        setLoading(false);
      }
    }
    carregarConsultas();
  }, []);

  const getStatusClass = (status) => {
    switch (status) {
      case 'AGENDADA': return 'status-confirmed';
      case 'CANCELADA': return 'status-cancelled';
      case 'EM_ANDAMENTO': return 'status-pending';
      case 'FINALIZADA': return 'status-finalized';
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
        <p>Acompanhe o status das suas consultas.</p>
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
                    <td>{consulta.profissional?.nome || 'N/A'}</td>
                    <td>{consulta.profissional?.especialidades?.map(e => e.nome).join(', ') || 'N/A'}</td>
                    <td>{new Date(consulta.dataHora).toLocaleString()}</td>
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
