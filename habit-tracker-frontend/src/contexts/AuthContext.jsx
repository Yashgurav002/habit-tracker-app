import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../services/api';

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // Initialize auth state from localStorage
  useEffect(() => {
    const token = localStorage.getItem('token');
    const refreshToken = localStorage.getItem('refreshToken');
    const userData = localStorage.getItem('user');

    if (token && userData) {
      try {
        const parsedUser = JSON.parse(userData);
        setUser(parsedUser);
        setIsAuthenticated(true);
        
        // Set the authorization header
        api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      } catch (error) {
        console.error('Error parsing user data:', error);
        // Clear localStorage
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('user');
        // Clear auth header
        delete api.defaults.headers.common['Authorization'];
      }
    }
    
    setLoading(false);
  }, []);

  const login = async (credentials) => {
    try {
      const response = await api.post('/auth/login', credentials);
      
      if (response.data.success) {
        const { token, refreshToken, id, username, email, firstName, lastName } = response.data.data;
        
        const userData = { id, username, email, firstName, lastName };
        
        // Store in localStorage
        localStorage.setItem('token', token);
        localStorage.setItem('refreshToken', refreshToken);
        localStorage.setItem('user', JSON.stringify(userData));
        
        // Set auth header
        api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        
        // Update state
        setUser(userData);
        setIsAuthenticated(true);
        
        return { success: true };
      } else {
        throw new Error(response.data.message || 'Login failed');
      }
    } catch (error) {
      const message = error.response?.data?.message || error.message || 'Login failed';
      return { success: false, error: message };
    }
  };

  const register = async (userData) => {
    try {
      const response = await api.post('/auth/register', userData);
      
      if (response.data.success) {
        const { token, refreshToken, id, username, email, firstName, lastName } = response.data.data;
        
        const userInfo = { id, username, email, firstName, lastName };
        
        // Store in localStorage
        localStorage.setItem('token', token);
        localStorage.setItem('refreshToken', refreshToken);
        localStorage.setItem('user', JSON.stringify(userInfo));
        
        // Set auth header
        api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        
        // Update state
        setUser(userInfo);
        setIsAuthenticated(true);
        
        return { success: true };
      } else {
        throw new Error(response.data.message || 'Registration failed');
      }
    } catch (error) {
      const message = error.response?.data?.message || error.message || 'Registration failed';
      return { success: false, error: message };
    }
  };

  const logout = () => {
    // Clear localStorage
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
    
    // Clear auth header
    delete api.defaults.headers.common['Authorization'];
    
    // Update state
    setUser(null);
    setIsAuthenticated(false);
  };

  const refreshToken = async () => {
    try {
      const refreshToken = localStorage.getItem('refreshToken');
      if (!refreshToken) {
        logout();
        return false;
      }

      const response = await api.post('/auth/refresh', { refreshToken });
      
      if (response.data.success) {
        const { token, refreshToken: newRefreshToken } = response.data.data;
        
        // Update tokens
        localStorage.setItem('token', token);
        localStorage.setItem('refreshToken', newRefreshToken);
        
        // Update auth header
        api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        
        return true;
      } else {
        logout();
        return false;
      }
    } catch (error) {
      console.error('Token refresh failed:', error);
      logout();
      return false;
    }
  };

  const value = {
    user,
    loading,
    isAuthenticated,
    login,
    register,
    logout,
    refreshToken,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
