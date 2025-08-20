import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

// Create axios instance
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle token refresh
api.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const originalRequest = error.config;

    // If error is 401 and we haven't already tried to refresh
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      const refreshToken = localStorage.getItem('refreshToken');
      if (refreshToken) {
        try {
          const refreshResponse = await axios.post(`${API_BASE_URL}/auth/refresh`, {
            refreshToken: refreshToken,
          });

          if (refreshResponse.data.success) {
            const { token, refreshToken: newRefreshToken } = refreshResponse.data.data;
            
            // Update stored tokens
            localStorage.setItem('token', token);
            localStorage.setItem('refreshToken', newRefreshToken);
            
            // Update the authorization header
            api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            originalRequest.headers.Authorization = `Bearer ${token}`;
            
            // Retry the original request
            return api(originalRequest);
          }
        } catch (refreshError) {
          // Refresh failed, redirect to login
          localStorage.removeItem('token');
          localStorage.removeItem('refreshToken');
          localStorage.removeItem('user');
          window.location.href = '/login';
        }
      } else {
        // No refresh token, redirect to login
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('user');
        window.location.href = '/login';
      }
    }

    return Promise.reject(error);
  }
);

export default api;
