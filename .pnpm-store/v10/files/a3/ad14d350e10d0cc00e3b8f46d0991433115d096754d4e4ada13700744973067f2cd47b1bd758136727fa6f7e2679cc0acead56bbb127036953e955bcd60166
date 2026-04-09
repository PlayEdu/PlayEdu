import { useEffect } from 'react';
import useLatest from '../useLatest';
import { isFunction } from '../utils';
import isDev from '../utils/isDev';
var useUnmount = function (fn) {
    if (isDev) {
        if (!isFunction(fn)) {
            console.error("useUnmount expected parameter is a function, got ".concat(typeof fn));
        }
    }
    var fnRef = useLatest(fn);
    useEffect(function () { return function () {
        fnRef.current();
    }; }, []);
};
export default useUnmount;
