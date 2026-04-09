import { __read } from "tslib";
import ResizeObserver from 'resize-observer-polyfill';
import useRafState from '../useRafState';
import { getTargetElement } from '../utils/domTarget';
import useIsomorphicLayoutEffectWithTarget from '../utils/useIsomorphicLayoutEffectWithTarget';
function useSize(target) {
    var _a = __read(useRafState(function () {
        var el = getTargetElement(target);
        return el ? { width: el.clientWidth, height: el.clientHeight } : undefined;
    }), 2), state = _a[0], setState = _a[1];
    useIsomorphicLayoutEffectWithTarget(function () {
        var el = getTargetElement(target);
        if (!el) {
            return;
        }
        var resizeObserver = new ResizeObserver(function (entries) {
            entries.forEach(function (entry) {
                var _a = entry.target, clientWidth = _a.clientWidth, clientHeight = _a.clientHeight;
                setState({ width: clientWidth, height: clientHeight });
            });
        });
        resizeObserver.observe(el);
        return function () {
            resizeObserver.disconnect();
        };
    }, [], target);
    return state;
}
export default useSize;
