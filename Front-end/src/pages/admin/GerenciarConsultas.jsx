import React, { useState, useEffect } from 'react';
import { listarTodasConsultas, confirmarConsulta, cancelarConsulta } from '../../services/consultaService';
import EmptyState from '../../components/EmptyState';

export default function GerenciarConsultas() {
  const [consultas, setConsultas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    carregarConsultas();
  }, []);

  async function carregarConsultas() {
    setLoading(true);
    try {
      const data = await listarTodasConsultas();
      setConsultas(data);
    } catch (err) {
      setError('Erro ao carregar as solicitações de consulta.');
    } finally {
      setLoading(false);
    }
  }

  const handleConfirmar = async (id) => {
    try {
      await confirmarConsulta(id);
      // Atualiza o status localmente para uma resposta visual imediata
      setConsultas(consultas.map(c => c.id === id ? { ...c, status: 'CONFIRMADA' } : c));
    } catch (err) {
      alert('Erro ao confirmar a consulta.');
    }
  };

  const handleCancelar = async (id) => {
    if (!window.confirm('Tem certeza que deseja cancelar esta consulta?')) return;
    try {
      await cancelarConsulta(id);
      setConsultas(consultas.map(c => c.id === id ? { ...c, status: 'CANCELADA' } : c));
    } catch (err) {
      alert('Erro ao cancelar a consulta.');
    }
  };

  const getStatusClass = (status) => {
    switch (status) {
      case 'CONFIRMADA': return 'status-confirmed';
      case 'CANCELADA': return 'status-cancelled';
      case 'PENDENTE': return 'status-pending';
      default: return '';
    }
  };

  if (loading) {
    return <div>Carregando...</div>;
  }

  if (error) {
    return <div className="alert error">{error}</div>;
  }

  const consultasPendentes = consultas.filter(c => c.status === 'PENDENTE');
  const outrasConsultas = consultas.filter(c => c.status !== 'PENDENTE');

  return (
    <div>
      <div className="page-title">
        <h2>Gerenciar Solicitações de Consulta</h2>
        <p>Aprove ou cancele as solicitações pendentes e visualize o histórico.</p>
      </div>

      {/* Seção de Consultas Pendentes */}
      <div className="panel">
        <div className="panel-header">
          <h3>Solicitações Pendentes</h3>
        </div>
        {consultasPendentes.length === 0 ? (
          <EmptyState message="Nenhuma solicitação pendente no momento." />
        ) : (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Paciente</th>
                  <th>Profissional</th>
                  <th>Data</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {consultasPendentes.map(c => (
                  <tr key={c.id}>
                    <td>{c.paciente?.nome || 'N/A'}</td>
                    <td>{c.agenda?.profissional?.nome || 'N/A'}</td>
                    <td>{new Date(c.agenda?.data).toLocaleString()}</td>
                    <td>
                      <button onClick={() => handleConfirmar(c.id)} className="button success" style={{marginRight: '8px'}}>Confirmar</button>
                      <button onClick={() => handleCancelar(c.id)} className="button danger">Cancelar</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Seção de Histórico de Consultas */}
      <div className="panel">
        <div className="panel-header">
          <h3>Histórico de Consultas</h3>
        </div>
        {outrasConsultas.length === 0 ? (
          <EmptyState message="Nenhuma consulta no histórico." />
        ) : (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Paciente</th>
                  <th>Profissional</th>
                  <th>Data</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {outrasConsultas.map(c => (
                  <tr key={c.id}>
                    <td>{c.paciente?.nome || 'N/A'}</td>
                    <td>{c.agenda?.profissional?.nome || 'N/A'}</td>
                    <td>{new Date(c.agenda?.data).toLocaleString()}</td>
                    <td>
                      <span className={`status-badge ${getStatusClass(c.status)}`}>
                        {c.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}