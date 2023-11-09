import React, {useEffect, useState} from 'react';
import styled from 'styled-components';
import {cartApi, orderApi} from "../api";
import {showBackendError} from "../utils/utils";
import PageContainer from "../components/UI/PageContainer";
import {Grid} from "@mui/material";
import {AiFillCloseCircle} from "react-icons/ai";
import Toaster from "../plugin/Toaster";
import ErrorPage from "./ErrorPage";
import {useSelector} from "react-redux";
import {selectUser} from "../store/selectors";
import {CiDeliveryTruck} from "react-icons/ci";
import Loader from "./Loader";
import TextInput from "../components/UI/TextInput";
import ProductItem from "../components/cart/ProductItem";

function Cart() {
    const userId = useSelector(selectUser).user_id;

    const [isLoading, setIsLoading] = useState(true);
    const [products, setProducts] = useState([]);
    const [checkingIfSure, setCheckingIfSure] = useState(false);
    const [shippingAddress, setShippingAddress] = useState('');

    useEffect(() => {
        (async () => {
            try {
                const response = await cartApi.getCart(userId);
                setProducts(response.data.products);
            } catch (e) {
                showBackendError(e);
            } finally {
                setIsLoading(false)
            }
        })();
    }, [userId]);

    async function createOrder() {
        if (!checkingIfSure) {
            Toaster.info("You are about to send your order. Once this is done, you won't be able to modify it. Enter the address and press on continue to confirm.")
            setCheckingIfSure(true);

            return;
        }

        try {
            await orderApi.createOrder(userId, shippingAddress)
            Toaster.success("Successfully created the order! You can navigate to the order tab to track their progress.")

            setProducts([]);
        } catch (e) {
            showBackendError(e);
        }
    }

    if (isLoading) {
        return <Loader/>
    }

    if (products.length < 1) {
        return <ErrorPage message={'Select items from the products menu to see them appear here!'}/>
    }

    return (
        <PageContainer>
            <ButtonContainer>
                <Button onClick={createOrder}>
                    <CiDeliveryTruck size={20} color={'grey'}/>
                    <p style={{color: 'grey', marginLeft: '10px'}}>{checkingIfSure ? 'Continue' : 'Checkout'}</p>
                </Button>

                {checkingIfSure && (
                    <>
                        <CancelButton onClick={() => setCheckingIfSure(false)}>
                            <AiFillCloseCircle size={20} color={'grey'}/>
                            <p style={{color: 'grey', marginLeft: '10px'}}>Cancel</p>
                        </CancelButton>

                        <ShippingAddressContainer>
                            <TextInput
                                type="text"
                                label={'Shipping address'}
                                value={shippingAddress}
                                marginTop="20px"
                                marginBottom="15px"
                                fontSize={'15px'}
                                onChange={(e) => setShippingAddress(e.target.value)}
                            />
                        </ShippingAddressContainer>
                    </>

                )}
            </ButtonContainer>

            <ProductContainer>
                {products?.length === 2 ? (
                    products.map(product => {
                        return <ProductItem product={product} products={products} setProducts={setProducts}/>
                    })
                ) : (
                    <Grid container spacing={{xs: 2, md: 3}} columns={{xs: 4, sm: 8, md: 12}}>
                        {products?.map((product, index) => (
                            <Grid item xs={2} sm={4} md={4} key={index}>
                                <ProductItem product={product} products={products} setProducts={setProducts}/>
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
  display: flex;
  flex: 7;
  padding: 30px;
`

const ButtonContainer = styled.div`
  display: flex;
  align-items: center;
  flex: 1;
  width: 100%;

  max-height: 100px;
`;

const Button = styled.div`
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

const CancelButton = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;

  background-color: lightpink;

  padding: 20px;

  height: 50px;

  margin-left: 40px;

  border-radius: 20px;

  &:hover {
    opacity: 0.7;
    cursor: pointer;
  }
`;

const ShippingAddressContainer = styled.div`
  display: flex;

  margin: 20px 0 0 40px;

  width: 25%;
  max-width: 400px;
  min-width: 300px;
`
