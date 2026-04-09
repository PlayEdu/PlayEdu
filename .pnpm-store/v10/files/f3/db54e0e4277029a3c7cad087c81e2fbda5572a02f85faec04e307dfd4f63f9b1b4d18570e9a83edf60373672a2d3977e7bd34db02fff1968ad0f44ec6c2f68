import { getTargetElement } from '../utils/domTarget';
import useDeepCompareEffectWithTarget from '../utils/useDeepCompareWithTarget';
import useLatest from '../useLatest';
var useMutationObserver = function (callback, target, options) {
    if (options === void 0) { options = {}; }
    var callbackRef = useLatest(callback);
    useDeepCompareEffectWithTarget(function () {
        var element = getTargetElement(target);
        if (!element) {
            return;
        }
        var observer = new MutationObserver(callbackRef.current);
        observer.observe(element, options);
        return function () {
            observer === null || observer === void 0 ? void 0 : observer.disconnect();
        };
    }, [options], target);
};
export default useMutationObserver;
