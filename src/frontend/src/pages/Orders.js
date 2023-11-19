import React, {useEffect, useState} from 'react';
import styled from 'styled-components';
import PageContainer from "../components/UI/PageContainer";
import Loader from "./Loader";
import ErrorPage from "./ErrorPage";
import {orderApi} from "../api";
import {showBackendError} from "../utils/utils";
import {useSelector} from "react-redux";
import {selectUser} from "../store/selectors";
import Order from "../components/order/Order";

function Orders() {
    const user = useSelector(selectUser);

    const [isLoading, setIsLoading] = useState(true);
    const [currentOrders, setCurrentOrders] = useState([]);
    const [shippedOrders, setShippedOrders] = useState([]);

    const isCustomer = user.role === 'customer';

    useEffect(() => {
        (async () => {
            try {
                let response;

                if (isCustomer) {
                    response = await orderApi.getOrdersForCustomer(user.user_id);
                } else {
                    response = await orderApi.getAllOrders();
                }

                // Parsing orders
                let ordersCurrent = [];
                let ordersShipped = [];

                response.data.forEach(order => {
                    if (order.tracking_number === 0) {
                        ordersCurrent = [...ordersCurrent, order];
                    } else {
                        ordersShipped = [...ordersShipped, order];
                    }
                })

                setCurrentOrders(ordersCurrent);
                setShippedOrders(ordersShipped);
            } catch (e) {
                showBackendError(e);
            } finally {
                setIsLoading(false);
            }
        })();
    }, [isCustomer, user.user_id]);

    if (isLoading) {
        return <Loader/>
    }

    if (currentOrders.length < 1 && shippedOrders.length < 1) {
        return <ErrorPage message={'Create orders to see them appear here!'}/>
    }

    return (
        <PageContainer>
            <SectionContainer>
                <Section marginRight={'40px'}>
                    <Title>Current orders</Title>

                    <OrderContainer>
                        {currentOrders?.map(order => {
                            return <Order order={order}/>
                        })}
                    </OrderContainer>
                </Section>

                <Section>
                    <Title>Shipped orders</Title>

                    <OrderContainer>
                        {shippedOrders?.map(order => {
                            return <Order order={order}/>
                        })}
                    </OrderContainer>
                </Section>
            </SectionContainer>
        </PageContainer>
    )
}

export default Orders;

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
`;

const OrderContainer = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;

  overflow: auto;
`;

const Title = styled.h2`
  font-size: 25px;
  color: grey;
`
