import mstyled from '@emotion/styled';
import { Button } from "@mui/material";
import React, { useState } from 'react';
import styled from 'styled-components';
import { authApi } from '../api';
import PageContainer from '../components/UI/PageContainer';
import TextInput from '../components/UI/TextInput';
import { usePasswordConfirmed } from '../hooks/InputValidation/usePasswordConfirmed';
import Toaster from '../plugin/Toaster';
import { showBackendError } from '../utils/utils';

function UpdatePassword() {
    const [password, setPassword] = useState();
    const [newPassword, setNewPassword] = useState();
    const [confirmedPassword, setConfirmedNewPassword] = useState();

    const isPasswordConfirmed = usePasswordConfirmed(newPassword, confirmedPassword)

    async function updatePassword() {
        if (!isPasswordConfirmed) {
            Toaster.warning('Your two passwords do not match.')

            return
        }

        try {
            await authApi.updatePassword(password, newPassword);

            Toaster.success("Successfully updated password!");
        } catch (e) {
            showBackendError(e)
        }
    }

    return (
        <PageContainer centered>
            <CardContainer>
                <Title>UPDATE PASSWORD</Title>

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
                    label={'New Password'}
                    value={newPassword}
                    marginTop="20px"
                    marginBottom="45px"
                    fontSize={'15px'}
                    onChange={(e) => setNewPassword(e.target.value)}
                />

                <TextInput
                    type="password"
                    label={'Confirm New Password'}
                    value={confirmedPassword}
                    marginTop="20px"
                    marginBottom="45px"
                    fontSize={'15px'}
                    onChange={(e) => setConfirmedNewPassword(e.target.value)}
                />

                <MyButton variant="contained" onClick={updatePassword}>Update Password</MyButton>
            </CardContainer>
        </PageContainer>
    )
}

export default UpdatePassword

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
