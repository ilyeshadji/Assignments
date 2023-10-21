import api, {formatParamsForURL} from './api.js';

// eslint-disable-next-line import/no-anonymous-default-export
export default {
    async addToCart(user_id, product_id, quantity) {
        return api.post(`/cart?${formatParamsForURL({user_id, product_id, quantity})}`);
    },
    async removeFromCart(user_id, product_id) {
        console.log(formatParamsForURL({user_id, product_id}))
        return api.delete(`/cart?${formatParamsForURL({user_id, product_id})}`);
    },
    async getCart(user_id) {
        return api.get(`/cart?${formatParamsForURL(user_id)}`);
    }
};
