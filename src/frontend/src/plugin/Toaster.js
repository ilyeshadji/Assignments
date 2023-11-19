import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

export default class Toaster {
  static warning(label, options) {
    toast.warn(label, {
      position: options ? options.position : 'top-right',
      autoClose: options ? options.autoClose : 10000,
      hideProgressBar: options ? options.hideProgressBar : false,
      closeOnClick: options ? options.closeOnClick : true,
      pauseOnHover: options ? options.pauseOnHover : true,
      draggable: options ? options.draggable : true,
      progress: options ? options.progress : undefined,
    });
  }

  static error(label, options) {
    toast.error(label, {
      position: 'bottom-right',
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
      theme: 'colored',
    });
  }

  static info(label, options) {
    toast.info(label, {
      position: options ? options.position : 'top-right',
      autoClose: options ? options.autoClose : 10000,
      hideProgressBar: options ? options.hideProgressBar : false,
      closeOnClick: options ? options.closeOnClick : true,
      pauseOnHover: options ? options.pauseOnHover : true,
      draggable: options ? options.draggable : true,
      progress: options ? options.progress : undefined,
    });
  }

  static success(label, options) {
    toast.success(label, {
      position: options ? options.position : 'top-right',
      autoClose: options ? options.autoClose : 10000,
      hideProgressBar: options ? options.hideProgressBar : false,
      closeOnClick: options ? options.closeOnClick : true,
      pauseOnHover: options ? options.pauseOnHover : true,
      draggable: options ? options.draggable : true,
      progress: options ? options.progress : undefined,
    });
  }
}
