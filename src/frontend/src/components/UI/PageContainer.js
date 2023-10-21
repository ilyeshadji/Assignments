import React from 'react';
import styled from 'styled-components'

function PageContainer({children, style}) {
    return <Page style={style}>{children}</Page>
}

export default PageContainer;

const Page = styled.div`
  display: flex;
  justify-content: flex-start;
  align-items: center;
  flex: 1;
  flex-direction: column;

  height: 95vh;
`;
