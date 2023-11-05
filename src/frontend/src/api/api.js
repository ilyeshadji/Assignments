import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/Assignments',
});

api.interceptors.request.use(
    (config) => {
        config.headers['Customer-Role'] = JSON.parse(localStorage.getItem("user"));

        return config;
    },

    (error) => console.log(error)
);

export const formatParamsForURL = (params) => {
    return new URLSearchParams(params).toString();
}

export default api;
