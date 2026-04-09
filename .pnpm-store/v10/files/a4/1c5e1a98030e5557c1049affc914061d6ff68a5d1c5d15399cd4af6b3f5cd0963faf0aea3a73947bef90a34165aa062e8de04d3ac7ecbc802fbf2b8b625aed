import { __read } from "tslib";
import useRafState from '../useRafState';
import useEventListener from '../useEventListener';
import { getTargetElement } from '../utils/domTarget';
var initState = {
    screenX: NaN,
    screenY: NaN,
    clientX: NaN,
    clientY: NaN,
    pageX: NaN,
    pageY: NaN,
    elementX: NaN,
    elementY: NaN,
    elementH: NaN,
    elementW: NaN,
    elementPosX: NaN,
    elementPosY: NaN,
};
export default (function (target) {
    var _a = __read(useRafState(initState), 2), state = _a[0], setState = _a[1];
    useEventListener('mousemove', function (event) {
        var screenX = event.screenX, screenY = event.screenY, clientX = event.clientX, clientY = event.clientY, pageX = event.pageX, pageY = event.pageY;
        var newState = {
            screenX: screenX,
            screenY: screenY,
            clientX: clientX,
            clientY: clientY,
            pageX: pageX,
            pageY: pageY,
            elementX: NaN,
            elementY: NaN,
            elementH: NaN,
            elementW: NaN,
            elementPosX: NaN,
            elementPosY: NaN,
        };
        var targetElement = getTargetElement(target);
        if (targetElement) {
            var _a = targetElement.getBoundingClientRect(), left = _a.left, top_1 = _a.top, width = _a.width, height = _a.height;
            newState.elementPosX = left + window.pageXOffset;
            newState.elementPosY = top_1 + window.pageYOffset;
            newState.elementX = pageX - newState.elementPosX;
            newState.elementY = pageY - newState.elementPosY;
            newState.elementW = width;
            newState.elementH = height;
        }
        setState(newState);
    }, {
        target: function () { return document; },
    });
    return state;
});
