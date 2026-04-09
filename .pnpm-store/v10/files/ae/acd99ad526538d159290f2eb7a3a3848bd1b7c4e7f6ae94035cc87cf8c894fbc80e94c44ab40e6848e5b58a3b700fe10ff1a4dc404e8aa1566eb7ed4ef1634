import { __read } from "tslib";
import { useEffect, useState, useRef } from 'react';
import screenfull from 'screenfull';
import useLatest from '../useLatest';
import useMemoizedFn from '../useMemoizedFn';
import { getTargetElement } from '../utils/domTarget';
import { isBoolean } from '../utils';
var useFullscreen = function (target, options) {
    var _a = options || {}, onExit = _a.onExit, onEnter = _a.onEnter, _b = _a.pageFullscreen, pageFullscreen = _b === void 0 ? false : _b;
    var _c = isBoolean(pageFullscreen) || !pageFullscreen ? {} : pageFullscreen, _d = _c.className, className = _d === void 0 ? 'ahooks-page-fullscreen' : _d, _e = _c.zIndex, zIndex = _e === void 0 ? 999999 : _e;
    var onExitRef = useLatest(onExit);
    var onEnterRef = useLatest(onEnter);
    // The state of full screen may be changed by other scripts/components,
    // so the initial value needs to be computed dynamically.
    var _f = __read(useState(getIsFullscreen), 2), state = _f[0], setState = _f[1];
    var stateRef = useRef(getIsFullscreen());
    function getIsFullscreen() {
        return (screenfull.isEnabled &&
            !!screenfull.element &&
            screenfull.element === getTargetElement(target));
    }
    var invokeCallback = function (fullscreen) {
        var _a, _b;
        if (fullscreen) {
            (_a = onEnterRef.current) === null || _a === void 0 ? void 0 : _a.call(onEnterRef);
        }
        else {
            (_b = onExitRef.current) === null || _b === void 0 ? void 0 : _b.call(onExitRef);
        }
    };
    var updateFullscreenState = function (fullscreen) {
        // Prevent repeated calls when the state is not changed.
        if (stateRef.current !== fullscreen) {
            invokeCallback(fullscreen);
            setState(fullscreen);
            stateRef.current = fullscreen;
        }
    };
    var onScreenfullChange = function () {
        var fullscreen = getIsFullscreen();
        updateFullscreenState(fullscreen);
    };
    var togglePageFullscreen = function (fullscreen) {
        var el = getTargetElement(target);
        if (!el) {
            return;
        }
        var styleElem = document.getElementById(className);
        if (fullscreen) {
            el.classList.add(className);
            if (!styleElem) {
                styleElem = document.createElement('style');
                styleElem.setAttribute('id', className);
                styleElem.textContent = "\n          .".concat(className, " {\n            position: fixed; left: 0; top: 0; right: 0; bottom: 0;\n            width: 100% !important; height: 100% !important;\n            z-index: ").concat(zIndex, ";\n          }");
                el.appendChild(styleElem);
            }
        }
        else {
            el.classList.remove(className);
            if (styleElem) {
                styleElem.remove();
            }
        }
        updateFullscreenState(fullscreen);
    };
    var enterFullscreen = function () {
        var el = getTargetElement(target);
        if (!el) {
            return;
        }
        if (pageFullscreen) {
            togglePageFullscreen(true);
            return;
        }
        if (screenfull.isEnabled) {
            try {
                screenfull.request(el);
            }
            catch (error) {
                console.error(error);
            }
        }
    };
    var exitFullscreen = function () {
        var el = getTargetElement(target);
        if (!el) {
            return;
        }
        if (pageFullscreen) {
            togglePageFullscreen(false);
            return;
        }
        if (screenfull.isEnabled && screenfull.element === el) {
            screenfull.exit();
        }
    };
    var toggleFullscreen = function () {
        if (state) {
            exitFullscreen();
        }
        else {
            enterFullscreen();
        }
    };
    useEffect(function () {
        if (!screenfull.isEnabled || pageFullscreen) {
            return;
        }
        screenfull.on('change', onScreenfullChange);
        return function () {
            screenfull.off('change', onScreenfullChange);
        };
    }, []);
    return [
        state,
        {
            enterFullscreen: useMemoizedFn(enterFullscreen),
            exitFullscreen: useMemoizedFn(exitFullscreen),
            toggleFullscreen: useMemoizedFn(toggleFullscreen),
            isEnabled: screenfull.isEnabled,
        },
    ];
};
export default useFullscreen;
