import { __read } from "tslib";
import { useState } from 'react';
import useEventListener from '../useEventListener';
import isBrowser from '../utils/isBrowser';
var getVisibility = function () {
    if (!isBrowser) {
        return 'visible';
    }
    return document.visibilityState;
};
function useDocumentVisibility() {
    var _a = __read(useState(getVisibility), 2), documentVisibility = _a[0], setDocumentVisibility = _a[1];
    useEventListener('visibilitychange', function () {
        setDocumentVisibility(getVisibility());
    }, {
        target: function () { return document; },
    });
    return documentVisibility;
}
export default useDocumentVisibility;
