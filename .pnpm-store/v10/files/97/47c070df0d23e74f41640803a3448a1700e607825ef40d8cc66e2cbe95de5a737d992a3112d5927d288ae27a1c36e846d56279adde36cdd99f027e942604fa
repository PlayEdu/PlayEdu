import { __read } from "tslib";
import { useRef, useState } from 'react';
import { isFunction } from '../utils';
import useMemoizedFn from '../useMemoizedFn';
import useCreation from '../useCreation';
var useResetState = function (initialState) {
    var initialStateRef = useRef(initialState);
    var initialStateMemo = useCreation(function () {
        return isFunction(initialStateRef.current) ? initialStateRef.current() : initialStateRef.current;
    }, []);
    var _a = __read(useState(initialStateMemo), 2), state = _a[0], setState = _a[1];
    var resetState = useMemoizedFn(function () {
        setState(initialStateMemo);
    });
    return [state, setState, resetState];
};
export default useResetState;
