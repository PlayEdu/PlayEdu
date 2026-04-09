import { useRef } from 'react';
import useLatest from '../useLatest';
import { getTargetElement } from '../utils/domTarget';
import useEffectWithTarget from '../utils/useEffectWithTarget';
function useLongPress(onLongPress, target, _a) {
    var _b = _a === void 0 ? {} : _a, _c = _b.delay, delay = _c === void 0 ? 300 : _c, moveThreshold = _b.moveThreshold, onClick = _b.onClick, onLongPressEnd = _b.onLongPressEnd;
    var onLongPressRef = useLatest(onLongPress);
    var onClickRef = useLatest(onClick);
    var onLongPressEndRef = useLatest(onLongPressEnd);
    var timerRef = useRef(undefined);
    var isTriggeredRef = useRef(false);
    var pervPositionRef = useRef({ x: 0, y: 0 });
    var mousePressed = useRef(false);
    var touchPressed = useRef(false);
    var hasMoveThreshold = !!(((moveThreshold === null || moveThreshold === void 0 ? void 0 : moveThreshold.x) && moveThreshold.x > 0) ||
        ((moveThreshold === null || moveThreshold === void 0 ? void 0 : moveThreshold.y) && moveThreshold.y > 0));
    useEffectWithTarget(function () {
        var targetElement = getTargetElement(target);
        if (!(targetElement === null || targetElement === void 0 ? void 0 : targetElement.addEventListener)) {
            return;
        }
        var overThreshold = function (event) {
            var _a = getClientPosition(event), clientX = _a.clientX, clientY = _a.clientY;
            var offsetX = Math.abs(clientX - pervPositionRef.current.x);
            var offsetY = Math.abs(clientY - pervPositionRef.current.y);
            return !!(((moveThreshold === null || moveThreshold === void 0 ? void 0 : moveThreshold.x) && offsetX > moveThreshold.x) ||
                ((moveThreshold === null || moveThreshold === void 0 ? void 0 : moveThreshold.y) && offsetY > moveThreshold.y));
        };
        function getClientPosition(event) {
            if ('TouchEvent' in window && event instanceof TouchEvent) {
                return {
                    clientX: event.touches[0].clientX,
                    clientY: event.touches[0].clientY,
                };
            }
            if (event instanceof MouseEvent) {
                return {
                    clientX: event.clientX,
                    clientY: event.clientY,
                };
            }
            return { clientX: 0, clientY: 0 };
        }
        var createTimer = function (event) {
            timerRef.current = setTimeout(function () {
                onLongPressRef.current(event);
                isTriggeredRef.current = true;
            }, delay);
        };
        var onTouchStart = function (event) {
            if (touchPressed.current) {
                return;
            }
            touchPressed.current = true;
            if (hasMoveThreshold) {
                var _a = getClientPosition(event), clientX = _a.clientX, clientY = _a.clientY;
                pervPositionRef.current.x = clientX;
                pervPositionRef.current.y = clientY;
            }
            createTimer(event);
        };
        var onMouseDown = function (event) {
            var _a;
            if ((_a = event === null || event === void 0 ? void 0 : event.sourceCapabilities) === null || _a === void 0 ? void 0 : _a.firesTouchEvents) {
                return;
            }
            mousePressed.current = true;
            if (hasMoveThreshold) {
                pervPositionRef.current.x = event.clientX;
                pervPositionRef.current.y = event.clientY;
            }
            createTimer(event);
        };
        var onMove = function (event) {
            if (timerRef.current && overThreshold(event)) {
                clearTimeout(timerRef.current);
                timerRef.current = undefined;
            }
        };
        var onTouchEnd = function (event) {
            var _a;
            if (!touchPressed.current) {
                return;
            }
            touchPressed.current = false;
            if (timerRef.current) {
                clearTimeout(timerRef.current);
                timerRef.current = undefined;
            }
            if (isTriggeredRef.current) {
                (_a = onLongPressEndRef.current) === null || _a === void 0 ? void 0 : _a.call(onLongPressEndRef, event);
            }
            else if (onClickRef.current) {
                onClickRef.current(event);
            }
            isTriggeredRef.current = false;
        };
        var onMouseUp = function (event) {
            var _a, _b;
            if ((_a = event === null || event === void 0 ? void 0 : event.sourceCapabilities) === null || _a === void 0 ? void 0 : _a.firesTouchEvents) {
                return;
            }
            if (!mousePressed.current) {
                return;
            }
            mousePressed.current = false;
            if (timerRef.current) {
                clearTimeout(timerRef.current);
                timerRef.current = undefined;
            }
            if (isTriggeredRef.current) {
                (_b = onLongPressEndRef.current) === null || _b === void 0 ? void 0 : _b.call(onLongPressEndRef, event);
            }
            else if (onClickRef.current) {
                onClickRef.current(event);
            }
            isTriggeredRef.current = false;
        };
        var onMouseLeave = function (event) {
            var _a;
            if (!mousePressed.current) {
                return;
            }
            mousePressed.current = false;
            if (timerRef.current) {
                clearTimeout(timerRef.current);
                timerRef.current = undefined;
            }
            if (isTriggeredRef.current) {
                (_a = onLongPressEndRef.current) === null || _a === void 0 ? void 0 : _a.call(onLongPressEndRef, event);
                isTriggeredRef.current = false;
            }
        };
        targetElement.addEventListener('mousedown', onMouseDown);
        targetElement.addEventListener('mouseup', onMouseUp);
        targetElement.addEventListener('mouseleave', onMouseLeave);
        targetElement.addEventListener('touchstart', onTouchStart);
        targetElement.addEventListener('touchend', onTouchEnd);
        if (hasMoveThreshold) {
            targetElement.addEventListener('mousemove', onMove);
            targetElement.addEventListener('touchmove', onMove);
        }
        return function () {
            if (timerRef.current) {
                clearTimeout(timerRef.current);
                isTriggeredRef.current = false;
            }
            targetElement.removeEventListener('mousedown', onMouseDown);
            targetElement.removeEventListener('mouseup', onMouseUp);
            targetElement.removeEventListener('mouseleave', onMouseLeave);
            targetElement.removeEventListener('touchstart', onTouchStart);
            targetElement.removeEventListener('touchend', onTouchEnd);
            if (hasMoveThreshold) {
                targetElement.removeEventListener('mousemove', onMove);
                targetElement.removeEventListener('touchmove', onMove);
            }
        };
    }, [], target);
}
export default useLongPress;
