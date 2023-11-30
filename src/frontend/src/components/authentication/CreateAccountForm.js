import mstyled from '@emotion/styled';
import { Button } from "@mui/material";
import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import styled from 'styled-components';
import { authApi } from "../../api";
import { usePasswordConfirmed } from "../../hooks/InputValidation/usePasswordConfirmed";
import Toaster from "../../plugin/Toaster";
import { selectUserRole } from '../../store/selectors';
import { isPasswordValid, showBackendError } from "../../utils/utils";
import TextInput from "../UI/TextInput";

function CreateAccountForm({ setCreateAccount = () => { } }) {
    const role = useSelector(selectUserRole)

    const [password, setPassword] = useState('');
    const [confirmedPassword, setConfirmedPassword] = useState('');

    const isPasswordConfirmed = usePasswordConfirmed(password, confirmedPassword)

    async function signup() {
        if (!isPasswordConfirmed) {
            Toaster.warning('Your two passwords do not match.')

            return
        }

        if (!isPasswordValid(password)) {
            Toaster.error(
                'Please make sure your password is alpha numeric and contains at least four characters.'
            );

            return
        }

        try {
            await authApi.signup(password, role)

            Toaster.success("Successfully created the account. You can now log in with those credentials.")
            setCreateAccount(false);
        } catch (e) {
            showBackendError(e);
        }
    }

    return (
        <CardContainer>
            <Title>{role === 'staff' ? 'CREATE STAFF ACCOUNT' : 'SIGN UP'}</Title>

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
    text-align: center;
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
