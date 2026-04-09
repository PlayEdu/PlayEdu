import { useCallback, useEffect, useRef } from 'react';
import useLatest from '../useLatest';
import { isNumber } from '../utils';
var setRafInterval = function (callback, delay) {
    if (delay === void 0) { delay = 0; }
    if (typeof requestAnimationFrame === 'undefined') {
        return {
            id: setInterval(callback, delay),
        };
    }
    var start = Date.now();
    var handle = {
        id: 0,
    };
    var loop = function () {
        var current = Date.now();
        handle.id = requestAnimationFrame(loop);
        if (current - start >= delay) {
            callback();
            start = Date.now();
        }
    };
    handle.id = requestAnimationFrame(loop);
    return handle;
};
var cancelAnimationFrameIsNotDefined = function (t) {
    return typeof cancelAnimationFrame === 'undefined';
};
var clearRafInterval = function (handle) {
    if (cancelAnimationFrameIsNotDefined(handle.id)) {
        return clearInterval(handle.id);
    }
    cancelAnimationFrame(handle.id);
};
function useRafInterval(fn, delay, options) {
    var immediate = options === null || options === void 0 ? void 0 : options.immediate;
    var fnRef = useLatest(fn);
    var timerRef = useRef(undefined);
    var clear = useCallback(function () {
        if (timerRef.current) {
            clearRafInterval(timerRef.current);
        }
    }, []);
    useEffect(function () {
        if (!isNumber(delay) || delay < 0) {
            return;
        }
        if (immediate) {
            fnRef.current();
        }
        timerRef.current = setRafInterval(function () {
            fnRef.current();
        }, delay);
        return clear;
    }, [delay]);
    return clear;
}
export default useRafInterval;
