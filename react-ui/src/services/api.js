import axios from 'axios';

// Base API URL - Change this to match your backend
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

// Create axios instance
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor to include user ID in headers
api.interceptors.request.use(
  (config) => {
    const user = JSON.parse(localStorage.getItem('currentUser') || 'null');
    if (user && user.userId) {
      config.headers['X-User-Id'] = user.userId;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// ==================== AUTH API ====================

export const authAPI = {
  login: (username, password) =>
    api.post('/auth/login', { username, password }),

  register: (name, email, username, password) =>
    api.post('/auth/register', { name, email, username, password }),

  logout: () =>
    api.post('/auth/logout', {}),
};

// ==================== USER API ====================

export const userAPI = {
  getAllUsers: () =>
    api.get('/users'),

  getUserById: (id) =>
    api.get(`/users/${id}`),

  createUser: (userData) =>
    api.post('/users', userData),

  updateUser: (id, userData) =>
    api.put(`/users/${id}`, userData),

  deleteUser: (id) =>
    api.delete(`/users/${id}`),
};

// ==================== TASK/TODO ITEM API ====================

export const taskAPI = {
  getAllTasks: (userId, status) => {
    let url = '/items';
    const params = new URLSearchParams();

    if (userId) params.append('userId', userId);
    if (status) params.append('status', status);

    if (params.toString()) {
      url += `?${params.toString()}`;
    }

    return api.get(url);
  },

  getTaskById: (id) =>
    api.get(`/items/${id}`),

  createTask: (taskData) =>
    api.post('/items', taskData),

  updateTask: (id, taskData) =>
    api.put(`/items/${id}`, taskData),

  updateTaskStatus: (id, status) =>
    api.patch(`/items/${id}/status?status=${status}`, {}),

  toggleTaskComplete: (id) =>
    api.patch(`/items/${id}/toggle`, {}),

  deleteTask: (id) =>
    api.delete(`/items/${id}`),
};

// ==================== SUBTASK API ====================

export const subtaskAPI = {
  getSubtasks: (taskId) =>
    api.get(`/items/${taskId}/subtasks`),

  getSubtaskById: (taskId, subtaskId) =>
    api.get(`/items/${taskId}/subtasks/${subtaskId}`),

  createSubtask: (taskId, subtaskData) =>
    api.post(`/items/${taskId}/subtasks`, subtaskData),

  updateSubtask: (taskId, subtaskId, subtaskData) =>
    api.put(`/items/${taskId}/subtasks/${subtaskId}`, subtaskData),

  toggleSubtaskComplete: (taskId, subtaskId) =>
    api.patch(`/items/${taskId}/subtasks/${subtaskId}/toggle`, {}),

  deleteSubtask: (taskId, subtaskId) =>
    api.delete(`/items/${taskId}/subtasks/${subtaskId}`),
};

export default api;
