import React, {useState} from 'react';
import styled from 'styled-components';
import {AiFillDelete} from "react-icons/ai";
import {FiEdit2} from "react-icons/fi";
import {cartApi} from "../../api";
import Toaster from "../../plugin/Toaster";
import {showBackendError} from "../../utils/utils";
import {GiCancel, GiConfirmed} from "react-icons/gi";
import TextInput from "../UI/TextInput";
import {useSelector} from "react-redux";
import {selectUser} from "../../store/selectors";

function ProductItem({product, setProducts, products}) {
    const user_id = useSelector(selectUser).user_id;

    const [isEditing, setIsEditing] = useState(false);
    const [quantity, setQuantity] = useState(product.quantity);

    async function deleteItem() {
        try {
            await cartApi.removeFromCart(1, product.sku);
            Toaster.success("Successfully removed item from cart!")
        } catch (e) {
            showBackendError(e);
        }

        const updatedProducts = products.filter(item => item.sku !== product.sku)

        setProducts(updatedProducts);
    }


    async function editQuantity() {
        try {
            await cartApi.changeQuantity(user_id, product.sku, quantity)

            Toaster.success(`Successfully changed the amount of product ${product.name} in your cart.`);
        } catch (e) {
            showBackendError(e)
        }

        setIsEditing(false);
    }

    return (
        <Container>
            <Product>
                <Title>{product.name} ({product.sku})</Title>
                <Attribute>Description: {product.description}</Attribute>
                <Attribute>Vendor: {product.vendor}</Attribute>
                <Attribute>Url: {product.url}</Attribute>
                <Attribute>Price: {product.price} $ ({(product.price * quantity).toFixed(2)} $)</Attribute>
                {isEditing ? (
                    <InputContainer>
                        <TextInput
                            type="number"
                            label={'Quantity'}
                            value={quantity}
                            fontSize={'15px'}
                            line
                            width={'10px'}
                            inputStyle={{textAlign: 'center'}}
                            onChange={(e) => setQuantity(e.target.value)}
                        />
                    </InputContainer>
                ) : (
                    <Attribute>Quantity: {quantity}</Attribute>
                )}
            </Product>

            <ButtonsContainer>
                {isEditing ? (
                    <>
                        <DeleteButtonContainer onClick={() => setIsEditing(!isEditing)}>
                            <GiCancel size={40} color={'white'}/>
                        </DeleteButtonContainer>

                        <ConfirmButtonContainer onClick={editQuantity}>
                            <GiConfirmed size={40} color={'white'}/>
                        </ConfirmButtonContainer>
                    </>
                ) : (
                    <>
                        <DeleteButtonContainer onClick={deleteItem}>
                            <AiFillDelete size={40} color={'white'}/>
                        </DeleteButtonContainer>

                        <EditButtonContainer onClick={() => setIsEditing(!isEditing)}>
                            <FiEdit2 size={40} color={'white'}/>
                        </EditButtonContainer>
                    </>
                )}
            </ButtonsContainer>

        </Container>
    )
}

export default ProductItem

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

const Container = styled.div`
  display: flex;
  flex-direction: row;

  border-radius: 20px;
  border: 1px solid gray;


  padding: 20px;

  min-width: 500px;
  max-height: 210px;
`;

const DeleteButtonContainer = styled.div`
  display: flex;
  flex: 1;
  justify-content: center;
  align-items: center;

  background-color: lightpink;
  border-radius: 20px;

  margin: 4px 0;

  height: 30px;

  &:hover {
    opacity: 0.7;
    cursor: pointer;
  }
`;

const EditButtonContainer = styled.div`
  display: flex;
  flex: 1;
  justify-content: center;
  align-items: center;

  background-color: lightblue;
  border-radius: 20px;

  margin: 4px 0;

  height: 30px;

  &:hover {
    opacity: 0.7;
    cursor: pointer;
  }
`;

const ConfirmButtonContainer = styled.div`
  display: flex;
  flex: 1;
  justify-content: center;
  align-items: center;

  background-color: lightgreen;
  border-radius: 20px;

  margin: 4px 0;

  height: 30px;

  &:hover {
    opacity: 0.7;
    cursor: pointer;
  }
`;

const ButtonsContainer = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;

  height: 170px;
`

const InputContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 35%;
`;

