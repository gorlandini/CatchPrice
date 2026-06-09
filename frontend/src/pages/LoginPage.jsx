import { useState } from 'react';
import { useAuth } from '../context/AuthContext';

const styles = {
  page: {
    minHeight: '100vh', display: 'flex', alignItems: 'center',
    justifyContent: 'center', background: '#0f172a',
  },
  card: {
    background: '#1e293b', borderRadius: 16, padding: '40px 48px',
    width: '100%', maxWidth: 400, boxShadow: '0 25px 50px rgba(0,0,0,0.5)',
  },
  logo: { textAlign: 'center', marginBottom: 32 },
  logoText: { fontSize: 28, fontWeight: 700, color: '#38bdf8', letterSpacing: '-0.5px' },
  tagline: { fontSize: 13, color: '#64748b', marginTop: 4 },
  label: { display: 'block', fontSize: 13, color: '#94a3b8', marginBottom: 6 },
  input: {
    width: '100%', padding: '10px 14px', background: '#0f172a',
    border: '1px solid #334155', borderRadius: 8, color: '#f1f5f9',
    fontSize: 14, boxSizing: 'border-box', outline: 'none',
  },
  button: {
    width: '100%', padding: '12px', background: '#38bdf8',
    color: '#0f172a', border: 'none', borderRadius: 8,
    fontSize: 15, fontWeight: 600, cursor: 'pointer', marginTop: 8,
  },
  error: { color: '#f87171', fontSize: 13, marginBottom: 12, textAlign: 'center' },
  field: { marginBottom: 16 },
};

export default function LoginPage() {
  const { login } = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try { await login(email, password); }
    catch { setError('Email ou senha inválidos'); }
    finally { setLoading(false); }
  };

  return (
    <div style={styles.page}>
      <div style={styles.card}>
        <div style={styles.logo}>
          <div style={styles.logoText}>💰 CatchPrice</div>
          <div style={styles.tagline}>Monitore preços. Economize de verdade.</div>
        </div>
        <form onSubmit={handleSubmit}>
          <div style={styles.field}>
            <label style={styles.label}>Email</label>
            <input style={styles.input} type='email' value={email}
              onChange={e => setEmail(e.target.value)} required />
          </div>
          <div style={styles.field}>
            <label style={styles.label}>Senha</label>
            <input style={styles.input} type='password' value={password}
              onChange={e => setPassword(e.target.value)} required />
          </div>
          {error && <p style={styles.error}>{error}</p>}
          <button style={styles.button} type='submit' disabled={loading}>
            {loading ? 'Entrando...' : 'Entrar'}
          </button>
        </form>
      </div>
    </div>
  );
}