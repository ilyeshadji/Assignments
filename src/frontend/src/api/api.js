import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/Assignment1',
    headers: {}
});

api.interceptors.request.use(
    (config) => {
        const user = JSON.parse(localStorage.getItem("user"))

        console.log(user.role);
        config.headers['Customer-Role'] = user.role

        return config;
    },

    (error) => console.log(error)
);

export default api;
