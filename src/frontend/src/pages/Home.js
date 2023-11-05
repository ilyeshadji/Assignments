import React, {useState, useRef, useMemo} from 'react';
import styled from 'styled-components';
import Card from "../components/UI/Card";
import PageContainer from "../components/UI/PageContainer";
import {useDispatch, useSelector} from "react-redux";
import {selectUserRole} from "../store/selectors";
import {FiPackage} from "react-icons/fi";
import {LuPackagePlus} from "react-icons/lu";
import {AiOutlineShoppingCart} from 'react-icons/ai';
import {useNavigate} from "react-router-dom";

function Home() {
    const navigate = useNavigate();

    const role = useSelector(selectUserRole);

    const isCustomer = useMemo(() => {
        return role === 'customer'
    }, [role]);

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

  max-height: 700px;
`;

const CardTitle = styled.p`
  font-size: 30px;
  margin-top: 50px;
  color: gray;
`;
