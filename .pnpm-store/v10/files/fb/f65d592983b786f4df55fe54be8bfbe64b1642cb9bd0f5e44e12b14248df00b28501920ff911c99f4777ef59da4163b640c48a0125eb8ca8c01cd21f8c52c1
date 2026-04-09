import { useRef } from 'react';
var useRetryPlugin = function (fetchInstance, _a) {
    var retryInterval = _a.retryInterval, retryCount = _a.retryCount;
    var timerRef = useRef(undefined);
    var countRef = useRef(0);
    var triggerByRetry = useRef(false);
    if (!retryCount) {
        return {};
    }
    return {
        onBefore: function () {
            if (!triggerByRetry.current) {
                countRef.current = 0;
            }
            triggerByRetry.current = false;
            if (timerRef.current) {
                clearTimeout(timerRef.current);
            }
        },
        onSuccess: function () {
            countRef.current = 0;
        },
        onError: function () {
            countRef.current += 1;
            if (retryCount === -1 || countRef.current <= retryCount) {
                // Exponential backoff
                var timeout = retryInterval !== null && retryInterval !== void 0 ? retryInterval : Math.min(1000 * Math.pow(2, countRef.current), 30000);
                timerRef.current = setTimeout(function () {
                    triggerByRetry.current = true;
                    fetchInstance.refresh();
                }, timeout);
            }
            else {
                countRef.current = 0;
            }
        },
        onCancel: function () {
            countRef.current = 0;
            if (timerRef.current) {
                clearTimeout(timerRef.current);
            }
        },
    };
};
export default useRetryPlugin;
