import { useCallback, useEffect, useRef } from 'react';
import useMemoizedFn from '../useMemoizedFn';
import { isNumber } from '../utils';
var useInterval = function (fn, delay, options) {
    if (options === void 0) { options = {}; }
    var timerCallback = useMemoizedFn(fn);
    var timerRef = useRef(null);
    var clear = useCallback(function () {
        if (timerRef.current) {
            clearInterval(timerRef.current);
        }
    }, []);
    useEffect(function () {
        if (!isNumber(delay) || delay < 0) {
            return;
        }
        if (options.immediate) {
            timerCallback();
        }
        timerRef.current = setInterval(timerCallback, delay);
        return clear;
    }, [delay, options.immediate]);
    return clear;
};
export default useInterval;
