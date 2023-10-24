import React from 'react';
import styled from 'styled-components'
import PageContainer from "../components/UI/PageContainer";
import {BiErrorAlt} from "react-icons/bi";
import {AiOutlineShoppingCart} from "react-icons/ai";

function ErrorPage({message, icon}) {
    return (
        <PageContainer>
            <Container>
                {icon === 'cart' ? (
                    <AiOutlineShoppingCart size={200} style={{marginTop: '-90px', marginBottom: '50px'}}
                                           color={'grey'}/>

                ) : (
                    <BiErrorAlt size={200} style={{marginTop: '-90px', marginBottom: '50px'}} color={'grey'}/>

                )}
                <Message color={'grey'}>{message}</Message>
            </Container>
        </PageContainer>
    )
}

export default ErrorPage;

const Container = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  flex: 1;
`

const Message = styled.h2`
  color: grey;
`;
