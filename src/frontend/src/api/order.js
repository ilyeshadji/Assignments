import api, {formatParamsForURL} from './api.js';

// eslint-disable-next-line import/no-anonymous-default-export
export default {
    async createOrder(user_id, shipping_address) {
        return api.post(`/orders/customer?${formatParamsForURL({user_id, shipping_address})}`);
    },
    async getOrdersForCustomer(user_id) {
        return api.get(`/orders/customer?${formatParamsForURL({user_id})}`);
    },
    async getAllOrders() {
        return api.get(`/orders`);
    },
    async getOrder(order_id) {
        return api.get(`/orders/order?${formatParamsForURL({order_id})}`);

    },
    async ship(order_id) {
        return api.post(`/orders/ship?${formatParamsForURL({order_id})}`);
    },
};
