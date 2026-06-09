import { useState } from 'react';
import api from '../api/axiosConfig';
import { useNavigate } from 'react-router-dom';

const S = {
  page: { minHeight: '100vh', background: '#0f172a', color: '#f1f5f9', padding: '40px 24px' },
  card: { maxWidth: 520, margin: '0 auto', background: '#1e293b', borderRadius: 16, padding: 32 },
  title: { fontSize: 22, fontWeight: 700, color: '#38bdf8', marginBottom: 8 },
  subtitle: { fontSize: 14, color: '#64748b', marginBottom: 28 },
  label: { fontSize: 13, color: '#94a3b8', display: 'block', marginBottom: 6 },
  input: {
    width: '100%', padding: '10px 14px', background: '#0f172a',
    border: '1px solid #334155', borderRadius: 8, color: '#f1f5f9',
    fontSize: 14, boxSizing: 'border-box', marginBottom: 14,
  },
  btnPrimary: {
    padding: '10px 20px', background: '#38bdf8', color: '#0f172a',
    border: 'none', borderRadius: 8, fontWeight: 600, cursor: 'pointer',
  },
  btnSecondary: {
    padding: '10px 20px', background: 'transparent', color: '#64748b',
    border: '1px solid #334155', borderRadius: 8, cursor: 'pointer', marginLeft: 12,
  },
  productItem: {
    background: '#0f172a', borderRadius: 8, padding: '12px 16px',
    marginBottom: 10, display: 'flex', justifyContent: 'space-between', alignItems: 'center',
  },
  productName: { fontWeight: 600, color: '#f1f5f9', fontSize: 14 },
  productRef: { fontSize: 12, color: '#64748b' },
  removeBtn: { background: 'none', border: 'none', color: '#ef4444', cursor: 'pointer', fontSize: 18 },
  error: { color: '#f87171', fontSize: 13, marginBottom: 12 },
};

export default function ProductSetupPage() {
  const navigate = useNavigate();
  const [name, setName] = useState('');
  const [query, setQuery] = useState('');
  const [refPrice, setRefPrice] = useState('');
  const [products, setProducts] = useState([]);
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  const handleAdd = async () => {
    if (!name || !query || !refPrice) { setError('Preencha todos os campos.'); return; }
    setError('');
    setSaving(true);
    try {
      const { data } = await api.post('/api/v1/products', {
        name, mlQuery: query, referencePrice: Number(refPrice),
      });
      setProducts(p => [...p, data]);
      setName(''); setQuery(''); setRefPrice('');
    } catch (e) {
      setError(e.response?.data?.message || 'Erro ao cadastrar produto.');
    } finally { setSaving(false); }
  };

  const handleRemove = async (id) => {
    await api.delete(`/api/v1/products/${id}`);
    setProducts(p => p.filter(x => x.id !== id));
  };

  return (
    <div style={S.page}>
      <div style={S.card}>
        <h1 style={S.title}>🎯 Quais produtos você quer monitorar?</h1>
        <p style={S.subtitle}>
          Cadastre os produtos e defina o preço de referência.
          Você será alertado quando o preço total (c/ frete) ficar abaixo desse valor.
        </p>

        <label style={S.label}>Nome do produto</label>
        <input style={S.input} placeholder='Ex: Smart TV 55' value={name}
          onChange={e => setName(e.target.value)} />

        <label style={S.label}>Termo de busca (como pesquisar no Mercado Livre)</label>
        <input style={S.input} placeholder='Ex: smart tv 55 4k' value={query}
          onChange={e => setQuery(e.target.value)} />

        <label style={S.label}>Preço de referência (R$) — alerta abaixo deste valor</label>
        <input style={S.input} type='number' placeholder='Ex: 1800' value={refPrice}
          onChange={e => setRefPrice(e.target.value)} />

        {error && <p style={S.error}>{error}</p>}

        <button style={S.btnPrimary} onClick={handleAdd} disabled={saving}>
          {saving ? 'Salvando...' : '+ Adicionar produto'}
        </button>

        {products.length > 0 && (
          <div style={{ marginTop: 28 }}>
            <p style={{ color: '#94a3b8', fontSize: 13, marginBottom: 12 }}>
              Produtos cadastrados nesta sessão:
            </p>
            {products.map(p => (
              <div key={p.id} style={S.productItem}>
                <div>
                  <div style={S.productName}>{p.name}</div>
                  <div style={S.productRef}>Ref: R$ {Number(p.referencePrice).toFixed(2)}</div>
                </div>
                <button style={S.removeBtn} onClick={() => handleRemove(p.id)}>×</button>
              </div>
            ))}
            <button style={{ ...S.btnPrimary, marginTop: 16, width: '100%' }}
              onClick={() => navigate('/')}>
              Ir para o Dashboard →
            </button>
          </div>
        )}

        {products.length === 0 && (
          <button style={{ ...S.btnSecondary, marginLeft: 0, marginTop: 12 }}
            onClick={() => navigate('/')}>
            Pular por agora
          </button>
        )}
      </div>
    </div>
  );
}