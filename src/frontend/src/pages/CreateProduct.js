import React, {useState} from 'react';
import styled from 'styled-components';
import PageContainer from "../components/UI/PageContainer";
import TextInput from "../components/UI/TextInput";
import {useNavigate} from "react-router-dom";
import Toaster from "../plugin/Toaster";
import {productApi} from "../api";

function CreateProduct() {
    const navigate = useNavigate();

    const [sku, setSku] = useState();
    const [name, setName] = useState();
    const [description, setDescription] = useState();
    const [vendor, setVendor] = useState();
    const [url, setUrl] = useState();
    const [price, setPrice] = useState();
    const [productCreated, setProductCreated] = useState(false);

    async function buttonHandler() {
        if (productCreated) {
            navigate('/');
        } else {
            try {
                await productApi.createProduct({
                    sku,
                    name,
                    description,
                    vendor,
                    url,
                    price
                });

                setProductCreated(true);
                Toaster.success('Product created')
            } catch (e) {
                // TODO: catch error
            }
        }
    }

    return (
        <PageContainer>
            <FormContainer>
                <FormTitle>Product attributes</FormTitle>
                <TextInput
                    type="text"
                    label={'Sku'}
                    value={sku}
                    marginTop="20px"
                    marginBottom="15px"
                    fontSize={'15px'}
                    onChange={(e) => setSku(e.target.value)}
                />

                <TextInput
                    type="text"
                    label={'Name'}
                    value={name}
                    marginTop="20px"
                    marginBottom="15px"
                    fontSize={'15px'}
                    onChange={(e) => setName(e.target.value)}
                />

                <TextInput
                    type="text"
                    label={'Description'}
                    value={description}
                    marginTop="20px"
                    marginBottom="15px"
                    fontSize={'15px'}
                    onChange={(e) => setDescription(e.target.value)}
                />

                <TextInput
                    type="text"
                    label={'Vendor'}
                    value={vendor}
                    marginTop="20px"
                    marginBottom="15px"
                    fontSize={'15px'}
                    onChange={(e) => setVendor(e.target.value)}
                />

                <TextInput
                    type="text"
                    label={'Url'}
                    value={url}
                    marginTop="20px"
                    marginBottom="15px"
                    fontSize={'15px'}
                    onChange={(e) => setUrl(e.target.value)}
                />

                <TextInput
                    type="number"
                    label={'Price'}
                    value={price}
                    marginTop="20px"
                    marginBottom="15px"
                    fontSize={'15px'}
                    onChange={(e) => setPrice(e.target.value)}
                />

                <SaveButtonContainer>
                    <SaveButton onClick={buttonHandler}>
                        {productCreated ? 'Go to Home' : 'Save'}
                    </SaveButton>
                </SaveButtonContainer>

            </FormContainer>
        </PageContainer>
    );
}

export default CreateProduct;

const FormContainer = styled.div`
  display: flex;
  align-items: flex-start;
  flex-direction: column;

  border: 1px solid gray;
  border-radius: 20px;

  width: 600px;

  padding: 20px;
`;

const FormTitle = styled.p`
  font-size: 20px;
  font-weight: bold;

  margin: 0 0 15px 0;
`;

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
