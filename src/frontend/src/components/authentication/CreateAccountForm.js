import React, {useState} from 'react';
import styled from 'styled-components'
import mstyled from '@emotion/styled'
import TextInput from "../UI/TextInput";
import {Button} from "@mui/material";
import {usePasswordConfirmed} from "../../hooks/InputValidation/usePasswordConfirmed";
import {useValidatedEmail} from "../../hooks/InputValidation/useValidatedEmail";
import {authApi} from "../../api";
import {isPasswordValid, showBackendError} from "../../utils/utils";
import Toaster from "../../plugin/Toaster";

function CreateAccountForm() {
    const [email, setEmail, validated] = useValidatedEmail();
    const [password, setPassword] = useState('');
    const [confirmedPassword, setConfirmedPassword] = useState('');

    const isPasswordConfirmed = usePasswordConfirmed(password, confirmedPassword)

    async function signup() {
        if (!validated) {
            Toaster.warning('Please write a valid email format');

            return
        }

        if (!isPasswordConfirmed) {
            Toaster.warning('Your two passwords do not match.')

            return
        }

        if (!isPasswordValid(password)) {
            Toaster.error(
                'Please make sure your password contains at least 1 uppercase letter, 1 lowercase letter, 1 special character and 1 number'
            );

            return
        }

        try {
            const response = await authApi.signup(email, password)
        } catch (e) {
            showBackendError(e);
        }
    }

    return (
        <CardContainer>
            <Title>SIGN UP</Title>

            <TextInput
                type="text"
                label={'Email'}
                value={email}
                marginTop="20px"
                marginBottom="30px"
                fontSize={'15px'}
                onChange={(e) => setEmail(e.target.value)}
            />

            <TextInput
                type="password"
                label={'Password'}
                value={password}
                marginTop="20px"
                marginBottom="30px"
                fontSize={'15px'}
                onChange={(e) => setPassword(e.target.value)}
            />

            <TextInput
                type="password"
                label={'Confirm Password'}
                value={confirmedPassword}
                marginTop="20px"
                marginBottom="45px"
                fontSize={'15px'}
                onChange={(e) => setConfirmedPassword(e.target.value)}
            />

            <MyButton variant="contained" onClick={signup}>Sign Up</MyButton>
        </CardContainer>
    )
}

export default CreateAccountForm;

const Title = styled.h1`
  color: grey;
  margin: 0 0 50px 0;
`;

const CardContainer = styled.div`
  display: flex;
  flex-direction: column;

  justify-content: center;
  align-items: center;

  width: 25%;

  min-width: 300px;
`

const MyButton = mstyled(Button)`
  color: grey;
  background-color: lightgrey;
  
  margin: 0 0 40px 0;
  
  &:hover{
    opacity: 0.5;
    background-color: white;
  }
`;

const Text = styled.p`
  color: grey;
  text-decoration: underline;

  &:hover {
    cursor: pointer;
  }
`;
