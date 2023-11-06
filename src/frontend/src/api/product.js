import api, {formatParamsForURL} from './api.js';

// eslint-disable-next-line import/no-anonymous-default-export
export default {
    async createProduct(productData) {
        return api.post(`/product/create?${formatParamsForURL(productData)}`);
    },
    async updateProduct(productData, productId) {
        return api.put(`/product/${productId}?${formatParamsForURL(productData)}`);
    },
    async getProductBySlug(productSlug) {
        return api.get(`/product/${productSlug}`);
    },
    async getProductBySku(productSku) {
        return api.get(`/product/${productSku}`);
    },
    async getProductList() {
        return api.get(`/product-list`);
    },
};
