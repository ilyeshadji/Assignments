import api from './api.js';

// eslint-disable-next-line import/no-anonymous-default-export
export default {
    async initialization() {
        return api.post(`/database-initialization`);
    },
};
