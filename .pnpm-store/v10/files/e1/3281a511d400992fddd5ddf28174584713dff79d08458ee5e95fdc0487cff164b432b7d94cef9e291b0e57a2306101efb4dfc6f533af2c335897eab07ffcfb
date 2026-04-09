import { useMemo, useRef } from 'react';
import { isFunction } from '../utils';
import isDev from '../utils/isDev';
var useMemoizedFn = function (fn) {
    if (isDev) {
        if (!isFunction(fn)) {
            console.error("useMemoizedFn expected parameter is a function, got ".concat(typeof fn));
        }
    }
    var fnRef = useRef(fn);
    // why not write `fnRef.current = fn`?
    // https://github.com/alibaba/hooks/issues/728
    fnRef.current = useMemo(function () { return fn; }, [fn]);
    var memoizedFn = useRef(undefined);
    if (!memoizedFn.current) {
        memoizedFn.current = function () {
            var args = [];
            for (var _i = 0; _i < arguments.length; _i++) {
                args[_i] = arguments[_i];
            }
            return fnRef.current.apply(this, args);
        };
    }
    return memoizedFn.current;
};
export default useMemoizedFn;
