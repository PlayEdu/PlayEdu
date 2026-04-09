import useLatest from '../useLatest';
import { getTargetElement } from '../utils/domTarget';
import useEffectWithTarget from '../utils/useEffectWithTarget';
function useEventListener(eventName, handler, options) {
    if (options === void 0) { options = {}; }
    var _a = options.enable, enable = _a === void 0 ? true : _a;
    var handlerRef = useLatest(handler);
    useEffectWithTarget(function () {
        if (!enable) {
            return;
        }
        var targetElement = getTargetElement(options.target, window);
        if (!(targetElement === null || targetElement === void 0 ? void 0 : targetElement.addEventListener)) {
            return;
        }
        var eventListener = function (event) {
            return handlerRef.current(event);
        };
        var eventNameArray = Array.isArray(eventName) ? eventName : [eventName];
        eventNameArray.forEach(function (event) {
            targetElement.addEventListener(event, eventListener, {
                capture: options.capture,
                once: options.once,
                passive: options.passive,
            });
        });
        return function () {
            eventNameArray.forEach(function (event) {
                targetElement.removeEventListener(event, eventListener, {
                    capture: options.capture,
                });
            });
        };
    }, [eventName, options.capture, options.once, options.passive, enable], options.target);
}
export default useEventListener;
