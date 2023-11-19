import React, {useEffect, useState} from 'react';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {ToastContainer} from "react-toastify";
import styled from 'styled-components';

import AccessRoute from "./components/navigation/AccessRoute";
import GlobalStyles from "./utils/GlobalStyles";
import NavBar from "./components/navigation/NavBar";
import Home from "./pages/Home";
import Product from "./pages/Product";
import ProductList from "./pages/ProductList";
import Cart from "./pages/Cart";
import CreateProduct from "./pages/CreateProduct";
import {databaseApi} from "./api";
import {showBackendError} from "./utils/utils";
import {useDispatch} from "react-redux";
import Login from "./pages/Login";
import {authUserSet, authUserUnset} from "./store/user/authUserSlice";
import Orders from "./pages/Orders";

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
            <NavBar/>
            <GlobalStyles/>
            <ToastContainer/>
            {!isLoading && (
                <AccessRoute>
                    <RoutesWrapper>
                        <Route exact path="/" element={<Home/>}/>
                        <Route exact path="/product/:sku" element={<Product/>}/>
                        <Route exact path="/products" element={<ProductList/>}/>
                        <Route exact path="/login" element={<Login/>}/>
                        <Route exact path="/cart" element={<Cart/>}/>
                        <Route exact path="/product/create" element={<CreateProduct/>}/>
                        <Route exact path="/orders" element={<Orders/>}/>
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


