import { useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import LoginPage from './pages/LoginPage';
import ProductSetupPage from './pages/ProductSetupPage';
import Dashboard from './pages/Dashboard';
import AlertsPage from './pages/AlertsPage';

function AppRoutes() {
  const { user, isFirstLogin } = useAuth();
  if (!user) return <LoginPage />;
  if (isFirstLogin) return <ProductSetupPage />;
  return (
    <BrowserRouter>
      <Routes>
        <Route path='/' element={<Dashboard />} />
        <Route path='/alerts' element={<AlertsPage />} />
        <Route path='/products' element={<ProductSetupPage />} />
        <Route path='*' element={<Navigate to='/' />} />
      </Routes>
    </BrowserRouter>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <AppRoutes />
    </AuthProvider>
  );
}