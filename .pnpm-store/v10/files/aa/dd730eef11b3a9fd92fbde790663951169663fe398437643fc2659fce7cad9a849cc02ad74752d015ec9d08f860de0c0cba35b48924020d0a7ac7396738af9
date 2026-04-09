import { __read } from "tslib";
import { useCallback, useState } from 'react';
import useUnmountedRef from '../useUnmountedRef';
function useSafeState(initialState) {
    var unmountedRef = useUnmountedRef();
    var _a = __read(useState(initialState), 2), state = _a[0], setState = _a[1];
    var setCurrentState = useCallback(function (currentState) {
        /** if component is unmounted, stop update */
        if (unmountedRef.current) {
            return;
        }
        setState(currentState);
    }, []);
    return [state, setCurrentState];
}
export default useSafeState;
