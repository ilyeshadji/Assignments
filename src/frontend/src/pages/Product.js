import React, { useEffect, useMemo, useState } from 'react';
import { useSelector } from "react-redux";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import styled from 'styled-components';
import { cartApi, productApi } from "../api";
import PageContainer from "../components/UI/PageContainer";
import TextInput from "../components/UI/TextInput";
import Toaster from "../plugin/Toaster";
import { selectIsLoggedIn, selectUser, selectUserRole } from "../store/selectors";
import { showBackendError } from "../utils/utils";
import { FormContainer, FormTitle } from "./CreateProduct";

function Product() {
    const location = useLocation();
    const params = useParams();
    const navigate = useNavigate();
    const role = useSelector(selectUserRole);
    const userId = useSelector(selectUser).user_id;
    const isLoggedIn = useSelector(selectIsLoggedIn);

    const [sku, setSku] = useState(location?.state?.sku || '');
    const [name, setName] = useState(location?.state?.name || '');
    const [description, setDescription] = useState(location?.state?.description || '');
    const [vendor, setVendor] = useState(location?.state?.vendor || '');
    const [url, setUrl] = useState(location?.state?.url || '');
    const [price, setPrice] = useState(location?.state?.price || '');

    const [quantity, setQuantity] = useState(1);
    const [navigateToProductList, setNavigateToProductList] = useState(false);

    const isCustomer = useMemo(() => role === 'customer', [role]);

    const buttonText = useMemo(() => {
        if (isCustomer) {
            return 'Add to Cart';
        } else if (navigateToProductList) {
            return 'Go to product list'
        } else {
            return 'Edit'
        }
    }, [isCustomer, navigateToProductList]);

    useEffect(() => {
        if (!name) {
            (async () => {
                try {
                    const response = await productApi.getProductBySku(location?.state?.sku || params?.sku);
                    setSku(response.data.sku);
                    setName(response.data.name);
                    setDescription(response.data.description)
                    setVendor(response.data.vendor);
                    setUrl(response.data.url);
                    setPrice(response.data.price);
                } catch (e) {
                    showBackendError(e);
                }
            })();
        }
    }, [location?.state?.sku, name, params?.sku])

    async function handleButton() {
        if (!isLoggedIn) {
            Toaster.warning("You have to be logged in to add an item to your cart.")

            return
        }

        if (isCustomer) {
            try {
                await cartApi.addToCart(userId, location.state.sku, quantity);
                Toaster.success('Product successfully added to cart!');
            } catch (e) {
                showBackendError(e);
            }
        } else if (navigateToProductList) {
            navigate('/products')
        } else {
            try {
                await productApi.updateProduct({ sku, name, description, vendor, url, price });
                setNavigateToProductList(true);
                Toaster.success('Product successfully updated!');
            } catch (e) {
                showBackendError(e);
            }
        }
    }

    return (
        <PageContainer style={{ justifyContent: 'center' }}>
            <FormContainer>
                <FormTitle>Product attributes</FormTitle>
                <TextInput
                    type="text"
                    label={'Sku'}
                    value={sku}
                    marginTop="20px"
                    marginBottom="15px"
                    fontSize={'15px'}
                    disabled
                />

                <TextInput
                    type="text"
                    label={'Name'}
                    value={name}
                    marginTop="20px"
                    marginBottom="15px"
                    fontSize={'15px'}
                    disabled={isCustomer}
                    onChange={(e) => setName(e.target.value)}
                />

                <TextInput
                    type="text"
                    label={'Description'}
                    value={description}
                    marginTop="20px"
                    marginBottom="15px"
                    fontSize={'15px'}
                    disabled={isCustomer}
                    onChange={(e) => setDescription(e.target.value)}
                />

                <TextInput
                    type="text"
                    label={'Vendor'}
                    value={vendor}
                    marginTop="20px"
                    marginBottom="15px"
                    fontSize={'15px'}
                    disabled={isCustomer}
                    onChange={(e) => setVendor(e.target.value)}
                />

                <TextInput
                    type="text"
                    label={'Url'}
                    value={url}
                    marginTop="20px"
                    marginBottom="15px"
                    fontSize={'15px'}
                    disabled
                />

                <TextInput
                    type="number"
                    label={'Price'}
                    value={price}
                    marginTop="20px"
                    marginBottom="15px"
                    fontSize={'15px'}
                    disabled={isCustomer}
                    onChange={(e) => setPrice(e.target.value)}
                />

                <SaveButtonContainer>
                    {isCustomer && (
                        <InputContainer>
                            <TextInput
                                type="number"
                                label={'Quantity'}
                                value={quantity}
                                marginTop="20px"
                                marginBottom="15px"
                                fontSize={'15px'}
                                line
                                width={'10px'}
                                inputStyle={{ textAlign: 'center' }}
                                onChange={(e) => setQuantity(e.target.value)}
                            />
                        </InputContainer>
                    )}

                    <SaveButton onClick={handleButton}>
                        {buttonText}
                    </SaveButton>
                </SaveButtonContainer>

            </FormContainer>
        </PageContainer>
    );
}

export default Product;

const SaveButtonContainer = styled.div`
  display: flex;
  justify-content: flex-end;
  align-items: center;

  width: 100%;

  margin-top: 20px;
`;

const SaveButton = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;

  height: 50px;

  padding: 0 20px 0 20px;

  background-color: lightgray;

  border-radius: 20px;

  &:hover {
    opacity: 0.7;
    cursor: pointer;
  }
`;

const InputContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 20%;

  margin-right: 20px;
  margin-top: 10px;
`;
