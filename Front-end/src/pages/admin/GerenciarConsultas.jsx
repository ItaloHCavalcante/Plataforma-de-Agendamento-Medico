import React, { useState, useEffect } from 'react';
import { listarTodasConsultas, confirmarConsulta, cancelarConsulta, finalizarConsulta } from '../../services/consultaService';
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

  const handleAction = async (action, id) => {
    try {
      let updatedConsulta;
      if (action === 'confirmar') {
        updatedConsulta = await confirmarConsulta(id);
      } else if (action === 'cancelar') {
        if (!window.confirm('Tem certeza que deseja cancelar esta consulta?')) return;
        updatedConsulta = await cancelarConsulta(id);
      } else if (action === 'finalizar') {
        if (!window.confirm('Tem certeza que deseja finalizar esta consulta?')) return;
        updatedConsulta = await finalizarConsulta(id);
      }
      
      // Atualiza o status localmente para uma resposta visual imediata
      setConsultas(consultas.map(c => c.id === id ? { ...c, status: updatedConsulta.status } : c));
    } catch (err) {
      alert(`Erro ao executar a ação: ${err.response?.data?.message || err.message}`);
    }
  };

  const getStatusClass = (status) => {
    switch (status) {
      case 'AGENDADA': return 'status-confirmed';
      case 'CANCELADA': return 'status-cancelled';
      case 'EM_ANDAMENTO': return 'status-pending';
      case 'FINALIZADA': return 'status-finalized'; // Nova classe de estilo
      default: return '';
    }
  };

  if (loading) {
    return <div>Carregando...</div>;
  }

  if (error) {
    return <div className="alert error">{error}</div>;
  }

  const consultasPendentes = consultas.filter(c => c.status === 'EM_ANDAMENTO');
  const consultasAgendadas = consultas.filter(c => c.status === 'AGENDADA');
  const outrasConsultas = consultas.filter(c => c.status !== 'EM_ANDAMENTO' && c.status !== 'AGENDADA');

  return (
    <div>
      <div className="page-title">
        <h2>Gerenciar Consultas</h2>
        <p>Aprove, cancele, finalize e visualize o histórico de consultas.</p>
      </div>

      <div className="panel">
        <div className="panel-header"><h3>Solicitações Pendentes</h3></div>
        {consultasPendentes.length === 0 ? <EmptyState message="Nenhuma solicitação pendente." /> : (
          <div className="table-wrap">
            <table>
              <thead><tr><th>Paciente</th><th>Profissional</th><th>Data</th><th>Ações</th></tr></thead>
              <tbody>
                {consultasPendentes.map(c => (
                  <tr key={c.id}>
                    <td>{c.paciente?.nome || 'N/A'}</td>
                    <td>{c.profissional?.nome || 'N/A'}</td>
                    <td>{new Date(c.dataHora).toLocaleString()}</td>
                    <td>
                      <button onClick={() => handleAction('confirmar', c.id)} className="button success" style={{marginRight: '8px'}}>Confirmar</button>
                      <button onClick={() => handleAction('cancelar', c.id)} className="button danger">Cancelar</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      <div className="panel">
        <div className="panel-header"><h3>Consultas Agendadas</h3></div>
        {consultasAgendadas.length === 0 ? <EmptyState message="Nenhuma consulta agendada no momento." /> : (
          <div className="table-wrap">
            <table>
              <thead><tr><th>Paciente</th><th>Profissional</th><th>Data</th><th>Ações</th></tr></thead>
              <tbody>
                {consultasAgendadas.map(c => (
                  <tr key={c.id}>
                    <td>{c.paciente?.nome || 'N/A'}</td>
                    <td>{c.profissional?.nome || 'N/A'}</td>
                    <td>{new Date(c.dataHora).toLocaleString()}</td>
                    <td>
                      <button onClick={() => handleAction('finalizar', c.id)} className="button primary">Finalizar</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      <div className="panel">
        <div className="panel-header"><h3>Histórico de Consultas</h3></div>
        {outrasConsultas.length === 0 ? <EmptyState message="Nenhum registro no histórico." /> : (
          <div className="table-wrap">
            <table>
              <thead><tr><th>Paciente</th><th>Profissional</th><th>Data</th><th>Status</th></tr></thead>
              <tbody>
                {outrasConsultas.map(c => (
                  <tr key={c.id}>
                    <td>{c.paciente?.nome || 'N/A'}</td>
                    <td>{c.profissional?.nome || 'N/A'}</td>
                    <td>{new Date(c.dataHora).toLocaleString()}</td>
                    <td><span className={`status-badge ${getStatusClass(c.status)}`}>{c.status}</span></td>
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
