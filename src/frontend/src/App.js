import React, {useEffect} from 'react';
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
import {selectUser} from "./store/selectors";
import {useDispatch, useSelector} from "react-redux";
import Login from "./pages/Login";
import {authUserSet, authUserUnset} from "./store/user/authUserSlice";

function App() {
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

        if (!user?.id) {
            dispatch(authUserUnset);
        }
        if (user.user_id) {
            dispatch(authUserSet(user));
        }

    }, [dispatch, user]);

    return (
        <BrowserRouter>

            <NavBar/>
            <GlobalStyles/>
            <ToastContainer/>
            <AccessRoute>
                <RoutesWrapper>
                    <Route exact path="/Assignments/index.jsp" element={<Home/>}/>
                    <Route exact path="/product/:sku" element={<Product/>}/>
                    <Route exact path="/products" element={<ProductList/>}/>
                    <Route exact path="/login" element={<Login/>}/>
                    <Route exact path="/cart" element={<Cart/>}/>
                    <Route exact path="/product/create" element={<CreateProduct/>}/>
                </RoutesWrapper>
            </AccessRoute>
        </BrowserRouter>
    );
}

export default App;

const RoutesWrapper = styled(Routes)`
  display: flex;
  flex: 1;
`;


