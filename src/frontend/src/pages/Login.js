import React, {useState} from 'react'

import PageContainer from "../components/UI/PageContainer";
import LoginForm from "../components/authentication/LoginForm";
import CreateAccountForm from "../components/authentication/CreateAccountForm";

function Login() {
    const [createAccount, setCreateAccount] = useState(false)

    return (
        <PageContainer centered>
            {createAccount ? <CreateAccountForm/> : <LoginForm setCreateAccount={setCreateAccount}/>}
        </PageContainer>
    )
}

export default Login
