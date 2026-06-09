import { useState, useEffect } from 'react';
import api from '../api/axiosConfig';

const S = {
  title: { fontSize: 22, fontWeight: 700, color: '#f1f5f9', marginBottom: 24 },
  cardGreen: {
    background: '#052e16', border: '1px solid #16a34a', borderRadius: 12,
    padding: '20px 24px', marginBottom: 16,
  },
  cardOrange: {
    background: '#431407', border: '1px solid #ea580c', borderRadius: 12,
    padding: '20px 24px', marginBottom: 16,
  },
  cardTitle: { fontSize: 15, fontWeight: 600, marginBottom: 12 },
  row: { display: 'flex', justifyContent: 'space-between', fontSize: 14, color: '#94a3b8', marginBottom: 6 },
  value: { color: '#f1f5f9', fontWeight: 600 },
  roi: { fontSize: 24, fontWeight: 700, color: '#4ade80' },
  table: { width: '100%', borderCollapse: 'collapse', marginTop: 24, fontSize: 13 },
  th: { textAlign: 'left', color: '#64748b', padding: '8px 12px', borderBottom: '1px solid #334155' },
  td: { padding: '10px 12px', borderBottom: '1px solid #1e293b', color: '#94a3b8' },
  emptyState: { textAlign: 'center', color: '#475569', padding: '48px 0' },
};

export default function SavingsDashboard() {
  const [savings, setSavings] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/api/v1/savings')
      .then(({ data }) => setSavings(data))
      .finally(() => setLoading(false));
  }, []);

  const fmt = (v) => v != null ? `R$ ${Number(v).toFixed(2).replace('.', ',')}` : 'R$ 0,00';
  const current = savings[0];
  const roi = current ? (Number(current.totalSaved) / 10).toFixed(0) : 0;

  if (loading) return <p style={{ color: '#64748b' }}>Carregando...</p>;

  if (!current) return (
    <div style={S.emptyState}>
      <p style={{ fontSize: 40 }}>📭</p>
      <p>Nenhum alerta ainda este mês.</p>
      <p style={{ fontSize: 13 }}>Os dados aparecem conforme os preços são verificados.</p>
    </div>
  );

  return (
    <div>
      <h1 style={S.title}>Sua Economia</h1>
      <div style={S.cardGreen}>
        <div style={S.cardTitle}>✅ Economia este mês</div>
        <div style={S.row}><span>Total economizado</span><span style={S.value}>{fmt(current.totalSaved)}</span></div>
        <div style={S.row}><span>Menor preço encontrado (c/ frete)</span><span style={S.value}>{fmt(current.lowestTotalFound)}</span></div>
        <div style={S.row}><span>Menor frete</span><span style={S.value}>{fmt(current.lowestShippingFound)}</span></div>
        <div style={S.row}><span>Alertas recebidos</span><span style={S.value}>{current.alertsCount}</span></div>
        <div style={{ marginTop: 12 }}>
          <span style={{ color: '#94a3b8', fontSize: 13 }}>Retorno sobre assinatura: </span>
          <span style={S.roi}>{roi}x 🚀</span>
        </div>
        <p style={{ fontSize: 12, color: '#4ade80', marginTop: 8 }}>
          * Considera preço + frete para o seu CEP.
        </p>
      </div>
      <table style={S.table}>
        <thead>
          <tr>
            <th style={S.th}>Mês</th>
            <th style={S.th}>Economia</th>
            <th style={S.th}>Alertas</th>
            <th style={S.th}>Melhor desconto</th>
          </tr>
        </thead>
        <tbody>
          {savings.map(s => (
            <tr key={s.month}>
              <td style={S.td}>{new Date(s.month).toLocaleDateString('pt-BR', { month: 'long', year: 'numeric' })}</td>
              <td style={S.td}>{fmt(s.totalSaved)}</td>
              <td style={S.td}>{s.alertsCount}</td>
              <td style={S.td}>{fmt(s.bestDeal)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );