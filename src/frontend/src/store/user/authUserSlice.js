import {createSlice} from '@reduxjs/toolkit';

export const authUserSlice = createSlice({
    name: 'authUser',
    initialState: {
        role: 'customer',
        isLoggedIn: !!JSON.parse(localStorage.getItem("user")).isLoggedIn
    },
    reducers: {
        authUserSet(state, action) {
            localStorage.setItem("user", JSON.stringify(action.payload));

            state.isLoggedIn = !!action.payload.token;
            state.role = action.payload;
        },
        authUserUnset(state) {
            localStorage.removeItem("user");
            
            state.role = 'customer';
            state.isLoggedIn = false;
        },
    },
});

export const {
    authUserSet,
    authUserUnset,
} = authUserSlice.actions;

export default authUserSlice.reducer;
