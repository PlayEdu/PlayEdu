import { __assign } from "tslib";
import { StrictMode } from 'react';
import { renderHook } from '@testing-library/react';
export * from '@testing-library/react';
var Wrapper = process.env.REACT_MODE === 'strict' ? StrictMode : undefined;
var customRender = function (ui, options) {
    return renderHook(ui, __assign({ wrapper: Wrapper }, options));
};
export { customRender as renderHook };
