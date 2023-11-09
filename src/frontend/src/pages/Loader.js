import React from 'react';
import styled from "styled-components";
import {CircularProgress} from "@mui/material";

function Loader() {
    return (
        <Container>
            <CircularProgress color="inherit"/>
        </Container>
    )
}

export default Loader;

const Container = styled.div`
  display: flex;
  flex: 1;

  justify-content: center;
  align-items: center;

  height: 80vh;

  color: grey;
`
