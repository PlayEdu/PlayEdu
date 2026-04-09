import { __read, __spreadArray } from "tslib";
import { useMemo, useRef } from 'react';
import { isFunction } from '../utils';
import useMemoizedFn from '../useMemoizedFn';
import useUpdate from '../useUpdate';
function useControllableValue(defaultProps, options) {
    if (options === void 0) { options = {}; }
    var props = defaultProps !== null && defaultProps !== void 0 ? defaultProps : {};
    var defaultValue = options.defaultValue, _a = options.defaultValuePropName, defaultValuePropName = _a === void 0 ? 'defaultValue' : _a, _b = options.valuePropName, valuePropName = _b === void 0 ? 'value' : _b, _c = options.trigger, trigger = _c === void 0 ? 'onChange' : _c;
    var value = props[valuePropName];
    var isControlled = Object.prototype.hasOwnProperty.call(props, valuePropName);
    var initialValue = useMemo(function () {
        if (isControlled) {
            return value;
        }
        if (Object.prototype.hasOwnProperty.call(props, defaultValuePropName)) {
            return props[defaultValuePropName];
        }
        return defaultValue;
    }, []);
    var stateRef = useRef(initialValue);
    if (isControlled) {
        stateRef.current = value;
    }
    var update = useUpdate();
    function setState(v) {
        var args = [];
        for (var _i = 1; _i < arguments.length; _i++) {
            args[_i - 1] = arguments[_i];
        }
        var r = isFunction(v) ? v(stateRef.current) : v;
        if (!isControlled) {
            stateRef.current = r;
            update();
        }
        if (props[trigger]) {
            props[trigger].apply(props, __spreadArray([r], __read(args), false));
        }
    }
    return [stateRef.current, useMemoizedFn(setState)];
}
export default useControllableValue;
