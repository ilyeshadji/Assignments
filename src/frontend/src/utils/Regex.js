export const emailRegex =
    /^([a-zA-Z0-9!#$%&'*+\/=?^_`{|}~-]+\.?)*[a-zA-Z0-9!#$%&'*+\/=?^_`{|}~-]+@(([a-zA-Z0-9][a-zA-Z0-9-]{0,61}[a-zA-Z0-9]\.)+[a-zA-Z]{2,}|([0-9]{1,3}\.){3}[0-9]{1,3})$/;
export const passwordRegex = /^(?=.*[0-9a-zA-Z])(?!.*(\w).*\1)^.{4,}$/;
export const skuRegex = /^[a-z0-9-]{1,100}$/;

