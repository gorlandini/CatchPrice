import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axiosConfig';

const S = {
  page: { minHeight: '100vh', background: '#0f172a', color: '#f1f5f9', padding: '32px 24px' },
  header: { display: 'flex', alignItems: 'center', gap: 12, marginBottom: 24 },
  backBtn: {
    background: 'none', border: 'none', color: '#64748b',
    cursor: 'pointer', fontSize: 20, lineHeight: 1,
  },
  title: { fontSize: 22, fontWeight: 700, color: '#f1f5f9' },
  card: {
    background: '#1e293b', borderRadius: 12, padding: '16px 20px',
    marginBottom: 12, maxWidth: 600, margin: '0 auto 12px',
  },
  productName: { fontWeight: 600, fontSize: 15, marginBottom: 6 },
  row: { display: 'flex', justifyContent: 'space-between', fontSize: 13, color: '#94a3b8', marginBottom: 4 },
  total: { color: '#38bdf8', fontWeight: 700 },
  marketplace: { fontSize: 12, color: '#475569', marginTop: 4 },
  link: { color: '#38bdf8', fontSize: 13 },
  pagination: { display: 'flex', justifyContent: 'center', gap: 12, marginTop: 24, alignItems: 'center' },
  pageBtn: {
    padding: '8px 16px', background: '#1e293b', border: '1px solid #334155',
    borderRadius: 8, color: '#94a3b8', cursor: 'pointer',
  },
};

export default function AlertsPage() {
  const navigate = useNavigate();
  const [alerts, setAlerts] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);
    api.get(`/api/v1/alerts?page=${page}&size=10&sort=createdAt,desc`)
      .then(({ data }) => { setAlerts(data.content ?? []); setTotalPages(data.totalPages ?? 1); })
      .finally(() => setLoading(false));
  }, [page]);

  const fmt = (v) => `R$ ${Number(v).toFixed(2).replace('.', ',')}`;

  return (
    <div style={S.page}>
      <div style={{ maxWidth: 600, margin: '0 auto' }}>
        <div style={S.header}>
          <button style={S.backBtn} onClick={() => navigate('/')}>←</button>
          <h1 style={S.title}>🔔 Alertas de Preço</h1>
        </div>
        {loading && <p style={{ color: '#64748b', textAlign: 'center' }}>Carregando...</p>}
        {!loading && alerts.length === 0 && (
          <p style={{ color: '#64748b', textAlign: 'center', marginTop: 48 }}>
            Nenhum alerta ainda. Os alertas aparecem quando o preço cair abaixo do seu valor de referência.
          </p>
        )}
        {alerts.map(a => (
          <div key={a.id} style={S.card}>
            <div style={S.productName}>{a.productName}</div>
            <div style={S.row}>
              <span>Preço</span><span>{fmt(a.priceFound)}</span>
            </div>
            <div style={S.row}>
              <span>Frete</span><span>{fmt(a.shipping)}</span>
            </div>
            <div style={{ ...S.row, ...S.total }}>
              <span>Total</span><span>{fmt(a.totalPrice)}</span>
            </div>
            <div style={S.marketplace}>{a.marketplace}</div>
            <a href={a.sourceUrl} target='_blank' rel='noreferrer' style={S.link}>
              Ver produto →
            </a>
          </div>
        ))}
        {totalPages > 1 && (
          <div style={S.pagination}>
            <button style={S.pageBtn} onClick={() => setPage(p => p - 1)} disabled={page === 0}>←</button>
            <span style={{ color: '#64748b', fontSize: 13 }}>{page + 1} / {totalPages}</span>
            <button style={S.pageBtn} onClick={() => setPage(p => p + 1)} disabled={page >= totalPages - 1}>→</button>
          </div>
        )}
      </div>
    </div>
  );
}