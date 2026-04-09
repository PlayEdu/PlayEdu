import { __read } from "tslib";
import { useCallback, useRef, useState } from 'react';
import useUnmount from '../useUnmount';
function useRafState(initialState) {
    var ref = useRef(0);
    var _a = __read(useState(initialState), 2), state = _a[0], setState = _a[1];
    var setRafState = useCallback(function (value) {
        cancelAnimationFrame(ref.current);
        ref.current = requestAnimationFrame(function () {
            setState(value);
        });
    }, []);
    useUnmount(function () {
        cancelAnimationFrame(ref.current);
    });
    return [state, setRafState];
}
export default useRafState;
