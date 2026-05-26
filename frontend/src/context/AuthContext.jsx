import { createContext, useContext, useEffect, useState } from 'react';
import { authApi } from '../api/services';
import { clearTokens, setTokens, USER_KEY, getAccessToken } from '../api/client';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    const raw = localStorage.getItem(USER_KEY);
    return raw ? JSON.parse(raw) : null;
  });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const onLogout = () => setUser(null);
    const onRefreshed = (e) => {
      const d = e.detail;
      setUser({
        userId: d.userId,
        username: d.username,
        email: d.email,
        firstName: d.firstName,
        lastName: d.lastName,
        roles: d.roles,
      });
    };
    window.addEventListener('auth:logout', onLogout);
    window.addEventListener('auth:refreshed', onRefreshed);
    return () => {
      window.removeEventListener('auth:logout', onLogout);
      window.removeEventListener('auth:refreshed', onRefreshed);
    };
  }, []);

  const persistAuth = (data) => {
    setTokens(data.accessToken, data.refreshToken);
    const u = {
      userId: data.userId,
      username: data.username,
      email: data.email,
      firstName: data.firstName,
      lastName: data.lastName,
      roles: data.roles,
    };
    localStorage.setItem(USER_KEY, JSON.stringify(u));
    setUser(u);
    return u;
  };

  const login = async (username, password) => {
    setLoading(true);
    try {
      const { data } = await authApi.login(username, password);
      return persistAuth(data);
    } finally {
      setLoading(false);
    }
  };

  const register = async (formData) => {
    setLoading(true);
    try {
      const { data } = await authApi.register(formData);
      return persistAuth(data);
    } finally {
      setLoading(false);
    }
  };

  const logout = async () => {
    try {
      if (getAccessToken()) await authApi.logout();
    } catch (e) {
      // ignorisi
    }
    clearTokens();
    setUser(null);
  };

  const isAdmin = !!user?.roles?.includes('ROLE_ADMIN');
  const isAuthenticated = !!user;

  return (
    <AuthContext.Provider value={{ user, login, register, logout, loading, isAdmin, isAuthenticated }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth mora biti u AuthProvider');
  return ctx;
};
