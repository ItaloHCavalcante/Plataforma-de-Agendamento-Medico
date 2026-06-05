import React, { useState, useEffect } from 'react';
import { listarProfissionais, cadastrarProfissional, deletarProfissional } from '../../services/profissionalService';
import { listarEspecialidades } from '../../services/especialidadeService';
import EmptyState from '../../components/EmptyState';

export default function GerenciarProfissionais() {
  const [profissionais, setProfissionais] = useState([]);
  const [especialidades, setEspecialidades] = useState([]);
  const [form, setForm] = useState({
    nome: '',
    crm: '',
    email: '',
    fotoUrl: '',
    miniCurriculo: '',
    especialidadesIds: [], // Para associar especialidades
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');

  useEffect(() => {
    carregarDados();
  }, []);

  async function carregarDados() {
    setLoading(true);
    try {
      const [profissionaisData, especialidadesData] = await Promise.all([
        listarProfissionais(),
        listarEspecialidades(),
      ]);
      setProfissionais(profissionaisData);
      setEspecialidades(especialidadesData);
    } catch (err) {
      setError('Erro ao carregar dados da página.');
    } finally {
      setLoading(false);
    }
  }

  const handleChange = (e) => {
    const { name, value, type, checked, options } = e.target;
    if (name === 'especialidadesIds') {
      const selectedOptions = Array.from(options)
        .filter(option => option.selected)
        .map(option => Number(option.value));
      setForm({ ...form, [name]: selectedOptions });
    } else {
      setForm({ ...form, [name]: value });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage('');
    setError('');
    
    setLoading(true);
    try {
      // Mapear especialidadesIds para o formato esperado pelo backend
      const profissionalParaCadastrar = {
        ...form,
        especialidades: form.especialidadesIds.map(id => ({ id })),
        convenios: [] // Convenios não estão no formulário, então envia vazio
      };
      await cadastrarProfissional(profissionalParaCadastrar);
      setMessage('Profissional cadastrado com sucesso!');
      setForm({ // Limpa o formulário
        nome: '',
        crm: '',
        email: '',
        fotoUrl: '',
        miniCurriculo: '',
        especialidadesIds: [],
      });
      carregarDados(); // Recarrega a lista
    } catch (err) {
      setError('Erro ao cadastrar profissional. Verifique os dados.');
    } finally {
      setLoading(false);
    }
  };

  const handleDeletar = async (id) => {
    if (!window.confirm('Tem certeza que deseja deletar este profissional?')) return;
    
    try {
      await deletarProfissional(id);
      carregarDados(); // Recarrega a lista
    } catch (err) {
      setError('Erro ao deletar profissional. Pode estar vinculado a agendas ou consultas.');
    }
  };

  if (loading) {
    return <div>Carregando...</div>;
  }

  return (
    <div>
      <div className="page-title">
        <h2>Gerenciar Profissionais</h2>
        <p>Adicione, visualize e remova profissionais do sistema.</p>
      </div>

      <div className="grid-two">
        <div className="panel">
          <div className="panel-header">
            <h3>Cadastrar Novo Profissional</h3>
          </div>
          <form onSubmit={handleSubmit} className="form-stack">
            <label>
              Nome
              <input type="text" name="nome" value={form.nome} onChange={handleChange} placeholder="Nome completo" required />
            </label>
            <label>
              CRM
              <input type="text" name="crm" value={form.crm} onChange={handleChange} placeholder="CRM do profissional" required />
            </label>
            <label>
              Email
              <input type="email" name="email" value={form.email} onChange={handleChange} placeholder="Email do profissional" required />
            </label>
            <label>
              URL da Foto (Opcional)
              <input type="text" name="fotoUrl" value={form.fotoUrl} onChange={handleChange} placeholder="URL da foto do profissional" />
            </label>
            <label>
              Mini Currículo
              <textarea name="miniCurriculo" value={form.miniCurriculo} onChange={handleChange} placeholder="Breve descrição do profissional" rows="3"></textarea>
            </label>
            <label>
              Especialidades
              <select name="especialidadesIds" multiple={true} value={form.especialidadesIds} onChange={handleChange} required>
                <option value="">-- Selecione as especialidades --</option>
                {especialidades.map(esp => (
                  <option key={esp.id} value={esp.id}>{esp.nome}</option>
                ))}
              </select>
            </label>
            
            {message && <div className="alert success">{message}</div>}
            {error && <div className="alert error">{error}</div>}

            <button className="primary-button" type="submit">
              Cadastrar Profissional
            </button>
          </form>
        </div>

        <div className="panel">
          <div className="panel-header">
            <h3>Profissionais Cadastrados</h3>
          </div>
          {profissionais.length === 0 ? (
            <EmptyState message="Nenhum profissional cadastrado." />
          ) : (
            <div className="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>Nome</th>
                    <th>CRM</th>
                    <th>Email</th>
                    <th>Especialidades</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {profissionais.map(p => (
                    <tr key={p.id}>
                      <td>{p.nome}</td>
                      <td>{p.crm}</td>
                      <td>{p.email}</td>
                      <td>{p.especialidades?.map(e => e.nome).join(', ') || '-'}</td>
                      <td>
                        <button 
                          className="button danger" 
                          onClick={() => handleDeletar(p.id)}
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