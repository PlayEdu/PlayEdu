import { useEffect } from 'react';
import { isFunction } from '../utils';
import isDev from '../utils/isDev';
var useMount = function (fn) {
    if (isDev) {
        if (!isFunction(fn)) {
            console.error("useMount: parameter `fn` expected to be a function, but got \"".concat(typeof fn, "\"."));
        }
    }
    useEffect(function () {
        var result = fn === null || fn === void 0 ? void 0 : fn();
        // If fn returns a Promise, don't return it as cleanup function
        if (result && typeof result === 'object' && typeof result.then === 'function') {
            return;
        }
        return result;
    }, []);
};
export default useMount;
