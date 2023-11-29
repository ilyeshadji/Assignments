import React, { useEffect, useState } from 'react';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import styled from 'styled-components';

import { useDispatch } from "react-redux";
import { databaseApi } from "./api";
import AccessRoute from "./components/navigation/AccessRoute";
import NavBar from "./components/navigation/NavBar";
import Cart from "./pages/Cart";
import ClaimOrder from './pages/ClaimOrder';
import CreateOrder from './pages/CreateOrder';
import CreateProduct from "./pages/CreateProduct";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Orders from "./pages/Orders";
import Product from "./pages/Product";
import ProductList from "./pages/ProductList";
import StaffCreateAccount from './pages/StaffCreateAccount';
import UpdatePassword from './pages/UpdatePassword';
import UserManagement from './pages/UserManagement';
import { authUserSet, authUserUnset } from "./store/user/authUserSlice";
import GlobalStyles from "./utils/GlobalStyles";
import { showBackendError } from "./utils/utils";

function App() {
    const [isLoading, setIsLoading] = useState(true);
    const dispatch = useDispatch();
    const user = JSON.parse(localStorage.getItem("user"));

    useEffect(() => {
        (async () => {
            try {
                await databaseApi.initialization();
            } catch (e) {
                showBackendError(e);
            }
        })();


        if (!user?.user_id) {
            dispatch(authUserUnset);
        } else if (user.user_id) {
            dispatch(authUserSet(user));
        }
        setIsLoading(false);
    }, [dispatch, user]);

    return (
        <BrowserRouter>
            <NavBar />
            <GlobalStyles />
            <ToastContainer />
            {!isLoading && (
                <AccessRoute>
                    <RoutesWrapper>
                        <Route exact path="/" element={<Home />} />
                        <Route exact path="/product/:sku" element={<Product />} />
                        <Route exact path="/products" element={<ProductList />} />
                        <Route exact path="/login" element={<Login />} />
                        <Route exact path="/cart" element={<Cart />} />
                        <Route exact path="/product/create" element={<CreateProduct />} />
                        <Route exact path="/orders" element={<Orders />} />
                        <Route exact path="/order/create" element={<CreateOrder />} />
                        <Route exact path="/staff/create-account" element={<StaffCreateAccount />} />
                        <Route exact path="/orders/claim" element={<ClaimOrder />} />
                        <Route exact path="/update-password" element={<UpdatePassword />} />
                        <Route exact path="/user-management" element={<UserManagement />} />
                    </RoutesWrapper>
                </AccessRoute>
            )}
        </BrowserRouter>
    );
}

export default App;

const RoutesWrapper = styled(Routes)`
  display: flex;
  flex: 1;
`;


