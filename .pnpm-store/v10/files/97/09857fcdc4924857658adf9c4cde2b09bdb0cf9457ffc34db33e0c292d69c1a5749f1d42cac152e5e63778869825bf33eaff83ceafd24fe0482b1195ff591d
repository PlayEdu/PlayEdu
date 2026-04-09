import isBrowser from '../../../utils/isBrowser';
import isDocumentVisible from './isDocumentVisible';
var listeners = new Set();
function subscribe(listener) {
    listeners.add(listener);
    return function unsubscribe() {
        listeners.has(listener) && listeners.delete(listener);
    };
}
if (isBrowser) {
    var revalidate = function () {
        if (!isDocumentVisible())
            return;
        listeners.forEach(function (listener) { return listener(); });
    };
    window.addEventListener('visibilitychange', revalidate, false);
}
export default subscribe;
