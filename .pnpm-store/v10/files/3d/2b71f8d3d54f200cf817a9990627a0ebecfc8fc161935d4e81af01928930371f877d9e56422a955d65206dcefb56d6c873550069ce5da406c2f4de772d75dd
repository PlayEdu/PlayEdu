import { __read, __spreadArray } from "tslib";
export default function limit(fn, timespan) {
    var pending = false;
    return function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i] = arguments[_i];
        }
        if (pending)
            return;
        pending = true;
        fn.apply(void 0, __spreadArray([], __read(args), false));
        setTimeout(function () {
            pending = false;
        }, timespan);
    };
}
