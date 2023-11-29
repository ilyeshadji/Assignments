import { Grid } from "@mui/material";
import React, { useEffect, useState } from 'react';
import { useSelector } from "react-redux";
import styled from 'styled-components';
import { orderApi } from '../api';
import PageContainer from '../components/UI/PageContainer';
import Order from "../components/order/Order";
import Toaster from "../plugin/Toaster";
import { selectUser } from "../store/selectors";
import { showBackendError } from '../utils/utils';
import Loader from './Loader';

function ClaimOrder() {
    const userId = useSelector(selectUser).user_id;

    const [orders, setOrders] = useState()
    const [isLoading, setIsLoading] = useState(true)

    useEffect(() => {
        (async () => {
            try {
                let response;

                response = await orderApi.getOrdersForCustomer(0);

                setOrders(response.data);
            } catch (e) {
                showBackendError(e);
            } finally {
                setIsLoading(false);
            }
        })();
    }, []);

    async function claimOrder(orderId) {
        try {
            await orderApi.claim(orderId, userId)

            Toaster.success("Sucessfully claimed the order!");

            // Just to emulate some time before the request gets completed
            setTimeout(() => {
                window.location.reload();
            }, 2000);

        } catch (e) {
            showBackendError(e)
        } finally {
        }
    }

    if (isLoading) {
        return <Loader />
    }

    return (
        <PageContainer>
            <OrdersContainer>
                <Title>Orders</Title>
                <Grid container spacing={{ xs: 2, md: 3 }} columns={{ xs: 4, sm: 4, md: 8 }}>
                    {orders?.map((order, index) => {
                        return <Grid item xs={2} sm={4} md={4} key={index}>
                            <Order order={order} claimable claimOrder={() => claimOrder(order.order_id)} />
                        </Grid>
                    })}
                </Grid>
            </OrdersContainer>
        </PageContainer>
    )
}

export default ClaimOrder;

const OrdersContainer = styled.div`
    display: flex;
    flex: 1;
    flex-direction: column;
    justify-content: flex-start;
    align-items: flex-start;
    width: 100%;
    padding: 50px;
`

const Title = styled.p`
  font-weight: bold;
  font-size: 20px;
  margin-bottom: 25px;
`;