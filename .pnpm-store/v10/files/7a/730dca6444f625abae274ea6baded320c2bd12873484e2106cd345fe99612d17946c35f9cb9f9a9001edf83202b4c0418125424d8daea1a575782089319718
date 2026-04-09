"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _react = require("react");
var _useLatest = _interopRequireDefault(require("../useLatest"));
var _utils = require("../utils");
var setRafInterval = function setRafInterval(callback, delay) {
  if (delay === void 0) {
    delay = 0;
  }
  if (typeof requestAnimationFrame === 'undefined') {
    return {
      id: setInterval(callback, delay)
    };
  }
  var start = Date.now();
  var handle = {
    id: 0
  };
  var _loop = function loop() {
    var current = Date.now();
    handle.id = requestAnimationFrame(_loop);
    if (current - start >= delay) {
      callback();
      start = Date.now();
    }
  };
  handle.id = requestAnimationFrame(_loop);
  return handle;
};
var cancelAnimationFrameIsNotDefined = function cancelAnimationFrameIsNotDefined(t) {
  return typeof cancelAnimationFrame === 'undefined';
};
var clearRafInterval = function clearRafInterval(handle) {
  if (cancelAnimationFrameIsNotDefined(handle.id)) {
    return clearInterval(handle.id);
  }
  cancelAnimationFrame(handle.id);
};
function useRafInterval(fn, delay, options) {
  var immediate = options === null || options === void 0 ? void 0 : options.immediate;
  var fnRef = (0, _useLatest["default"])(fn);
  var timerRef = (0, _react.useRef)(undefined);
  var clear = (0, _react.useCallback)(function () {
    if (timerRef.current) {
      clearRafInterval(timerRef.current);
    }
  }, []);
  (0, _react.useEffect)(function () {
    if (!(0, _utils.isNumber)(delay) || delay < 0) {
      return;
    }
    if (immediate) {
      fnRef.current();
    }
    timerRef.current = setRafInterval(function () {
      fnRef.current();
    }, delay);
    return clear;
  }, [delay]);
  return clear;
}
var _default = exports["default"] = useRafInterval;