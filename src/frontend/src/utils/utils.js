import Toaster from '../plugin/Toaster';
import {emailRegex, passwordRegex, skuRegex} from "./Regex";

export function showBackendError(e) {
    let error;
    if (e.response?.data) {
        error = e.response.data;
    } else {
        error = "There is an issue with the server. Try again later."
    }

    Toaster.error(error);
}

export function isEmailValid(email) {
    return emailRegex.test(email);
}

export function isPasswordValid(password) {
    return passwordRegex.test(password);
}

export function isSkuValid(sku) {
    return skuRegex.test(sku);
}
