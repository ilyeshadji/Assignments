import Toaster from '../plugin/Toaster';

export function showBackendError(e) {
    const error = e.response.data;
    Toaster.error(error);
}
