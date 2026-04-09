"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _react = require("react");
var _useLatest = _interopRequireDefault(require("../useLatest"));
var _utils = require("../utils");
var setRafTimeout = function setRafTimeout(callback, delay) {
  if (delay === void 0) {
    delay = 0;
  }
  if (typeof requestAnimationFrame === 'undefined') {
    return {
      id: setTimeout(callback, delay)
    };
  }
  var handle = {
    id: 0
  };
  var startTime = Date.now();
  var _loop = function loop() {
    var current = Date.now();
    if (current - startTime >= delay) {
      callback();
    } else {
      handle.id = requestAnimationFrame(_loop);
    }
  };
  handle.id = requestAnimationFrame(_loop);
  return handle;
};
var cancelAnimationFrameIsNotDefined = function cancelAnimationFrameIsNotDefined(t) {
  return typeof cancelAnimationFrame === 'undefined';
};
var clearRafTimeout = function clearRafTimeout(handle) {
  if (cancelAnimationFrameIsNotDefined(handle.id)) {
    return clearTimeout(handle.id);
  }
  cancelAnimationFrame(handle.id);
};
function useRafTimeout(fn, delay) {
  var fnRef = (0, _useLatest["default"])(fn);
  var timerRef = (0, _react.useRef)(undefined);
  var clear = (0, _react.useCallback)(function () {
    if (timerRef.current) {
      clearRafTimeout(timerRef.current);
    }
  }, []);
  (0, _react.useEffect)(function () {
    if (!(0, _utils.isNumber)(delay) || delay < 0) {
      return;
    }
    timerRef.current = setRafTimeout(function () {
      fnRef.current();
    }, delay);
    return clear;
  }, [delay]);
  return clear;
}
var _default = exports["default"] = useRafTimeout;