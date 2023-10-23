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
import {databaseApi, productApi} from "./api";
import {showBackendError} from "./utils/utils";
import {selectUserRole} from "./store/selectors";
import {useSelector} from "react-redux";

function App() {
    const user = useSelector(selectUserRole);

    if (!user) {
        localStorage.setItem("user", JSON.stringify('customer'));
    }

    if (user === 'customer') {
        localStorage.setItem("user", JSON.stringify('customer'));
    } else {
        console.log('yo2');
    }

    useEffect(() => {
        (async () => {
            try {
                await databaseApi.initialization();
            } catch (e) {
                showBackendError(e);
            }
        })();
    }, []);

    return (
        <BrowserRouter>

            <NavBar/>
            <GlobalStyles/>
            <ToastContainer/>
            <AccessRoute>
                <RoutesWrapper>
                    <Route exact path="/Assignment1/index.jsp" element={<Home/>}/>
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
