import { __read } from "tslib";
import { useEffect, useState } from 'react';
import useDebounceFn from '../useDebounceFn';
function useDebounce(value, options) {
    var _a = __read(useState(value), 2), debounced = _a[0], setDebounced = _a[1];
    var run = useDebounceFn(function () {
        setDebounced(value);
    }, options).run;
    useEffect(function () {
        run();
    }, [value]);
    return debounced;
}
export default useDebounce;
