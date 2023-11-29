import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { orderApi, productApi } from '../api';
import PageContainer from '../components/UI/PageContainer';
import TextInput from '../components/UI/TextInput';
import ProductItem from '../components/product/ProductItem';
import Toaster from '../plugin/Toaster';
import { showBackendError } from '../utils/utils';
import Loader from './Loader';

function CreateOrder() {
    const [products, setProducts] = useState();
    const [order, setOrder] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [shippingAddress, setShippingAddress] = useState('');

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

    function onButtonPress(product, quantity) {
        // Adding to order
        if (quantity) {
            if (order.some(orderProduct => orderProduct.sku === product.sku)) {
                Toaster.warning('You already have this item in your order. To add some more, delete the one you currently have and update the quantity.')
                return
            }

            const updatedProduct = { ...product, quantity }
            const updatedOrderList = [...order, updatedProduct]

            setOrder(updatedOrderList)
            return
        }

        // Removing from order
        const updatedOrder = order.filter(orderProduct => orderProduct.sku !== product.sku)
        setOrder(updatedOrder)
    }

    async function sendOrder() {
        if (shippingAddress === '') {
            Toaster.warning('Please enter a shipping address to send your order')
        }

        try {
            await orderApi.createOrderWithoutCustomer(shippingAddress, order);
        }
        catch (e) {
            showBackendError(e)
        }
    }

    if (isLoading) {
        return <Loader />
    }

    return (
        <PageContainer centered>
            <SectionContainer>
                <Section marginRight={'40px'}>
                    {!products ? (
                        <div>No products in our catalog</div>
                    ) : (
                        <>
                            {products.map(product => <ProductItem product={product} onButtonPress={onButtonPress} />)}
                        </>
                    )}
                </Section>

                <Section withoutOverflow>
                    <div>
                        <Title>Shipping Address</Title>
                        <TextInput line value={shippingAddress} onChange={(e) => setShippingAddress(e.target.value)} fontSize={'20px'} placeholder={'Address'} />
                    </div>

                    <OrderContainer>
                        {order.length > 0 && (
                            <>
                                {order.map(product => <ProductItem product={product} onButtonPress={onButtonPress} />)}
                                {order.length > 0 && (
                                    <ButtonContainer>
                                        <AddToOrder onClick={sendOrder} >
                                            Confirm order
                                        </AddToOrder>
                                    </ButtonContainer>
                                )}
                            </>
                        )}
                    </OrderContainer>

                </Section>
            </SectionContainer>
        </PageContainer>
    )
}

export default CreateOrder;

const SectionContainer = styled.div`
    display: flex;
    flex: 1;
    flex-direction: row;

    width: 100%;

    padding: 40px;

`

const Section = styled.div`
    display: flex;
    flex: 1;
    flex-direction: column;

    border: 1px solid lightgray;
    border-radius: 30px;

    margin-right: ${props => props.marginRight ? props.marginRight : ''};

    padding: 30px 30px 30px 40px;

    max-height: 80vh;

    overflow: ${props => props.withoutOverflow ? 'none' : 'auto'};
`;

const ButtonContainer = styled.div`
  display: flex;
  flex-direction: column;
  position: absolute;
  top: 93%;
  right: 5%;
`;

const AddToOrder = styled.div`
  display: flex;
  flex: 1;
  justify-content: center;
  align-items: center;

  border-radius: 15px;

  background-color: lightblue;

  padding: 20px;
  margin: 5px 0 5px 0;

  &:hover {
    cursor: pointer;
    opacity: 0.5;
  }

  max-height: ${props => props.checkingIfSure ? '60px' : '120px'};
`;

const Title = styled.p`
  font-weight: bold;
  font-size: 20px;
  margin-bottom: -15px;
`;

const OrderContainer = styled.div`
    display: flex;
    flex-direction: column;

    margin-top: 50px;
    overflow: auto;
`
