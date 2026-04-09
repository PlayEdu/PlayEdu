"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _screenfull = _interopRequireDefault(require("screenfull"));
var _useLatest = _interopRequireDefault(require("../useLatest"));
var _useMemoizedFn = _interopRequireDefault(require("../useMemoizedFn"));
var _domTarget = require("../utils/domTarget");
var _utils = require("../utils");
var useFullscreen = function useFullscreen(target, options) {
  var _a = options || {},
    onExit = _a.onExit,
    onEnter = _a.onEnter,
    _b = _a.pageFullscreen,
    pageFullscreen = _b === void 0 ? false : _b;
  var _c = (0, _utils.isBoolean)(pageFullscreen) || !pageFullscreen ? {} : pageFullscreen,
    _d = _c.className,
    className = _d === void 0 ? 'ahooks-page-fullscreen' : _d,
    _e = _c.zIndex,
    zIndex = _e === void 0 ? 999999 : _e;
  var onExitRef = (0, _useLatest["default"])(onExit);
  var onEnterRef = (0, _useLatest["default"])(onEnter);
  // The state of full screen may be changed by other scripts/components,
  // so the initial value needs to be computed dynamically.
  var _f = (0, _tslib.__read)((0, _react.useState)(getIsFullscreen), 2),
    state = _f[0],
    setState = _f[1];
  var stateRef = (0, _react.useRef)(getIsFullscreen());
  function getIsFullscreen() {
    return _screenfull["default"].isEnabled && !!_screenfull["default"].element && _screenfull["default"].element === (0, _domTarget.getTargetElement)(target);
  }
  var invokeCallback = function invokeCallback(fullscreen) {
    var _a, _b;
    if (fullscreen) {
      (_a = onEnterRef.current) === null || _a === void 0 ? void 0 : _a.call(onEnterRef);
    } else {
      (_b = onExitRef.current) === null || _b === void 0 ? void 0 : _b.call(onExitRef);
    }
  };
  var updateFullscreenState = function updateFullscreenState(fullscreen) {
    // Prevent repeated calls when the state is not changed.
    if (stateRef.current !== fullscreen) {
      invokeCallback(fullscreen);
      setState(fullscreen);
      stateRef.current = fullscreen;
    }
  };
  var onScreenfullChange = function onScreenfullChange() {
    var fullscreen = getIsFullscreen();
    updateFullscreenState(fullscreen);
  };
  var togglePageFullscreen = function togglePageFullscreen(fullscreen) {
    var el = (0, _domTarget.getTargetElement)(target);
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
    } else {
      el.classList.remove(className);
      if (styleElem) {
        styleElem.remove();
      }
    }
    updateFullscreenState(fullscreen);
  };
  var enterFullscreen = function enterFullscreen() {
    var el = (0, _domTarget.getTargetElement)(target);
    if (!el) {
      return;
    }
    if (pageFullscreen) {
      togglePageFullscreen(true);
      return;
    }
    if (_screenfull["default"].isEnabled) {
      try {
        _screenfull["default"].request(el);
      } catch (error) {
        console.error(error);
      }
    }
  };
  var exitFullscreen = function exitFullscreen() {
    var el = (0, _domTarget.getTargetElement)(target);
    if (!el) {
      return;
    }
    if (pageFullscreen) {
      togglePageFullscreen(false);
      return;
    }
    if (_screenfull["default"].isEnabled && _screenfull["default"].element === el) {
      _screenfull["default"].exit();
    }
  };
  var toggleFullscreen = function toggleFullscreen() {
    if (state) {
      exitFullscreen();
    } else {
      enterFullscreen();
    }
  };
  (0, _react.useEffect)(function () {
    if (!_screenfull["default"].isEnabled || pageFullscreen) {
      return;
    }
    _screenfull["default"].on('change', onScreenfullChange);
    return function () {
      _screenfull["default"].off('change', onScreenfullChange);
    };
  }, []);
  return [state, {
    enterFullscreen: (0, _useMemoizedFn["default"])(enterFullscreen),
    exitFullscreen: (0, _useMemoizedFn["default"])(exitFullscreen),
    toggleFullscreen: (0, _useMemoizedFn["default"])(toggleFullscreen),
    isEnabled: _screenfull["default"].isEnabled
  }];
};
var _default = exports["default"] = useFullscreen;