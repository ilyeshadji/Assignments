import React from 'react';
import styled from 'styled-components'

function PageContainer({children, style, centered}) {
    return <Page style={style} centered={centered}>{children}</Page>
}

export default PageContainer;

const Page = styled.div`
  display: flex;
  justify-content: ${props => (props.centered ? 'center' : 'flex-start')};
  align-items: center;
  flex: 1;
  flex-direction: column;

  height: 95vh;
`;
