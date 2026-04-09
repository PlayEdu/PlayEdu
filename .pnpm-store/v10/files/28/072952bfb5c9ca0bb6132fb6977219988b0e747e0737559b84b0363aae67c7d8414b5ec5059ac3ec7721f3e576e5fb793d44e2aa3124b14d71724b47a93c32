import { useRef } from 'react';
import useUpdateEffect from '../../../useUpdateEffect';
import isDocumentVisible from '../utils/isDocumentVisible';
import subscribeReVisible from '../utils/subscribeReVisible';
var usePollingPlugin = function (fetchInstance, _a) {
    var pollingInterval = _a.pollingInterval, _b = _a.pollingWhenHidden, pollingWhenHidden = _b === void 0 ? true : _b, _c = _a.pollingErrorRetryCount, pollingErrorRetryCount = _c === void 0 ? -1 : _c;
    var timerRef = useRef(undefined);
    var unsubscribeRef = useRef(undefined);
    var countRef = useRef(0);
    var stopPolling = function () {
        var _a;
        if (timerRef.current) {
            clearTimeout(timerRef.current);
        }
        (_a = unsubscribeRef.current) === null || _a === void 0 ? void 0 : _a.call(unsubscribeRef);
    };
    useUpdateEffect(function () {
        if (!pollingInterval) {
            stopPolling();
        }
    }, [pollingInterval]);
    if (!pollingInterval) {
        return {};
    }
    return {
        onBefore: function () {
            stopPolling();
        },
        onError: function () {
            countRef.current += 1;
        },
        onSuccess: function () {
            countRef.current = 0;
        },
        onFinally: function () {
            if (pollingErrorRetryCount === -1 ||
                // When an error occurs, the request is not repeated after pollingErrorRetryCount retries
                (pollingErrorRetryCount !== -1 && countRef.current <= pollingErrorRetryCount)) {
                timerRef.current = setTimeout(function () {
                    // if pollingWhenHidden = false && document is hidden, then stop polling and subscribe revisible
                    if (!pollingWhenHidden && !isDocumentVisible()) {
                        unsubscribeRef.current = subscribeReVisible(function () {
                            fetchInstance.refresh();
                        });
                    }
                    else {
                        fetchInstance.refresh();
                    }
                }, pollingInterval);
            }
            else {
                countRef.current = 0;
            }
        },
        onCancel: function () {
            stopPolling();
        },
    };
};
export default usePollingPlugin;
