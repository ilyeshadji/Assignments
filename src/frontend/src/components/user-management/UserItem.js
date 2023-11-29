import React, { useMemo, useState } from 'react';
import styled from 'styled-components';
import { userApi } from '../../api';
import { showBackendError } from '../../utils/utils';

function UserItem({ user, isRefreshed, setIsRefreshed }) {
    const [role, setRole] = useState(user.role);

    const isCustomer = user.role === 'customer'
    const wasModified = role !== user.role

    const buttonText = useMemo(() => {
        if (isCustomer) {

            if (!wasModified) {
                return 'Grant Staff Access'
            }

            return 'Staff Access Granted'
        }

        if (!isCustomer) {
            if (!wasModified) {
                return 'Remove Staff access'
            }

            return 'Staff access removed'
        }
    }, [isCustomer, wasModified]);

    const buttonBackgroundColor = useMemo(() => {
        if (wasModified) {
            return 'lightgreen'
        }

        if (isCustomer) {
            return 'lightBlue'
        }
        else {
            return 'lightpink'
        }
    }, [isCustomer, wasModified])

    async function handleUserButton() {
        if (wasModified) {
            return
        }

        const role = isCustomer ? 'staff' : 'customer'

        try {
            await userApi.changeRole(user.user_id, role)
        }
        catch (e) {
            showBackendError(e)
        }
        finally {
            setRole(role)
            setIsRefreshed(false)
        }
    }

    return (
        <UserContainer>
            <InformationContainer>
                <Title>User ID: {user.user_id}</Title>
                <Attribute>Role: {user.role}</Attribute>
            </InformationContainer>

            <ButtonContainer>
                <UserButton onClick={handleUserButton} isCustomer={isCustomer} buttonBackgroundColor={buttonBackgroundColor} disabled={wasModified}>
                    {buttonText}
                </UserButton>
            </ButtonContainer>
        </UserContainer>
    )
}

export default UserItem

const InformationContainer = styled.div`
    display: flex;
    flex-direction: column;
`

const UserContainer = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;

  border: 1px solid lightgray;
  border-radius: 20px;

  margin: 0 0 20px 0;
  padding: 5px 5px 5px 20px;
  min-height: 70px;
`;

const Title = styled.p`
  font-weight: bold;
  font-size: 20px;
`;

const Attribute = styled.p`
  font-size: 17px;
`;

const ButtonContainer = styled.div`
    display: flex;

`;

const UserButton = styled.div`
  display: flex;
  flex: 1;
  justify-content: center;
  align-items: center;

  background-color: ${props => props.buttonBackgroundColor};

  border-radius: 15px;

  padding: 0 10px;
  margin: 5px 0 5px 0;

  &:hover {
    cursor: ${props => props.disabled ? 'not-allowed' : 'pointer'};
    opacity: ${props => props.disabled ? '1' : '0.5'};
  }

  max-height: ${props => props.checkingIfSure ? '60px' : '120px'};
`;