import axios from 'axios';

// In production (Vercel), VITE_API_URL must be set to the Railway backend URL.
// In local development, requests fall back to http://localhost:8081/api which
// matches the Vite dev-server proxy target defined in vite.config.js.
const BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8081/api';

const api = axios.create({
    baseURL: BASE_URL,
    headers: { 'Content-Type': 'application/json' },
    timeout: 15000,
});

api.interceptors.request.use((config) => {
    const token = localStorage.getItem('viakids_token_v3');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
}, (error) => Promise.reject(error));

api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            localStorage.removeItem('viakids_token_v3');
            localStorage.removeItem('viakids_role_v3');
            localStorage.removeItem('viakids_name_v3');
            window.location.href = '/';
        }
        return Promise.reject(error);
    }
);

export default api;
