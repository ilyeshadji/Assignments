import {createSlice} from '@reduxjs/toolkit';

export const authUserSlice = createSlice({
    name: 'authUser',
    initialState: {
        user_id: null,
        role: 'customer',
        isLoggedIn: !!JSON.parse(localStorage.getItem("user")).isLoggedIn,
        token: null,
    },
    reducers: {
        authUserSet(state, action) {
            localStorage.setItem("user", JSON.stringify(action.payload));

            state.isLoggedIn = !!action.payload.token;
            state.role = action.payload.user.role;
            state.user_id = action.payload.user.user_id;
            state.token = action.payload.token;
        },
        authUserUnset(state) {
            localStorage.removeItem("user");

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
