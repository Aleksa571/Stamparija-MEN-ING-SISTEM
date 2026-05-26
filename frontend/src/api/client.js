import axios from 'axios';

const BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

export const api = axios.create({
  baseURL: BASE_URL,
  headers: { 'Content-Type': 'application/json' },
});

export const TOKEN_KEY = 'stamparija_access_token';
export const REFRESH_KEY = 'stamparija_refresh_token';
export const USER_KEY = 'stamparija_user';

export const setTokens = (accessToken, refreshToken) => {
  localStorage.setItem(TOKEN_KEY, accessToken);
  localStorage.setItem(REFRESH_KEY, refreshToken);
};

export const clearTokens = () => {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(REFRESH_KEY);
  localStorage.removeItem(USER_KEY);
};

export const getAccessToken = () => localStorage.getItem(TOKEN_KEY);
export const getRefreshToken = () => localStorage.getItem(REFRESH_KEY);

api.interceptors.request.use((config) => {
  const token = getAccessToken();
  if (token && !config._skipAuth) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

let isRefreshing = false;
let refreshQueue = [];

const processQueue = (error, token = null) => {
  refreshQueue.forEach((p) => {
    if (error) p.reject(error);
    else p.resolve(token);
  });
  refreshQueue = [];
};

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const original = error.config;
    if (!error.response) return Promise.reject(error);

    const isAuthEndpoint = original.url?.includes('/api/auth/login') ||
                          original.url?.includes('/api/auth/register') ||
                          original.url?.includes('/api/auth/refresh');

    if (error.response.status === 401 && !original._retry && !isAuthEndpoint) {
      const refresh = getRefreshToken();
      if (!refresh) {
        clearTokens();
        window.dispatchEvent(new Event('auth:logout'));
        return Promise.reject(error);
      }

      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          refreshQueue.push({ resolve, reject });
        })
          .then((token) => {
            original.headers.Authorization = `Bearer ${token}`;
            return api(original);
          })
          .catch((err) => Promise.reject(err));
      }

      original._retry = true;
      isRefreshing = true;
      try {
        const { data } = await axios.post(
          `${BASE_URL}/api/auth/refresh`,
          { refreshToken: refresh },
          { headers: { 'Content-Type': 'application/json' } }
        );
        setTokens(data.accessToken, data.refreshToken);
        localStorage.setItem(USER_KEY, JSON.stringify({
          userId: data.userId,
          username: data.username,
          email: data.email,
          firstName: data.firstName,
          lastName: data.lastName,
          roles: data.roles,
        }));
        processQueue(null, data.accessToken);
        original.headers.Authorization = `Bearer ${data.accessToken}`;
        window.dispatchEvent(new CustomEvent('auth:refreshed', { detail: data }));
        return api(original);
      } catch (refreshError) {
        processQueue(refreshError, null);
        clearTokens();
        window.dispatchEvent(new Event('auth:logout'));
        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    }
    return Promise.reject(error);
  }
);

export const apiUrl = (path) => `${BASE_URL}${path}`;

export const resolveImage = (relativePath) => {
  if (!relativePath) return '/placeholder.png';
  if (relativePath.startsWith('http')) return relativePath;
  return `${BASE_URL}${relativePath}`;
};
