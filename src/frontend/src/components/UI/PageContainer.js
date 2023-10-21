import React from 'react';
import styled from 'styled-components'

function PageContainer({children}) {
    return <Page>{children}</Page>
}

export default PageContainer;

const Page = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  flex: 1;
  flex-direction: column;

  height: 95vh;
`;
