import axios from 'axios';

const axiosClient = axios.create({
    baseURL: 'http://localhost:8080/api',
});

// Add a response interceptor for centralized error handling
axiosClient.interceptors.response.use(
    response => response,
    error => {
        // If a global toast is available (set in main.js), use it; otherwise fallback to alert.
        if (window.$toast) {
            window.$toast.error(error.response?.data?.message || 'An unexpected error occurred');
        } else {
            alert(error.response?.data?.message || 'An unexpected error occurred');
        }
        return Promise.reject(error);
    }
);

export default axiosClient;
