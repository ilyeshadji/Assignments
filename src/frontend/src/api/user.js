import api, { formatParamsForURL } from './api.js';

// eslint-disable-next-line import/no-anonymous-default-export
export default {
    async getUsers() {
        return api.get(`/users`);
    },
    async changeRole(user_id, role) {
        return api.put(`/user?${formatParamsForURL({ user_id, role })}`);
    }
};
