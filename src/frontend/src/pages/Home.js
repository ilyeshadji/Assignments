import React, {useState, useRef, useMemo} from 'react';
import styled from 'styled-components';
import Card from "../components/UI/Card";
import PageContainer from "../components/UI/PageContainer";
import {useDispatch, useSelector} from "react-redux";
import {selectUserRole} from "../store/selectors";
import {authUserSet} from "../store/user/authUserSlice";
import Toaster from "../plugin/Toaster";
import {FiPackage} from "react-icons/fi";
import {LuPackagePlus} from "react-icons/lu";
import {AiOutlineShoppingCart} from 'react-icons/ai';
import {useNavigate} from "react-router-dom";

function Home() {
    const inputRef = useRef();
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const role = useSelector(selectUserRole);
    const [password, setPassword] = useState('');

    const isCustomer = useMemo(() => {
        return role === 'customer'
    }, [role]);

    const handleKeyPress = (event) => {
        if (event.key === 'Enter') {

            if (password.toLowerCase() !== 'secret') {
                Toaster.error('Wrong password');
                return;
            }

            const updatedRole = role === 'customer' ? 'staff' : 'customer';
            dispatch(authUserSet(updatedRole));
        }
    }

    return (
        <PageContainer>
            <CardContainer>
                <Card onClick={() => navigate('/products')}>
                    <FiPackage size={200} color={'grey'}/>
                    <CardTitle>View products</CardTitle>
                </Card>

                {isCustomer && <Card onClick={() => navigate('/cart')}>
                    <AiOutlineShoppingCart size={200} color={'grey'}/>
                    <CardTitle>View Cart</CardTitle>
                </Card>}

                {!isCustomer && <Card onClick={() => navigate('/product/create')}>
                    <LuPackagePlus size={200} color={'grey'}/>
                    <CardTitle>Create product</CardTitle>
                </Card>}

            </CardContainer>

            <LoginContainer>
                {isCustomer && <LoginButton onClick={() => inputRef.current?.focus()}>
                    <p>Are you a staff member? Enter the password here for access</p>
                    <Input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        ref={inputRef}
                        onKeyPress={handleKeyPress}
                    />
                </LoginButton>}
            </LoginContainer>
        </PageContainer>
    );
}

export default Home;

const CardContainer = styled.div`
  display: flex;
  justify-content: center;
  flex: 3;
  flex-direction: row;
  width: 100%;
`;

const LoginContainer = styled.div`
  display: flex;
  flex: 1;
  justify-content: center;

  margin-top: 30px;
`;

const LoginButton = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  background-color: lightgray;

  height: 100px;
  width: 500px;

  margin-bottom: 20px;

  border-radius: 40px;

  &:hover {
    opacity: 0.7;
    cursor: text;
  }
`;

const Input = styled.input`
  background-color: lightgray;
  font-size: 20px;
  outline: none;
  border: 0;
  width: 70%;

  line-height: 10px;
  color: #475063;

  margin-top: 10px;

  text-align: center;


  @media (max-width: 768px) {
    font-size: 15px;
  }
`;

const CardTitle = styled.p`
  font-size: 30px;
  margin-top: 50px;
  color: gray;
`;
