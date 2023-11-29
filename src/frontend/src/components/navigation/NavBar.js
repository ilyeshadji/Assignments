import React, { useMemo } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import styled from 'styled-components';

import { useDispatch, useSelector } from "react-redux";
import Scrolling from '../../enum/Scrolling';
import { useScrollInfo } from '../../hooks/useScrollInfo';
import { selectIsLoggedIn, selectUserRole } from "../../store/selectors";
import { authUserUnset } from "../../store/user/authUserSlice";

const NavBar = ({ showShadow }) => {
    const navigate = useNavigate();
    const dispatch = useDispatch();

    const role = useSelector(selectUserRole);
    const isLoggedIn = useSelector(selectIsLoggedIn);

    const isCustomer = useMemo(() => {
        return role === 'customer'
    }, [role]);

    const [direction, y] = useScrollInfo();

    const hideNavBar = useMemo(() => {
        return direction === Scrolling.DOWN && y > 90;
    }, [direction, y]);

    function redirect(url, section) {
        navigate(url, {
            state: {
                section: section,
            },
        });
    }

    function logout() {
        dispatch(authUserUnset());

        navigate('/')
        window.location.reload();
    }

    return (
        <NavContainer out={hideNavBar}>
            <ShadowContainer showShadow={showShadow}>
                <>
                    <LeftSection>
                        <ListItems onClick={() => redirect('/')}>
                            <StyledLink>Home</StyledLink>
                        </ListItems>

                        <ListItems onClick={() => redirect('/products')}>
                            <StyledLink>Products</StyledLink>
                        </ListItems>

                        {!isCustomer && <ListItems onClick={() => redirect('/product/create')}>
                            <StyledLink>Create product</StyledLink>
                        </ListItems>}

                        {isCustomer && !isLoggedIn && <ListItems onClick={() => redirect('/order/create')}>
                            <StyledLink>Create Order</StyledLink>
                        </ListItems>}

                        {isLoggedIn && <ListItems onClick={() => redirect('/orders')}>
                            <StyledLink>Orders</StyledLink>
                        </ListItems>}

                        {isCustomer && isLoggedIn && <ListItems onClick={() => redirect('/orders/claim')}>
                            <StyledLink>Claim Order</StyledLink>
                        </ListItems>}

                    </LeftSection>

                    <RightSection>

                        {!isCustomer && <ListItems onClick={() => redirect('/staff/create-account')}>
                            <StyledLink>CREATE NEW STAFF ACCOUNT</StyledLink>
                        </ListItems>}

                        {isCustomer && isLoggedIn && (
                            <ListItems onClick={() => redirect('/cart')}>
                                <StyledLink>Cart</StyledLink>
                            </ListItems>
                        )}

                        {isLoggedIn && <ListItems onClick={() => redirect('/update-password')}>
                            <StyledLink>Update Password</StyledLink>
                        </ListItems>}

                        {isLoggedIn ? (
                            <ListItems onClick={logout}>
                                <StyledLink>LOGOUT</StyledLink>
                            </ListItems>
                        ) : (
                            <ListItems onClick={() => redirect('/login')}>
                                <StyledLink>login</StyledLink>
                            </ListItems>
                        )}
                    </RightSection>
                </>
            </ShadowContainer>
        </NavContainer>
    );
};

const NavContainer = styled.div`
  display: flex;
  flex: 1;

  flex-direction: row;
  box-shadow: inset 0 0 0 0 red;
  width: 100%;

  top: 0;
  position: fixed;

  opacity: ${(props) => (props.out ? 0 : 1)};

  transition: opacity 0.5s;
`;

const ShadowContainer = styled.div`
  display: flex;
  flex: 1;
  width: 100%;
  box-shadow: inset 0 0 0 2000px rgb(0 0 0 / 38%);
  background-color: rgb(0, 0, 0, ${(props) => (props.showShadow ? '0.1' : '0')});

  padding: 0 20px 0 20px;
`;

const LeftSection = styled.div`
  display: flex;
  flex: 1;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;

  height: 87px;
`;

const RightSection = styled.div`
  display: flex;
  flex: 1;
  flex-direction: row;
  justify-content: flex-end;
`;

const ListItems = styled.li`
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 50px;

  height: 100%;

  &:hover {
    border-bottom: 4px solid white;
    cursor: pointer;
  }
`;

const StyledLink = styled(Link)`
  text-transform: uppercase;
  color: white;
  text-decoration: none;
  justify-content: center;
  align-items: center;
`;

export default NavBar;
