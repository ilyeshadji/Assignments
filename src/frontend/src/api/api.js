import axios from 'axios';

const api = axios.create({
    baseURL: process.env.REACT_APP_BACKEND_URL,
});

api.interceptors.request.use(
    (config) => {
        config.headers['Customer-Role'] = JSON.parse(localStorage.getItem("user"));
        config.headers['Authorization'] = `Bearer ${JSON.parse(localStorage.getItem("user")).token}`
        return config;
    },

    (error) => console.log(error)
);

export const formatParamsForURL = (params) => {
    return new URLSearchParams(params).toString();
}

export default api;
