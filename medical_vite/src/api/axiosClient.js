import axios from 'axios';

const axiosClient = axios.create({
    baseURL: 'http://localhost:8080/api',
});


axiosClient.interceptors.response.use(
    response => response,
    error => {

        if (window.$toast) {
            window.$toast.error(error.response?.data?.message || 'An unexpected error occurred');
        } else {
            alert(error.response?.data?.message || 'An unexpected error occurred');
        }
        return Promise.reject(error);
    }
);

export default axiosClient;
