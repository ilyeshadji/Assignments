import { createGlobalStyle } from 'styled-components';
import { setColor, setFont, setRem } from './styles';

const Globals = createGlobalStyle`

    /* Fonts import */
    @import url('https://fonts.googleapis.com/css2?family=Courgette|Lato:400,700');
    
    *{
        margin: 0;
        padding: 0;

        /* When you apply border, the size is of the exterior of the border */
        box-sizing: border-box;
    }
    main{
        margin: 80px 5px 20px 5px;
    }

    body{
        font-size: 100%;
        color: ${setColor.primary};
        background: ${setColor.white};
        ${setFont.main};
    }

    h1{
        font-size: 3em;
        line-height: 1.2;
        margin-bottom: 0.5em;
    }

    h2{
        font-size: 2em;
        margin-bottom: 0.75em;
    }
    h3{
        font-size: 1em;
        line-height: 1;
    }
    h4{
        font-size: 1.2em;
        line-height: 1.2;
        margin-bottom: 1.25em;
        font-weight: bold;
    }
    h5{
        font-size: 1em;
        margin-bottom: 1.5em;
        font-weight: bold;
    }
    h6{
        font-size: 1em;
        font-weight: bold;
    }

    p{
        line-height: 1.5;
        margin: 0 0 0 0;
        color: ${setColor.primary}
    }

    input{
        font-family: ${setFont.primary}
    }

    .hide-password{
        color: ${setColor.lightGrey};
        size:${setRem(25)};
    }

    .hide-password:hover{
        color: ${setColor.secondary};
        cursor: pointer;
    }  

    .see-password{
        color: ${setColor.secondary};
    }

    .see-password:hover{
        color: ${setColor.lightGrey};
        cursor: pointer;
    }

      //Header animation
    .slide-in-left-enter {
        transform: translateX(-100%);
    }

    .slide-in-left-enter-active {
        transform: translateX(0);
        opacity: 1;
        transition: all 200ms;
     }

    .slide-in-left-exit {
        transform: translateX(0%);
        opacity: 1;
    }

    .slide-in-left-exit-active {
        transform: translateX(-100%);
        opacity: 0;
        transition: all 200ms;
    }

    .ReactModal__Overlay {
        opacity: 0;
        transition: opacity 500ms ease-in-out;
    }

    .ReactModal__Overlay--after-open{
        opacity: 1;
    }

    .ReactModal__Overlay--before-close{
        opacity: 0;
    }
`;

export default Globals;
