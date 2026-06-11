import React, { useState, useEffect } from 'react';
import { listarAgendasDisponiveis, criarAgenda } from '../../services/agendaService';
import { listarProfissionais } from '../../services/profissionalService';
import EmptyState from '../../components/EmptyState';

export default function GerenciarAgendas() {
  const [agendas, setAgendas] = useState([]);
  const [profissionais, setProfissionais] = useState([]);
  const [form, setForm] = useState({ profissionalId: '', data: '', horario: '' });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');

  useEffect(() => {
    carregarDados();
  }, []);

  async function carregarDados() {
    setLoading(true);
    try {
      const [agendasData, profissionaisData] = await Promise.all([
        listarAgendasDisponiveis(),
        listarProfissionais(),
      ]);
      setAgendas(agendasData);
      setProfissionais(profissionaisData);
    } catch (err) {
      setError('Erro ao carregar dados da página.');
    } finally {
      setLoading(false);
    }
  }

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage('');
    setError('');
    
    try {
      await criarAgenda({
        profissional: { id: Number(form.profissionalId) },
        data: form.data,
        horario: form.horario,
      });
      setMessage('Horário criado com sucesso!');
      setForm({ profissionalId: '', data: '', horario: '' }); // Limpa o formulário
      carregarDados(); // Recarrega a lista de agendas
    } catch (err) {
      setError('Erro ao criar o horário. Verifique os campos.');
    }
  };

  if (loading) {
    return <div>Carregando...</div>;
  }

  return (
    <div>
      <div className="page-title">
        <h2>Gerenciar Agendas</h2>
        <p>Crie novos horários disponíveis para os profissionais.</p>
      </div>

      <div className="grid-two">
        <div className="panel">
          <div className="panel-header">
            <h3>Criar Novo Horário</h3>
          </div>
          <form onSubmit={handleSubmit} className="form-stack">
            <label>
              Profissional
              <select name="profissionalId" value={form.profissionalId} onChange={handleChange} required>
                <option value="">-- Selecione um profissional --</option>
                {profissionais.map(p => <option key={p.id} value={p.id}>{p.nome}</option>)}
              </select>
            </label>
            <label>
              Data
              <input type="date" name="data" value={form.data} onChange={handleChange} required />
            </label>
            <label>
              Horário
              <input type="time" name="horario" value={form.horario} onChange={handleChange} required />
            </label>
            
            {message && <div className="alert success">{message}</div>}
            {error && <div className="alert error">{error}</div>}

            <button className="primary-button" type="submit">
              Criar Horário
            </button>
          </form>
        </div>

        <div className="panel">
          <div className="panel-header">
            <h3>Horários Disponíveis</h3>
          </div>
          {agendas.length === 0 ? (
            <EmptyState message="Nenhum horário disponível cadastrado." />
          ) : (
            <div className="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>Profissional</th>
                    <th>Data</th>
                    <th>Hora</th>
                  </tr>
                </thead>
                <tbody>
                  {agendas.map(agenda => (
                    <tr key={agenda.id}>
                      <td>{agenda.profissional?.nome || 'N/A'}</td>
                      <td>{new Date(agenda.data).toLocaleDateString()}</td>
                      <td>{agenda.horario}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}