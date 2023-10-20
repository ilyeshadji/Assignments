import React from 'react';
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

function App() {

    const user = JSON.parse(localStorage.getItem("user"));

    if (!user) {
        localStorage.setItem("user", JSON.stringify({role: 'customer'}));
    }

    return (
        <BrowserRouter>

            <NavBar/>
            <GlobalStyles/>
            <ToastContainer/>
            <AccessRoute>
                <RoutesWrapper>
                    <Route exact path="/" element={<Home/>}/>
                    <Route exact path="/product/:sku" element={<Product/>}/>
                    <Route exact path="/products" element={<ProductList/>}/>
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

const PageContainer = styled.div`
  display: flex;
`;
