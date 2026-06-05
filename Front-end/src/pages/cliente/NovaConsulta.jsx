import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { listarEspecialidades } from '../../services/especialidadeService';
import { listarProfissionais } from '../../services/profissionalService';
import { listarAgendasDisponiveis } from '../../services/agendaService';
import { listarConvenios } from '../../services/convenioService';
import { solicitarConsulta } from '../../services/consultaService';
import { useAuth } from '../../context/AuthContext';

const FORMAS_PAGAMENTO = ['CONVENIO', 'PARTICULAR'];
const TIPOS_CONSULTA = ['PRESENCIAL', 'TELEMEDICINA'];

export default function NovaConsulta() {
  const { user } = useAuth();
  const navigate = useNavigate();

  const [especialidades, setEspecialidades] = useState([]);
  const [profissionais, setProfissionais] = useState([]);
  const [agendas, setAgendas] = useState([]);
  const [convenios, setConvenios] = useState([]);

  const [step, setStep] = useState(1);
  const [formData, setFormData] = useState({
    agendaId: '',
    pacienteNome: '',
    pacienteCpf: '',
    pacienteEmail: '',
    pacienteTelefone: '',
    formaPagamento: 'PARTICULAR',
    convenioId: '',
    tipoConsulta: 'PRESENCIAL',
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    async function carregarDadosIniciais() {
      try {
        const [especialidadesData, conveniosData] = await Promise.all([
          listarEspecialidades(),
          listarConvenios(),
        ]);
        setEspecialidades(especialidadesData);
        setConvenios(conveniosData);
      } catch (err) {
        setError('Não foi possível carregar os dados necessários.');
      }
    }
    carregarDadosIniciais();
  }, []);

  const handleEspecialidadeChange = async (especialidadeId) => {
    setFormData({ ...formData, especialidadeId, profissionalId: '', agendaId: '' });
    setProfissionais([]);
    setAgendas([]);
    if (especialidadeId) {
      setLoading(true);
      try {
        const todosProfissionais = await listarProfissionais(); 
        const profissionaisFiltrados = todosProfissionais.filter(p => 
          p.especialidades && p.especialidades.some(e => e.id === Number(especialidadeId))
        );
        setProfissionais(profissionaisFiltrados);
        setStep(2);
      } catch (err) {
        setError('Erro ao buscar profissionais.');
      } finally {
        setLoading(false);
      }
    }
  };

  const handleProfissionalChange = async (profissionalId) => {
    setFormData({ ...formData, profissionalId, agendaId: '' });
    setAgendas([]);
    if (profissionalId) {
      setLoading(true);
      try {
        const agendasData = await listarAgendasDisponiveis();
        const agendasFiltradas = agendasData.filter(a => a.profissional.id === Number(profissionalId));
        setAgendas(agendasFiltradas);
        setStep(3);
      } catch (err) {
        setError('Erro ao buscar agendas.');
      } finally {
        setLoading(false);
      }
    }
  };

  const handleAgendaChange = (agendaId) => {
    setFormData({ ...formData, agendaId });
    setStep(4);
  };

  const handleFormChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      // Monta o objeto final para a API
      const dadosParaApi = {
        agendaId: Number(formData.agendaId),
        pacienteNome: formData.pacienteNome,
        pacienteCpf: formData.pacienteCpf,
        pacienteEmail: formData.pacienteEmail,
        pacienteTelefone: formData.pacienteTelefone,
        formaPagamento: formData.formaPagamento,
        convenioId: formData.formaPagamento === 'CONVENIO' ? Number(formData.convenioId) : null,
        tipoConsulta: formData.tipoConsulta,
      };

      await solicitarConsulta(dadosParaApi);
      
      alert('Consulta solicitada com sucesso! Você será notificado quando for confirmada.');
      navigate('/cliente/minhas-consultas');
    } catch (err) {
      setError('Ocorreu um erro ao solicitar sua consulta. Verifique os dados e tente novamente.');
      console.error('Erro em handleSubmit:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <div className="page-title">
        <h2>Agendar Nova Consulta</h2>
        <p>Siga os passos para encontrar o melhor horário para você.</p>
      </div>

      <div className="panel">
        <h3>Passo 1: Selecione a Especialidade</h3>
        <select
          value={formData.especialidadeId}
          onChange={(e) => handleEspecialidadeChange(e.target.value)}
          disabled={loading}
        >
          <option value="">-- Escolha uma especialidade --</option>
          {especialidades.map(e => <option key={e.id} value={e.id}>{e.nome}</option>)}
        </select>
      </div>

      {step >= 2 && formData.especialidadeId && (
        <div className="panel">
          <h3>Passo 2: Selecione o Profissional</h3>
          <select
            value={formData.profissionalId}
            onChange={(e) => handleProfissionalChange(e.target.value)}
            disabled={loading || profissionais.length === 0}
          >
            <option value="">-- Escolha um profissional --</option>
            {profissionais.map(p => <option key={p.id} value={p.id}>{p.nome}</option>)}
          </select>
        </div>
      )}

      {step >= 3 && formData.profissionalId && (
        <div className="panel">
          <h3>Passo 3: Selecione o Horário</h3>
          <select
            value={formData.agendaId}
            onChange={(e) => handleAgendaChange(e.target.value)}
            disabled={loading || agendas.length === 0}
          >
            <option value="">-- Escolha um horário --</option>
            {agendas.map(a => <option key={a.id} value={a.id}>{new Date(a.data).toLocaleDateString()} às {a.horario}</option>)}
          </select>
        </div>
      )}

      {step >= 4 && formData.agendaId && (
        <div className="panel">
          <h3>Passo 4: Confirme seus Dados</h3>
          <form onSubmit={handleSubmit} className="form-stack">
            <h4>Informações do Paciente</h4>
            <input name="pacienteNome" value={formData.pacienteNome} onChange={handleFormChange} placeholder="Nome Completo" required />
            <input name="pacienteCpf" value={formData.pacienteCpf} onChange={handleFormChange} placeholder="CPF" required />
            <input name="pacienteEmail" value={formData.pacienteEmail} onChange={handleFormChange} placeholder="E-mail" required />
            <input name="pacienteTelefone" value={formData.pacienteTelefone} onChange={handleFormChange} placeholder="Telefone" required />

            <h4>Detalhes da Consulta</h4>
            <select name="tipoConsulta" value={formData.tipoConsulta} onChange={handleFormChange}>
              {TIPOS_CONSULTA.map(t => <option key={t} value={t}>{t}</option>)}
            </select>
            <select name="formaPagamento" value={formData.formaPagamento} onChange={handleFormChange}>
              {FORMAS_PAGAMENTO.map(f => <option key={f} value={f}>{f}</option>)}
            </select>
            {formData.formaPagamento === 'CONVENIO' && (
              <select name="convenioId" value={formData.convenioId} onChange={handleFormChange} required>
                <option value="">-- Selecione o Convênio --</option>
                {convenios.map(c => <option key={c.id} value={c.id}>{c.nome}</option>)}
              </select>
            )}

            {error && <div className="alert error">{error}</div>}
            <button type="submit" className="primary-button" disabled={loading}>
              {loading ? 'Enviando Solicitação...' : 'Solicitar Agendamento'}
            </button>
          </form>
        </div>
      )}
    </div>
  );
}