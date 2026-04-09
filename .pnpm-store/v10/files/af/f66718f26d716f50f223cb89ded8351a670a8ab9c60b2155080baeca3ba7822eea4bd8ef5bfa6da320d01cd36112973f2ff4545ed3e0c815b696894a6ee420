import { __read } from "tslib";
import { useEffect, useState } from 'react';
import useThrottleFn from '../useThrottleFn';
import useUpdateEffect from '../useUpdateEffect';
function useThrottleEffect(effect, deps, options) {
    var _a = __read(useState({}), 2), flag = _a[0], setFlag = _a[1];
    var run = useThrottleFn(function () {
        setFlag({});
    }, options).run;
    useEffect(function () {
        return run();
    }, deps);
    useUpdateEffect(effect, [flag]);
}
export default useThrottleEffect;
