import { __assign, __read, __rest, __values } from "tslib";
import 'intersection-observer';
import { useState } from 'react';
import { getTargetElement } from '../utils/domTarget';
import useEffectWithTarget from '../utils/useEffectWithTarget';
function useInViewport(target, options) {
    var _a = options || {}, callback = _a.callback, option = __rest(_a, ["callback"]);
    var _b = __read(useState(), 2), state = _b[0], setState = _b[1];
    var _c = __read(useState(), 2), ratio = _c[0], setRatio = _c[1];
    useEffectWithTarget(function () {
        var targets = Array.isArray(target) ? target : [target];
        var els = targets.map(function (element) { return getTargetElement(element); }).filter(Boolean);
        if (!els.length) {
            return;
        }
        var observer = new IntersectionObserver(function (entries) {
            var e_1, _a;
            try {
                for (var entries_1 = __values(entries), entries_1_1 = entries_1.next(); !entries_1_1.done; entries_1_1 = entries_1.next()) {
                    var entry = entries_1_1.value;
                    setRatio(entry.intersectionRatio);
                    setState(entry.isIntersecting);
                    callback === null || callback === void 0 ? void 0 : callback(entry);
                }
            }
            catch (e_1_1) { e_1 = { error: e_1_1 }; }
            finally {
                try {
                    if (entries_1_1 && !entries_1_1.done && (_a = entries_1.return)) _a.call(entries_1);
                }
                finally { if (e_1) throw e_1.error; }
            }
        }, __assign(__assign({}, option), { root: getTargetElement(options === null || options === void 0 ? void 0 : options.root) }));
        els.forEach(function (el) { return observer.observe(el); });
        return function () {
            observer.disconnect();
        };
    }, [options === null || options === void 0 ? void 0 : options.rootMargin, options === null || options === void 0 ? void 0 : options.threshold, callback], target);
    return [state, ratio];
}
export default useInViewport;
