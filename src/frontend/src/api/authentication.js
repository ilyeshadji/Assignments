import api, {formatParamsForURL} from './api.js';

// eslint-disable-next-line import/no-anonymous-default-export
export default {
    async login(email, password) {
        return api.post(`/authentication/login?${formatParamsForURL({email, password})}`);
    },
    async signup(email, password) {
        return api.post(`/authentication/signup?${formatParamsForURL({email, password})}`);
    }
};
