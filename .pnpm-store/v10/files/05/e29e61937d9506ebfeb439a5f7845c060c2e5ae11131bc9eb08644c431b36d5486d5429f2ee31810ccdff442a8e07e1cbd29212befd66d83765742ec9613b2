import { __read } from "tslib";
import { useEffect, useState } from 'react';
import useThrottleFn from '../useThrottleFn';
function useThrottle(value, options) {
    var _a = __read(useState(value), 2), throttled = _a[0], setThrottled = _a[1];
    var run = useThrottleFn(function () {
        setThrottled(value);
    }, options).run;
    useEffect(function () {
        run();
    }, [value]);
    return throttled;
}
export default useThrottle;
