import { __read } from "tslib";
import useBoolean from '../useBoolean';
import useEventListener from '../useEventListener';
export default (function (target, options) {
    var _a = options || {}, onEnter = _a.onEnter, onLeave = _a.onLeave, onChange = _a.onChange;
    var _b = __read(useBoolean(false), 2), state = _b[0], _c = _b[1], setTrue = _c.setTrue, setFalse = _c.setFalse;
    useEventListener('mouseenter', function () {
        onEnter === null || onEnter === void 0 ? void 0 : onEnter();
        setTrue();
        onChange === null || onChange === void 0 ? void 0 : onChange(true);
    }, {
        target: target,
    });
    useEventListener('mouseleave', function () {
        onLeave === null || onLeave === void 0 ? void 0 : onLeave();
        setFalse();
        onChange === null || onChange === void 0 ? void 0 : onChange(false);
    }, {
        target: target,
    });
    return state;
});
