import { __read } from "tslib";
import { useMemo, useState } from 'react';
function useToggle(defaultValue, reverseValue) {
    if (defaultValue === void 0) { defaultValue = false; }
    var _a = __read(useState(defaultValue), 2), state = _a[0], setState = _a[1];
    var actions = useMemo(function () {
        var reverseValueOrigin = (reverseValue === undefined ? !defaultValue : reverseValue);
        var toggle = function () { return setState(function (s) { return (s === defaultValue ? reverseValueOrigin : defaultValue); }); };
        var set = function (value) { return setState(value); };
        var setLeft = function () { return setState(defaultValue); };
        var setRight = function () { return setState(reverseValueOrigin); };
        return {
            toggle: toggle,
            set: set,
            setLeft: setLeft,
            setRight: setRight,
        };
        // useToggle ignore value change
        // }, [defaultValue, reverseValue]);
    }, []);
    return [state, actions];
}
export default useToggle;
