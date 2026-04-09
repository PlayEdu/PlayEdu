import { __values } from "tslib";
import { useRef, useEffect } from 'react';
var EventEmitter = /** @class */ (function () {
    function EventEmitter() {
        var _this = this;
        this.subscriptions = new Set();
        this.emit = function (val) {
            var e_1, _a;
            try {
                for (var _b = __values(_this.subscriptions), _c = _b.next(); !_c.done; _c = _b.next()) {
                    var subscription = _c.value;
                    subscription(val);
                }
            }
            catch (e_1_1) { e_1 = { error: e_1_1 }; }
            finally {
                try {
                    if (_c && !_c.done && (_a = _b.return)) _a.call(_b);
                }
                finally { if (e_1) throw e_1.error; }
            }
        };
        this.useSubscription = function (callback) {
            // eslint-disable-next-line react-hooks/rules-of-hooks
            var callbackRef = useRef(undefined);
            callbackRef.current = callback;
            // eslint-disable-next-line react-hooks/rules-of-hooks
            useEffect(function () {
                function subscription(val) {
                    if (callbackRef.current) {
                        callbackRef.current(val);
                    }
                }
                _this.subscriptions.add(subscription);
                return function () {
                    _this.subscriptions.delete(subscription);
                };
            }, []);
        };
    }
    return EventEmitter;
}());
export { EventEmitter };
function useEventEmitter() {
    var ref = useRef(undefined);
    if (!ref.current) {
        ref.current = new EventEmitter();
    }
    return ref.current;
}
export default useEventEmitter;
