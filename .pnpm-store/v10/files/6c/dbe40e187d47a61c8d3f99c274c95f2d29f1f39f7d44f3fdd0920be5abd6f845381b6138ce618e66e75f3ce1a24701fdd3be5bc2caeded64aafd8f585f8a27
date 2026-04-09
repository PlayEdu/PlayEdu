import { __read, __spreadArray } from "tslib";
import throttle from 'lodash/throttle';
import { useEffect, useRef } from 'react';
var useThrottlePlugin = function (fetchInstance, _a) {
    var throttleWait = _a.throttleWait, throttleLeading = _a.throttleLeading, throttleTrailing = _a.throttleTrailing;
    var throttledRef = useRef(undefined);
    var options = {};
    if (throttleLeading !== undefined) {
        options.leading = throttleLeading;
    }
    if (throttleTrailing !== undefined) {
        options.trailing = throttleTrailing;
    }
    useEffect(function () {
        if (throttleWait) {
            var _originRunAsync_1 = fetchInstance.runAsync.bind(fetchInstance);
            throttledRef.current = throttle(function (callback) {
                callback();
            }, throttleWait, options);
            // throttle runAsync should be promise
            // https://github.com/lodash/lodash/issues/4400#issuecomment-834800398
            fetchInstance.runAsync = function () {
                var args = [];
                for (var _i = 0; _i < arguments.length; _i++) {
                    args[_i] = arguments[_i];
                }
                return new Promise(function (resolve, reject) {
                    var _a;
                    (_a = throttledRef.current) === null || _a === void 0 ? void 0 : _a.call(throttledRef, function () {
                        _originRunAsync_1.apply(void 0, __spreadArray([], __read(args), false)).then(resolve)
                            .catch(reject);
                    });
                });
            };
            return function () {
                var _a;
                fetchInstance.runAsync = _originRunAsync_1;
                (_a = throttledRef.current) === null || _a === void 0 ? void 0 : _a.cancel();
            };
        }
    }, [throttleWait, throttleLeading, throttleTrailing]);
    if (!throttleWait) {
        return {};
    }
    return {
        onCancel: function () {
            var _a;
            (_a = throttledRef.current) === null || _a === void 0 ? void 0 : _a.cancel();
        },
    };
};
export default useThrottlePlugin;
