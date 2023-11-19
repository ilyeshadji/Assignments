import {configureStore} from '@reduxjs/toolkit';

import authUserReducer from './user/authUserSlice';

export default configureStore({
    reducer: {
        authUser: authUserReducer,
    },
});
