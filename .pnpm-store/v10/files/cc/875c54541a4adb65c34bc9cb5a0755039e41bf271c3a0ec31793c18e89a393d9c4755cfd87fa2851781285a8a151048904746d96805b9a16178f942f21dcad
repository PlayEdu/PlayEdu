import { useRef } from 'react';
var useLoadingDelayPlugin = function (fetchInstance, _a) {
    var loadingDelay = _a.loadingDelay, ready = _a.ready;
    var timerRef = useRef(undefined);
    if (!loadingDelay) {
        return {};
    }
    var cancelTimeout = function () {
        if (timerRef.current) {
            clearTimeout(timerRef.current);
        }
    };
    return {
        onBefore: function () {
            cancelTimeout();
            // Two cases:
            // 1. ready === undefined
            // 2. ready === true
            if (ready !== false) {
                timerRef.current = setTimeout(function () {
                    fetchInstance.setState({
                        loading: true,
                    });
                }, loadingDelay);
            }
            return {
                loading: false,
            };
        },
        onFinally: function () {
            cancelTimeout();
        },
        onCancel: function () {
            cancelTimeout();
        },
    };
};
export default useLoadingDelayPlugin;
