import React, {useEffect, useState} from 'react';
import styled from 'styled-components';
import {cartApi} from "../api";
import {showBackendError} from "../utils/utils";
import PageContainer from "../components/UI/PageContainer";
import {Grid} from "@mui/material";
import {AiFillDelete} from "react-icons/ai";
import Toaster from "../plugin/Toaster";
import ErrorPage from "./ErrorPage";

function Cart() {
    const [products, setProducts] = useState([]);

    useEffect(() => {
        (async () => {
            try {
                const response = await cartApi.getCart(1);
                setProducts(response.data);
            } catch (e) {
                showBackendError(e);
            }
        })();
    }, []);

    async function deleteItem(product) {
        try {
            await cartApi.removeFromCart(1, product.sku);
            Toaster.success("Successfully removed item from cart!")
        } catch (e) {
            showBackendError(e);
        }

        const updatedProducts = products.filter(item => item.sku !== product.sku)

        setProducts(updatedProducts);
    }

    if (products.length < 1) {
        return <ErrorPage message={'Select items from the products menu to see them appear here!'}/>
    }

    return (
        <PageContainer>
            <ProductContainer>
                {products?.length === 2 ? (
                    products.map(product => {
                        return <ProductItem>
                            <Product>
                                <Title>{product.name} ({product.sku})</Title>
                                <Attribute>Description: {product.description}</Attribute>
                                <Attribute>Vendor: {product.vendor}</Attribute>
                                <Attribute>Url: {product.url}</Attribute>
                                <Attribute>Price: {product.price}</Attribute>
                                <Attribute>Quantity: {product.quantity}</Attribute>
                            </Product>

                            <DeleteButtonContainer onClick={() => deleteItem(product)}>
                                <AiFillDelete size={70} color={'white'}/>
                            </DeleteButtonContainer>
                        </ProductItem>
                    })
                ) : (
                    <Grid container spacing={{xs: 2, md: 3}} columns={{xs: 4, sm: 8, md: 12}}>
                        {products?.map((product, index) => (
                            <Grid item xs={2} sm={4} md={4} key={index}>
                                <ProductItem>
                                    <Product>
                                        <Title>{product.name} ({product.sku})</Title>
                                        <Attribute>Description: {product.description}</Attribute>
                                        <Attribute>Vendor: {product.vendor}</Attribute>
                                        <Attribute>Url: {product.url}</Attribute>
                                        <Attribute>Price: {product.price}</Attribute>
                                        <Attribute>Quantity: {product.quantity}</Attribute>
                                    </Product>

                                    <DeleteButtonContainer onClick={() => deleteItem(product)}>
                                        <AiFillDelete size={70} color={'white'}/>
                                    </DeleteButtonContainer>
                                </ProductItem>
                            </Grid>
                        ))}
                    </Grid>
                )}
            </ProductContainer>
        </PageContainer>
    )
}

export default Cart;

const ProductContainer = styled.div`
  padding: 30px;
`

const Product = styled.div`
  display: flex;
  flex-direction: column;
  flex: 3;
`;

const Title = styled.p`
  font-weight: bold;
  font-size: 20px;
`;

const Attribute = styled.p`
  font-size: 17px;
`;

const ProductItem = styled.div`
  display: flex;
  flex-direction: row;

  border-radius: 20px;
  border: 1px solid gray;

  padding: 20px;

  min-width: 500px;
`;

const DeleteButtonContainer = styled.div`
  display: flex;
  flex: 1;
  justify-content: center;
  align-items: center;

  background-color: lightpink;
  border-radius: 20px;

  &:hover {
    opacity: 0.7;
    cursor: pointer;
  }
`;
