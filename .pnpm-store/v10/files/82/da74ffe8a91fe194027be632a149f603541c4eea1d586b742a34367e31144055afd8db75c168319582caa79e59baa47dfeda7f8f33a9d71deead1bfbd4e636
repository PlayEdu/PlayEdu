import { __assign, __read, __rest } from "tslib";
import Cookies from 'js-cookie';
import { useState } from 'react';
import useMemoizedFn from '../useMemoizedFn';
import { isFunction, isString } from '../utils';
function useCookieState(cookieKey, options) {
    if (options === void 0) { options = {}; }
    var _a = __read(useState(function () {
        var cookieValue = Cookies.get(cookieKey);
        if (isString(cookieValue)) {
            return cookieValue;
        }
        if (isFunction(options.defaultValue)) {
            return options.defaultValue();
        }
        return options.defaultValue;
    }), 2), state = _a[0], setState = _a[1];
    var updateState = useMemoizedFn(function (newValue, newOptions) {
        if (newOptions === void 0) { newOptions = {}; }
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        var _a = __assign(__assign({}, options), newOptions), defaultValue = _a.defaultValue, restOptions = __rest(_a, ["defaultValue"]);
        var value = isFunction(newValue) ? newValue(state) : newValue;
        setState(value);
        if (value === undefined) {
            Cookies.remove(cookieKey);
        }
        else {
            Cookies.set(cookieKey, value, restOptions);
        }
    });
    return [state, updateState];
}
export default useCookieState;
