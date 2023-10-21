import api from './api.js';

// eslint-disable-next-line import/no-anonymous-default-export
export default {
    async createProduct(productData) {
        return api.post(`/product/create`, {
            params: productData
        });
    },
    async updateProduct(productData, productId) {
        return api.put(`/product/${productId}`, {
            params: productData
        });
    },
    async getProductBySlug(productSlug) {
        return api.get(`/product/${productSlug}`);
    },
    async getProductBySku(productSku) {
        return api.get(`/product/${productSku}`);
    },
    async getProductList() {
        return api.get(`/product`);
    },
};
