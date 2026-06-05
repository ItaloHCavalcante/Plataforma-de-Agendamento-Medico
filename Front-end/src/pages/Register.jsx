import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { registerRequest } from '../services/authService';

export default function Register() {
  const [form, setForm] = useState({
    nome: '',
    email: '',
    login: '',
    password: '',
    role: 'CLIENTE', // Define 'CLIENTE' como o valor padrão
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  function handleChange(event) {
    setForm({ ...form, [event.target.name]: event.target.value });
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setError('');
    setLoading(true);

    if (!form.nome || !form.email || !form.login || !form.password) {
      setError('Todos os campos são obrigatórios.');
      setLoading(false);
      return;
    }

    try {
      await registerRequest(form);
      alert('Registro realizado com sucesso! Você será redirecionado para o login.');
      navigate('/login'); 
    } catch (err) {
      setError('Erro ao registrar. O login ou e-mail já pode estar em uso.');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="login-page">
      <div className="login-card">
        <div className="login-logo">+</div>
        <h1>MedAgenda - Crie sua Conta</h1>
        <p>Preencha os dados para se registrar na plataforma.</p>

        <form onSubmit={handleSubmit} className="form-stack">
          <label>
            Nome Completo
            <input name="nome" value={form.nome} onChange={handleChange} placeholder="Digite seu nome completo" required />
          </label>
          <label>
            Email
            <input type="email" name="email" value={form.email} onChange={handleChange} placeholder="Digite seu e-mail" required />
          </label>
          <label>
            Login
            <input name="login" value={form.login} onChange={handleChange} placeholder="Crie um nome de usuário" required />
          </label>
          <label>
            Senha
            <input type="password" name="password" value={form.password} onChange={handleChange} placeholder="Crie uma senha forte" required />
          </label>
          <label>
            Tipo de Conta
            <select name="role" value={form.role} onChange={handleChange}>
              <option value="CLIENTE">Quero ser Cliente</option>
              <option value="ADMIN">Sou um Administrador</option>
            </select>
          </label>

          {error && <div className="alert error">{error}</div>}

          <button className="primary-button" disabled={loading}>
            {loading ? 'Registrando...' : 'Criar Conta'}
          </button>

          <div style={{ textAlign: 'center', marginTop: '1rem' }}>
            <span>Já tem uma conta? <Link to="/login">Faça Login</Link></span>
          </div>
        </form>
      </div>
    </div>
  );
}