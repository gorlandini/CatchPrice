import { createContext, useContext, useState, useCallback } from 'react';
import api from '../api/axiosConfig';
const AuthContext = createContext(null);
export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const login = useCallback(async (email, password) => {
    const { data } = await api.post('/api/v1/auth/login', { email, password });
    localStorage.setItem('token', data.token); setUser(data);
  }, []);
  const logout = useCallback(() => { localStorage.removeItem('token'); setUser(null); }, []);
  return <AuthContext.Provider value={{ user, login, logout }}>{children}</AuthContext.Provider>;
}
export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be inside AuthProvider');
  return ctx;
}