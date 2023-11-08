import React, {useState} from 'react';
import styled from 'styled-components'
import mstyled from '@emotion/styled'
import {Button} from "@mui/material";

import TextInput from "../UI/TextInput";
import {authApi} from "../../api";
import {showBackendError} from "../../utils/utils";
import {useDispatch} from "react-redux";
import {authUserSet} from "../../store/user/authUserSlice";
import {useNavigate} from "react-router-dom";

function LoginForm({setCreateAccount}) {
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')

    async function login() {
        try {
            const response = await authApi.login(email, password)

            const base64Url = response.data.split('.')[1];
            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function (c) {
                return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
            }).join(''));

            const payload = JSON.parse(jsonPayload);

            dispatch(authUserSet({
                token: response.data,
                role: payload.role,
                user_id: payload.user_id
            }))

            navigate('/')
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
