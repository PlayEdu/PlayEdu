import { __read } from "tslib";
import { useCallback, useState } from 'react';
import useLatest from '../useLatest';
import { isFunction } from '../utils';
function useEventTarget(options) {
    var _a = options || {}, initialValue = _a.initialValue, transformer = _a.transformer;
    var _b = __read(useState(initialValue), 2), value = _b[0], setValue = _b[1];
    var transformerRef = useLatest(transformer);
    var reset = useCallback(function () { return setValue(initialValue); }, []);
    var onChange = useCallback(function (e) {
        var _value = e.target.value;
        if (isFunction(transformerRef.current)) {
            return setValue(transformerRef.current(_value));
        }
        // no transformer => U and T should be the same
        return setValue(_value);
    }, []);
    return [
        value,
        {
            onChange: onChange,
            reset: reset,
        },
    ];
}
export default useEventTarget;
