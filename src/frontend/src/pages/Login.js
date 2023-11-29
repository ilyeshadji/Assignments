import React, { useState } from 'react';

import PageContainer from "../components/UI/PageContainer";
import CreateAccountForm from "../components/authentication/CreateAccountForm";
import LoginForm from "../components/authentication/LoginForm";

function Login() {
    const [createAccount, setCreateAccount] = useState(false)

    return (
        <PageContainer centered>
            {createAccount ? <CreateAccountForm setCreateAccount={setCreateAccount} /> :
                <LoginForm setCreateAccount={setCreateAccount} />}
        </PageContainer>
    )
}

export default Login
