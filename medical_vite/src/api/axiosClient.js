import axios from 'axios';

// Adjust baseURL to match your Spring Boot server
// If the back-end is on localhost:8080, do:
const axiosClient = axios.create({
    baseURL: 'http://localhost:8080/api',
});

export default axiosClient;
