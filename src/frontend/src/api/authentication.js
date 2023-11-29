import api, { formatParamsForURL } from './api.js';

// eslint-disable-next-line import/no-anonymous-default-export
export default {
    async login(password) {
        return api.post(`/authentication/login?${formatParamsForURL({ password })}`);
    },
    async signup(password, role) {
        return api.post(`/authentication/signup?${formatParamsForURL({ password, role })}`);
    }
};
