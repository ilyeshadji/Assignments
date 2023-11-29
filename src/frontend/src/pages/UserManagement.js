import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { userApi } from '../api';
import PageContainer from '../components/UI/PageContainer';
import UserItem from '../components/user-management/UserItem';
import { showBackendError } from '../utils/utils';
import Loader from './Loader';

function UserManagement() {
    const [customers, setCustomers] = useState();
    const [staffs, setStaffs] = useState();
    const [isLoading, setIsLoading] = useState(true);
    const [isRefreshed, setIsRefreshed] = useState(false);

    useEffect(() => {
        (async () => fetchUsers())();
    }, [])

    async function fetchUsers() {
        try {
            const response = await userApi.getUsers();

            let customer = [];
            let staff = [];

            response.data.forEach(user => {
                if (user.role === 'customer') {
                    customer = [...customer, user];
                } else {
                    staff = [...staff, user];
                }
            })

            setCustomers(customer);
            setStaffs(staff);
        } catch (e) {
            showBackendError(e);
        } finally {
            setIsLoading(false);
            setIsRefreshed(true);
        }
    }

    if (isLoading) {
        return <Loader />
    }

    return (
        <PageContainer>
            <RefreshContainer>
                <RefreshButton onClick={() => window.location.reload()}>Refresh</RefreshButton>
            </RefreshContainer>

            <SectionContainer>
                <Section marginRight={'40px'}>
                    <Title>Customers</Title>
                    {customers.map(customer => <UserItem user={customer} isRefreshed={isRefreshed} setIsRefreshed={setIsRefreshed} />)}
                </Section>

                <Section>
                    <Title>Staff</Title>
                    {staffs.map(customer => <UserItem user={customer} isRefreshed={isRefreshed} setIsRefreshed={setIsRefreshed} />)}
                </Section>
            </SectionContainer>
        </PageContainer>)
}

export default UserManagement;

const RefreshContainer = styled.div`
    display: flex;
    width: 100%;
    justify-content: flex-start;
    align-items: flex-start;

    padding: 40px;
`

const SectionContainer = styled.div`
  display: flex;
  flex: 1;
  flex-direction: row;

  width: 100%;

  padding: 40px;
`

const Section = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;

  border: 1px solid lightgray;
  border-radius: 30px;

  margin-right: ${props => props.marginRight ? props.marginRight : ''};

  padding: 30px 30px 30px 40px;

  max-height: 80vh;
`;

const Title = styled.h2`
  font-size: 25px;
  color: grey;
`

const RefreshButton = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;

  background-color: lightblue;

  border-radius: 15px;

  padding: 0 10px;
  margin: 5px 0 5px 0;

  &:hover {
    cursor: pointer;
    opacity: 0.5;
  }

  width: 150px;
  height: 40px;
`;
