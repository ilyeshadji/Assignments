import React, {useEffect, useState} from 'react';
import styled from 'styled-components';
import {productApi} from "../api";
import {showBackendError} from "../utils/utils";
import PageContainer from "../components/UI/PageContainer";
import {Grid} from "@mui/material";
import {useNavigate} from "react-router-dom";
import {AiOutlineDownload} from "react-icons/ai";
import {useSelector} from "react-redux";
import {selectUserRole} from "../store/selectors";
import ErrorPage from "./ErrorPage";
import Loader from "./Loader";

function ProductList() {
    const navigate = useNavigate();
    const role = useSelector(selectUserRole);

    const [products, setProducts] = useState();
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        (async () => {
            try {
                const response = await productApi.getProductList();
                setProducts(response.data);
            } catch (e) {
                showBackendError(e);
            } finally {
                setIsLoading(false);
            }
        })();
    }, []);

    function redirect(url, product) {
        navigate(url, {
            state: {
                sku: product.sku,
                name: product.name,
                description: product.description,
                vendor: product.vendor,
                url: product.url,
                price: product.price
            },
        });
    }

    async function downloadProjectList() {
        await fetch('http://localhost:8080/Assignments/download/products', {
            method: 'GET',
            headers: {
                'Content-Type': 'text/plain',
                'Customer-Role': role
            },
        })
            .then((response) => response.blob())
            .then((blob) => {
                // Create blob link to download
                const url = window.URL.createObjectURL(
                    new Blob([blob]),
                );
                const link = document.createElement('a');
                link.href = url;
                link.setAttribute(
                    'download',
                    `product-list.txt`,
                );

                // Append to html link element page
                document.body.appendChild(link);

                // Start download
                link.click();

                // Clean up and remove the link
                link.parentNode.removeChild(link);
            });
    }

    if (isLoading) {
        return <Loader/>
    }

    if (!products) {
        return <ErrorPage message={'Please wait for a staff member to add products to the list!'}/>
    }

    return (
        <PageContainer>
            {role === 'staff' && (
                <DownloadButtonContainer>
                    <DownloadButton onClick={downloadProjectList}>
                        <AiOutlineDownload size={20} color={'grey'}/>
                        <p style={{color: 'grey', marginLeft: '10px'}}>Download products list</p>
                    </DownloadButton>
                </DownloadButtonContainer>
            )}


            <ProductContainer>
                <Grid container spacing={{xs: 2, md: 3}} columns={{xs: 4, sm: 8, md: 12}}>
                    {products?.map((product, index) => {
                        return <Grid item xs={2} sm={4} md={4} key={index}>
                            <Product onClick={() => redirect(`/product/${product.sku}`, product)}>
                                <Title>{product.name} ({product.sku})</Title>
                                <Attribute>Description: {product.description}</Attribute>
                                <Attribute>Vendor: {product.vendor}</Attribute>
                                <Attribute>Url: {product.url}</Attribute>
                                <Attribute>Price: {product.price}</Attribute>
                            </Product>
                        </Grid>
                    })}
                </Grid>
            </ProductContainer>
        </PageContainer>
    );
}

export default ProductList;

const ProductContainer = styled.div`
  display: flex;
  flex: 1;
  width: 100%;
  flex-direction: row;
  padding: 30px;
`

const Product = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;

  border-radius: 20px;
  border: 1px solid gray;

  padding: 20px;

  &:hover {
    opacity: 0.7;
    cursor: pointer;
    background-color: lightgray;
  }
`;

const Title = styled.p`
  font-weight: bold;
  font-size: 20px;
`;

const Attribute = styled.p`
  font-size: 17px;
`;

const DownloadButtonContainer = styled.div`
  display: flex;
  align-items: center;
  flex: 1;
  width: 100%;

  max-height: 100px;
`;

const DownloadButton = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;

  background-color: lightblue;

  padding: 20px;

  height: 50px;

  margin-left: 40px;

  border-radius: 20px;

  &:hover {
    opacity: 0.7;
    cursor: pointer;
  }
`;
