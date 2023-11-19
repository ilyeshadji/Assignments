import {createSlice} from '@reduxjs/toolkit';

export const authUserSlice = createSlice({
    name: 'authUser',
    initialState: {
        user_id: null,
        role: 'customer',
        isLoggedIn: false,
        token: null,
    },
    reducers: {
        authUserSet(state, action) {
            localStorage.setItem("user", JSON.stringify(action.payload));

            state.isLoggedIn = !!action.payload.token;
            state.role = action.payload.role;
            state.user_id = action.payload.user_id;
            state.token = action.payload.token;
        },
        authUserUnset(state) {
            const user = {
                user_id: null,
                role: 'customer',
                isLoggedIn: false,
                token: null
            };

            localStorage.setItem("user", JSON.stringify(user));

            state.role = 'customer';
            state.isLoggedIn = false;
            state.user_id = null;
            state.token = null;
        },
    },
});

export const {
    authUserSet,
    authUserUnset,
} = authUserSlice.actions;

export default authUserSlice.reducer;
