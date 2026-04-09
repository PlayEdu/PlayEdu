import { useCallback, useEffect, useRef } from 'react';
import useMemoizedFn from '../useMemoizedFn';
import { isNumber } from '../utils';
var useTimeout = function (fn, delay) {
    var timerCallback = useMemoizedFn(fn);
    var timerRef = useRef(null);
    var clear = useCallback(function () {
        if (timerRef.current) {
            clearTimeout(timerRef.current);
        }
    }, []);
    useEffect(function () {
        if (!isNumber(delay) || delay < 0) {
            return;
        }
        timerRef.current = setTimeout(timerCallback, delay);
        return clear;
    }, [delay]);
    return clear;
};
export default useTimeout;
