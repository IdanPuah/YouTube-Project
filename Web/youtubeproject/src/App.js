import React from 'react';
import { AuthProvider } from './auth/AuthContext.js';
import AppRoutes from './AppRoutes.js';
import './App.css';

function App() {
  return (
    <AuthProvider>
      <AppRoutes />
    </AuthProvider>
  );
}

export default App;
