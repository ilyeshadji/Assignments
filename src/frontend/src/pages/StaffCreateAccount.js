import React, { useEffect } from 'react'
import { useSelector } from 'react-redux'
import { useNavigate } from 'react-router-dom'
import PageContainer from '../components/UI/PageContainer'
import CreateAccountForm from '../components/authentication/CreateAccountForm'
import Toaster from '../plugin/Toaster'
import { selectUserRole } from '../store/selectors'

function StaffCreateAccount() {
    const navigate = useNavigate();

    const role = useSelector(selectUserRole)

    useEffect(() => {
        if (role !== 'staff') {
            Toaster.error('Oops, you are not supposed to be there, redirecting to login page');
            navigate('/')
        }

    }, [navigate, role]);

    return (
        <PageContainer centered>
            <CreateAccountForm role={'customer'} />
        </PageContainer>
    )
}

export default StaffCreateAccount