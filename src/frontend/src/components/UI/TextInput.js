import React from 'react';
import styled from 'styled-components';

import {setColor} from '../../utils/styles';

const TextInput = (props) => {
    return (
        <TextInputContainer
            disabled={props.disabled}
            line={props.line}
            marginBottom={props.marginBottom}
            margin={props.margin}
        >
            <TextInputView line={props.line}>
                <LabelContainer line={props.line}>
                    <Label
                        labelSize={props.labelSize}
                        labelWeight={props.labelWeight}
                        line={props.line}
                    >
                        {props.label}
                    </Label>
                </LabelContainer>

                <InputContainer line={props.line}>
                    {props.disabled === true ? (
                        <DisabledInput
                            placeholder={props.placeholder}
                            type={props.type}
                            form={props.form}
                            name={props.name}
                            value={props.value}
                            onChange={props.onChange}
                            fontSize={props.fontSize}
                            line={props.line}
                        />
                    ) : (
                        <Input
                            placeholder={props.placeholder}
                            type={props.type}
                            form={props.form}
                            name={props.name}
                            value={props.value}
                            onChange={props.onChange}
                            fontSize={props.fontSize}
                            line={props.line}
                            style={props.inputStyle}
                        />
                    )}
                </InputContainer>
            </TextInputView>
        </TextInputContainer>
    );
};

const Input = styled.input.attrs((props) => ({
    type: props.type ? props.type : 'text',
    placeholder: props.placeholder ? props.placeholder : '',
    form: props.form ? props.form : '',
    name: props.name ? props.name : '',
    value: props.value ? props.value : '',
    onChange: props.onChange ? props.onChange : () => {
    },
}))`
  border: 0;
  border-bottom: ${(props) =>
          props.line ? `0.5px groove ${setColor.primary}` : ''};
  font-size: ${(props) => (props.fontSize ? props.fontSize : '')};
  margin-top: ${(props) => (props.marginTop ? props.marginTop : '-1vh')};
  outline: none;
  height: 100%;
  width: 100%;
  ${(props) => (props.inputStyle ? props.inputStyle : '')};
`;

const TextInputView = styled.div`
  display: flex;
  flex-direction: ${(props) => (props.line ? 'row' : 'column')};
  align-items: ${(props) => (props.line ? 'flex-end' : 'flex-start')};
  width: 100%;
`;

const TextInputContainer = styled.div`
  display: flex;
  align-items: flex-end;
  border-bottom: ${(props) =>
          props.disabled || props.line ? '' : `0.5px groove ${setColor.primary}`};
  margin-bottom: ${(props) =>
          props.marginBottom ? props.marginBottom : '2vh'};
  margin: ${(props) => (props.margin ? props.margin : '')};
  width: 100%;
  height: 100%;
`;

const DisabledInput = styled.input.attrs((props) => ({
    type: props.type ? props.type : 'text',
    placeholder: props.placeholder ? props.placeholder : '',
    form: props.form ? props.form : '',
    name: props.name ? props.name : '',
    value: props.value ? props.value : '',
    disabled: true,
    onChange: props.onChange ? props.onChange : () => {
    },
}))`
  border: 0px;
  font-size: ${(props) => (props.fontSize ? props.fontSize : '')};
  margin-top: ${(props) => (props.marginTop ? props.marginTop : '-1.5vh')};
  outline: none;
  height: 100%;
  width: 100%;
  background-color: ${setColor.white};
`;

const Label = styled.p`
  width: ${(props) => (props.line ? '100%' : '')};
  font-size: ${(props) => (props.labelSize ? props.labelSize : '')};
  font-weight: ${(props) => (props.labelWeight ? props.labelWeight : '')};
`;

const LabelContainer = styled.div`
  display: flex;
  height: ${(props) => (props.line ? '20px' : '')};
  margin-right: ${(props) => (props.line ? '5px' : '')};
  margin-bottom: ${(props) => (props.line ? '' : '15px')};
  white-space: nowrap;
`;

const InputContainer = styled.div`
  display: flex;
  width: 100%;
`;

export default TextInput;
