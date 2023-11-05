import React, {useState} from 'react';
import styled from 'styled-components'
import mstyled from '@emotion/styled'
import {Button} from "@mui/material";

import TextInput from "../UI/TextInput";
import {authApi} from "../../api";
import {showBackendError} from "../../utils/utils";

function LoginForm({setCreateAccount}) {
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')

    async function login() {
        try {
            const response = await authApi.login(email, password)

        } catch (e) {
            showBackendError(e)
        }
    }

    return (
        <CardContainer>
            <Title>LOGIN</Title>

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
                marginBottom="45px"
                fontSize={'15px'}
                onChange={(e) => setPassword(e.target.value)}
            />

            <MyButton variant="contained" onClick={login}>Login</MyButton>

            <Text onClick={() => setCreateAccount(true)}>Create an account</Text>
        </CardContainer>
    )
}

export default LoginForm

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
