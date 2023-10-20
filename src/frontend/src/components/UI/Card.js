import React from 'react';
import styled from 'styled-components';

function Card({children, onClick, style}) {
    return (
        <Container onClick={onClick} style={style}>
            {children}
        </Container>
    );
}

export default Card;

const Container = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  box-shadow: 1px 2px 9px grey;
  margin: 4em;
  padding: 1em;

  max-width: 500px;

  &:hover {
    opacity: 0.5;
    cursor: pointer;
  }
`;
