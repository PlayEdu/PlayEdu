import { __assign, __read } from "tslib";
import { useState } from 'react';
import useMemoizedFn from '../useMemoizedFn';
import { isFunction } from '../utils';
var useSetState = function (initialState) {
    var _a = __read(useState(initialState), 2), state = _a[0], setState = _a[1];
    var setMergeState = useMemoizedFn(function (patch) {
        setState(function (prevState) {
            var newState = isFunction(patch) ? patch(prevState) : patch;
            return newState ? __assign(__assign({}, prevState), newState) : prevState;
        });
    });
    return [state, setMergeState];
};
export default useSetState;
