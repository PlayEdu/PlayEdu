import { __read, __spreadArray } from "tslib";
import { useRef } from 'react';
import useUpdateEffect from '../../../useUpdateEffect';
// support refreshDeps & ready
var useAutoRunPlugin = function (fetchInstance, _a) {
    var manual = _a.manual, _b = _a.ready, ready = _b === void 0 ? true : _b, _c = _a.defaultParams, defaultParams = _c === void 0 ? [] : _c, _d = _a.refreshDeps, refreshDeps = _d === void 0 ? [] : _d, refreshDepsAction = _a.refreshDepsAction;
    var hasAutoRun = useRef(false);
    hasAutoRun.current = false;
    useUpdateEffect(function () {
        if (!manual && ready) {
            hasAutoRun.current = true;
            fetchInstance.run.apply(fetchInstance, __spreadArray([], __read(defaultParams), false));
        }
    }, [ready]);
    useUpdateEffect(function () {
        if (hasAutoRun.current) {
            return;
        }
        if (!manual) {
            hasAutoRun.current = true;
            if (refreshDepsAction) {
                refreshDepsAction();
            }
            else {
                fetchInstance.refresh();
            }
        }
    }, __spreadArray([], __read(refreshDeps), false));
    return {
        onBefore: function () {
            if (!ready) {
                return {
                    stopNow: true,
                };
            }
        },
    };
};
useAutoRunPlugin.onInit = function (_a) {
    var _b = _a.ready, ready = _b === void 0 ? true : _b, manual = _a.manual;
    return {
        loading: !manual && ready,
    };
};
export default useAutoRunPlugin;
