import React, { useState } from 'react';
import { IoIosRemoveCircle, IoMdAddCircle } from "react-icons/io";
import styled from 'styled-components';
import QuantityInput from '../UI/QuantityInput';

function ProductItem({ product, onButtonPress }) {
    const [quantity, setQuantity] = useState(1);

    const isOrderProduct = product.quantity > 0;

    return (
        <Product>
            <ProductInformation>
                <Title>{product.name} ({product.sku})</Title>
                <Attribute>Description: {product.description}</Attribute>
                <Attribute>Vendor: {product.vendor}</Attribute>
                <Attribute>Url: {product.url}</Attribute>
                <Attribute>Price: {product.price}</Attribute>
            </ProductInformation>

            <RightContent>
                {isOrderProduct ? (
                    <Attribute>Quantity: {product.quantity}</Attribute>
                ) : (
                    <QuantityInput value={quantity} onChange={(event, value) => setQuantity(value)} />
                )}

                <ButtonContainer>
                    {isOrderProduct ? (
                        <RemoveFromOrder onClick={() => onButtonPress(product)} >
                            <IoIosRemoveCircle size={60} color={'lightpink'} />
                        </RemoveFromOrder>
                    ) : (
                        <AddToOrder onClick={() => onButtonPress(product, quantity)} >
                            <IoMdAddCircle size={60} color={'lightgreen'} />
                        </AddToOrder>
                    )}

                </ButtonContainer>
            </RightContent>
        </Product >)
}

export default ProductItem

const Product = styled.div`
  display: flex;
  flex-direction: row;
  flex: 1;

  border-radius: 20px;
  border: 1px solid gray;

  padding: 20px;

  margin-bottom: 20px;

  max-height: 170px;
`;

const ProductInformation = styled.div`
    display: flex;
    flex-direction: column;
    flex: 5;
`

const Title = styled.p`
  font-weight: bold;
  font-size: 20px;
`;

const Attribute = styled.p`
  font-size: 17px;
`;

const RightContent = styled.div`
    display: flex;
    flex-direction: column;
    flex: 1;
    justify-content: space-between;
`;

const ButtonContainer = styled.div`
  display: flex;
  flex-direction: column;
`;

const AddToOrder = styled.div`
  display: flex;
  flex: 1;
  justify-content: center;
  align-items: center;

  border-radius: 15px;

  padding: 0 10px;
  margin: 5px 0 5px 0;

  &:hover {
    cursor: pointer;
    opacity: 0.5;
  }

  max-height: ${props => props.checkingIfSure ? '60px' : '120px'};
`;

const RemoveFromOrder = styled.div`
  display: flex;
  flex: 1;
  justify-content: center;
  align-items: center;

  border-radius: 15px;

  padding: 0 10px;
  margin: 5px 0 5px 0;

  &:hover {
    cursor: pointer;
    opacity: 0.5;
  }

  max-height: ${props => props.checkingIfSure ? '60px' : '120px'};
`;