import { css } from 'styled-components';

export const sizes = {
  desktop: 1024,
  tablet: 601,
  phone: 360,
};

export const media = Object.keys(sizes).reduce((acc, label) => {
  acc[label] = (...args) => css`
    @media (min-width: ${sizes[label] / 16}em) {
      ${css(...args)}
    }
  `;
  return acc;
}, {});

export const setColor = {
  primary: 'rgba(0, 23, 48, 1)',
  lightPrimary: 'rgba(0, 23, 48, 0.9)',
  palePrimary: 'rgba(0, 23, 48, 0.5)',
  secondary: 'rgba(74, 215, 209, 1)',
  paleSecondary: 'rgba(74, 215, 209, 0.49)',
  red: 'rgba(237, 63, 63, 1)',
  white: 'rgba(255, 255, 255, 1)',
  paleRed: 'rgba(237, 63, 63, 0.47)',
  lightGrey: 'rgba(204,204,204,1)',
  grey: 'rgba(84, 84, 84, 0.8)',
  secondBlue: 'rgba(16,52,126,1)',
};

export const setFont = {
  main: "font-family: 'Lato', sans-serif;",
  secondary: "'Work Sans', sans-serif;",
  slanted: "font-family: 'Courgette', cursive;",
};

export const setFlex = ({ x = 'center ', y = 'center' } = {}) => {
  return `
        display:flex;
        align-items: ${y};
        justify-content: ${x};
    `;
};

//Takes the number in px and tranforms it in rem for more responsive results
export const setRem = (number = 16) => {
  return `
        ${number / 16}rem
    `;
};

export const setLetterSpacing = (number = 2) => {
  return `letter-spacing:${number}px`;
};

export const setBorder = ({
  width = '1px',
  style = 'groove',
  color = 'black',
} = {}) => {
  return `border:${width} ${style} ${color}`;
};

export const setTransition = ({
  property = 'all',
  time = '0.3s',
  timing = 'ease-in-out',
} = {}) => {
  return `transition: ${property} ${time} ${timing}`;
};

export const setShadow = {
  light: 'box-shadow: 3px 3px 5px 0px rgba(0,0,0,0.75);',
  dark: 'box-shadow: 6px 6px 5px 0px rgba(0,0,0,0.75);',
  darkest: 'box-shadow: 10px 10px 5px 0px rgba(0,0,0,0.75);',
};
