import React, { useState, useEffect } from 'react';
import { listarEspecialidades, criarEspecialidade } from '../../services/especialidadeService';
// Importação de api para usar chamadas diretas quando não há serviço específico
import { api } from '../../services/api'; 
import EmptyState from '../../components/EmptyState';

export default function GerenciarEspecialidades() {
  const [especialidades, setEspecialidades] = useState([]);
  const [novaEspecialidade, setNovaEspecialidade] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    carregarEspecialidades();
  }, []);

  async function carregarEspecialidades() {
    try {
      const data = await listarEspecialidades();
      setEspecialidades(data);
    } catch (err) {
      setError('Erro ao carregar especialidades.');
    }
  }

  const handleCriar = async (e) => {
    e.preventDefault();
    if (!novaEspecialidade.trim()) return;
    
    setLoading(true);
    try {
      await criarEspecialidade({ nome: novaEspecialidade });
      setNovaEspecialidade('');
      carregarEspecialidades(); // Recarrega a lista
    } catch (err) {
      setError('Erro ao criar especialidade.');
    } finally {
      setLoading(false);
    }
  };

  const handleDeletar = async (id) => {
    if (!window.confirm('Tem certeza que deseja deletar esta especialidade?')) return;
    
    try {
      // O serviço de deletar não existe em especialidadeService, usando api direto
      await api.delete(`/especialidades/${id}`);
      carregarEspecialidades();
    } catch (err) {
      setError('Erro ao deletar especialidade. Pode estar vinculada a um profissional.');
    }
  };

  return (
    <div>
      <div className="page-title">
        <h2>Gerenciar Especialidades</h2>
        <p>Adicione ou remova especialidades disponíveis no sistema.</p>
      </div>

      <div className="grid-two">
        <div className="panel">
          <div className="panel-header">
            <h3>Nova Especialidade</h3>
          </div>
          <form onSubmit={handleCriar} className="form-stack">
            <label>
              Nome da Especialidade
              <input
                type="text"
                value={novaEspecialidade}
                onChange={(e) => setNovaEspecialidade(e.target.value)}
                placeholder="Ex: Cardiologia"
                required
              />
            </label>
            {error && <div className="alert error">{error}</div>}
            <button className="primary-button" type="submit" disabled={loading || !novaEspecialidade}>
              {loading ? 'Adicionando...' : 'Adicionar'}
            </button>
          </form>
        </div>

        <div className="panel">
          <div className="panel-header">
            <h3>Especialidades Existentes</h3>
          </div>
          {especialidades.length === 0 ? (
            <EmptyState message="Nenhuma especialidade cadastrada." />
          ) : (
            <div className="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {especialidades.map(esp => (
                    <tr key={esp.id}>
                      <td>{esp.id}</td>
                      <td>{esp.nome}</td>
                      <td>
                        <button 
                          className="button danger" 
                          onClick={() => handleDeletar(esp.id)}
                          style={{ padding: '4px 8px', fontSize: '0.8rem', backgroundColor: '#dc3545', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}
                        >
                          Deletar
                        </button>
                      </td>
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