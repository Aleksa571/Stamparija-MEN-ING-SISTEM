import { api } from './client';

export const authApi = {
  login: (username, password) => api.post('/api/auth/login', { username, password }, { _skipAuth: true }),
  register: (data) => api.post('/api/auth/register', data, { _skipAuth: true }),
  refresh: (refreshToken) => api.post('/api/auth/refresh', { refreshToken }, { _skipAuth: true }),
  logout: () => api.post('/api/auth/logout'),
  me: () => api.get('/api/auth/me'),
};

export const categoryApi = {
  list: () => api.get('/api/categories'),
  get: (id) => api.get(`/api/categories/${id}`),
  create: (data) => api.post('/api/categories', data),
  update: (id, data) => api.put(`/api/categories/${id}`, data),
  delete: (id) => api.delete(`/api/categories/${id}`),
};

export const productApi = {
  list: (params) => api.get('/api/products', { params }),
  get: (id) => api.get(`/api/products/${id}`),
  create: (data) => api.post('/api/products', data),
  update: (id, data) => api.put(`/api/products/${id}`, data),
  delete: (id) => api.delete(`/api/products/${id}`),
};

export const orderApi = {
  create: (data) => api.post('/api/orders', data),
  myOrders: () => api.get('/api/orders/my'),
  list: () => api.get('/api/orders'),
  get: (id) => api.get(`/api/orders/${id}`),
  updateStatus: (id, status) => api.patch(`/api/orders/${id}/status`, { status }),
  delete: (id) => api.delete(`/api/orders/${id}`),
};

export const blogApi = {
  list: () => api.get('/api/blog'),
  get: (id) => api.get(`/api/blog/${id}`),
  create: (data) => api.post('/api/blog', data),
  update: (id, data) => api.put(`/api/blog/${id}`, data),
  delete: (id) => api.delete(`/api/blog/${id}`),
};

export const userApi = {
  list: () => api.get('/api/users'),
  get: (id) => api.get(`/api/users/${id}`),
  setEnabled: (id, enabled) => api.patch(`/api/users/${id}/enabled`, { enabled }),
  delete: (id) => api.delete(`/api/users/${id}`),
};
