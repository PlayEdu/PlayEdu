import { useCallback, useEffect, useRef } from 'react';
import useLatest from '../useLatest';
import { isNumber } from '../utils';
var setRafTimeout = function (callback, delay) {
    if (delay === void 0) { delay = 0; }
    if (typeof requestAnimationFrame === 'undefined') {
        return {
            id: setTimeout(callback, delay),
        };
    }
    var handle = {
        id: 0,
    };
    var startTime = Date.now();
    var loop = function () {
        var current = Date.now();
        if (current - startTime >= delay) {
            callback();
        }
        else {
            handle.id = requestAnimationFrame(loop);
        }
    };
    handle.id = requestAnimationFrame(loop);
    return handle;
};
var cancelAnimationFrameIsNotDefined = function (t) {
    return typeof cancelAnimationFrame === 'undefined';
};
var clearRafTimeout = function (handle) {
    if (cancelAnimationFrameIsNotDefined(handle.id)) {
        return clearTimeout(handle.id);
    }
    cancelAnimationFrame(handle.id);
};
function useRafTimeout(fn, delay) {
    var fnRef = useLatest(fn);
    var timerRef = useRef(undefined);
    var clear = useCallback(function () {
        if (timerRef.current) {
            clearRafTimeout(timerRef.current);
        }
    }, []);
    useEffect(function () {
        if (!isNumber(delay) || delay < 0) {
            return;
        }
        timerRef.current = setRafTimeout(function () {
            fnRef.current();
        }, delay);
        return clear;
    }, [delay]);
    return clear;
}
export default useRafTimeout;
