import React from 'react'
import styled from 'styled-components';
import mstyled from "@emotion/styled";
import {Divider} from "@mui/material";

function ProductItem({product}) {
    function redirect() {
        window.open(product.url, '_blank');
    }

    return (
        <Container>
            <StyledDivider/>
            <Label>Name: <Value>{product.name}</Value></Label>
            <Label>Sku: <Value>{product.sku}</Value></Label>
            <Label>Quantity: <Value>{product.quantity}</Value></Label>
            <Link onClick={redirect}>See Item</Link>
        </Container>
    )
}

export default ProductItem;

const Container = styled.div`
  display: flex;
  flex-direction: column;

  margin: 0 0 0 30px;
`;

const Label = styled.p`
  display: flex;
  flex-direction: row;

  color: grey;
  font-weight: bold;

  margin-left: ${props => props.marginLeft ? props.marginLeft : ''};
`

const Value = styled.p`
  color: grey;
  font-weight: normal;
  margin-left: 5px;
`

const Link = styled.p`
  text-decoration: underline;
  color: grey;

  &:hover {
    cursor: pointer;
    opacity: 0.5;
  }
`;

const StyledDivider = mstyled(Divider)`
    margin: 10px 0 10px 0;
    width: 50%;
`;
