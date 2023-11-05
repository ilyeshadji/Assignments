import Toaster from '../plugin/Toaster';

export function showBackendError(e) {
    let error;
    if (e.response?.data) {
        error = e.response.data;
    } else {
        error = "There is an issue with the server. Try again later."
    }

    Toaster.error(error);
}
