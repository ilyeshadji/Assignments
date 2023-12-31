import React, { useState } from 'react';
import { BiSolidDownArrow, BiSolidRightArrow } from "react-icons/bi";
import { GiCancel } from "react-icons/gi";
import { MdOutlineLocalShipping } from "react-icons/md";
import { useSelector } from "react-redux";
import styled from 'styled-components';
import { orderApi } from "../../api";
import Toaster from "../../plugin/Toaster";
import { selectUserRole } from "../../store/selectors";
import { showBackendError } from "../../utils/utils";
import ProductItem from "./ProductItem";

function Order({ order, claimable, claimOrder }) {
  const role = useSelector(selectUserRole);

  const [productsCollapsed, setProductsCollapsed] = useState(true);
  const [checkingIfSure, setCheckingIfSure] = useState(false);

  async function shipOrder() {
    if (!checkingIfSure) {
      Toaster.info("You are about to ship the order. Press on the same button to confirm or cancel the shipment.")
      setCheckingIfSure(true);

      return;
    }

    try {
      await orderApi.ship(order.order_id)
      window.location.reload();
    } catch (e) {
      showBackendError(e)
    } finally {
      setCheckingIfSure(false);
    }
  }

  async function claimingOrder() {
    if (!checkingIfSure) {
      Toaster.info("You are about to claim the order. Press on the same button to confirm or cancel the action.")
      setCheckingIfSure(true);

      return;
    }

    claimOrder();
  }

  return (
    <OrderContainer>
      <InformationContainer>
        <Label>Order id: <Value>{order.order_id}</Value></Label>
        {order.tracking_number !== 0 && <Label>Tracking number: <Value>{order.tracking_number}</Value></Label>}
        {role === 'staff' && <Label>User id: <Value>{order.user_id}</Value></Label>}
        <Label>Shipping address: <Value>{order.shipping_address}</Value></Label>

        <ProductsContainer onClick={() => setProductsCollapsed(!productsCollapsed)}>
          {productsCollapsed ? (
            <BiSolidRightArrow size={10} color={'grey'} />
          ) : (
            <BiSolidDownArrow size={10} color={'grey'} />
          )}
          <Label marginLeft={'5px'}>Products: {order.products.map((product, index) =>
            <Value>{product.name} ({product.quantity}){index < order.products.length - 1 ? ',' : ''}</Value>)}</Label>
        </ProductsContainer>

        {!productsCollapsed && order.products.map((product, index) => {
          return <ProductItem product={product} />
        })}
      </InformationContainer>

      <PriceContainer>
        <Price>{order.totalPrice.toFixed(2)} $</Price>
      </PriceContainer>

      {role === 'staff' && !order.tracking_number && (
        <ButtonContainer>
          <ShipContainer onClick={shipOrder} checkingIfSure={checkingIfSure}>
            <MdOutlineLocalShipping size={60} color={'grey'} />
          </ShipContainer>

          {checkingIfSure && (
            <CancelButton onClick={() => setCheckingIfSure(false)} checkingIfSure={checkingIfSure}>
              <GiCancel size={30} color={'grey'} />
            </CancelButton>
          )}
        </ButtonContainer>
      )}

      {claimable && (
        <ButtonContainer>
          <ClaimContainer onClick={claimingOrder} checkingIfSure={checkingIfSure}>
            Claim Order
          </ClaimContainer>

          {checkingIfSure && (
            <CancelButton onClick={() => setCheckingIfSure(false)} checkingIfSure={checkingIfSure}>
              <GiCancel size={30} color={'grey'} />
            </CancelButton>
          )}
        </ButtonContainer>
      )}
    </OrderContainer>
  )
}

export default Order;

const OrderContainer = styled.div`
  display: flex;
  flex-direction: row;

  border: 1px solid lightgray;
  border-radius: 20px;

  margin: 0 0 20px 0;
  padding: 5px 5px 5px 20px;
  /* min-height: 110px; */
`;

const Label = styled.p`
  display: flex;
  flex-direction: row;

  color: grey;
  font-weight: bold;

  margin-left: ${props => props.marginLeft ? props.marginLeft : ''};
`

const Value = styled.p`
  color: grey;
  font-weight: normal;
  margin-left: 5px;
`

const ProductsContainer = styled.div`
  display: flex;
  flex-direction: row;

  align-items: center;

  &:hover {
    opacity: 0.5;
    cursor: pointer;
  }
`;

const InformationContainer = styled.div`
  display: flex;
  flex: 7;
  flex-direction: column;
`

const PriceContainer = styled.div`
  display: flex;
  flex: 2;
  flex-direction: column;

  justify-content: center;
  align-items: center;

`;

const Price = styled.p`
  color: grey;
  font-weight: bold;
  font-size: 20px;
`;

const ShipContainer = styled.div`
  display: flex;
  flex: 1;
  justify-content: center;
  align-items: center;

  background-color: lightblue;

  border-radius: 15px;

  padding: 0 10px;
  margin: 5px 0 5px 0;

  &:hover {
    cursor: pointer;
    opacity: 0.5;
  }
`;

const ClaimContainer = styled.div`
  display: flex;
  flex: 1;
  justify-content: center;
  align-items: center;

  background-color: lightgreen;

  border-radius: 15px;

  padding: 0 10px;
  margin: 5px 0 5px 0;

  &:hover {
    cursor: pointer;
    opacity: 0.5;
  }
`;

const ButtonContainer = styled.div`
  display: flex;
  flex-direction: column;
`;

const CancelButton = styled.div`
  display: flex;
  flex: 1;
  justify-content: center;
  align-items: center;

  background-color: lightpink;

  border-radius: 15px;

  padding: 0 10px;
  margin: 5px 0 5px 0;

  &:hover {
    cursor: pointer;
    opacity: 0.5;
  }

  max-height: 40px;
`

