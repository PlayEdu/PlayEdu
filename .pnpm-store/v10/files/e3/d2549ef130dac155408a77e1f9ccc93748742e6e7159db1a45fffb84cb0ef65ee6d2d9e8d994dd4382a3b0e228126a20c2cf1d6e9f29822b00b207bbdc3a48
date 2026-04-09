"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _react = require("react");
var _useMemoizedFn = _interopRequireDefault(require("../useMemoizedFn"));
var _utils = require("../utils");
var useInterval = function useInterval(fn, delay, options) {
  if (options === void 0) {
    options = {};
  }
  var timerCallback = (0, _useMemoizedFn["default"])(fn);
  var timerRef = (0, _react.useRef)(null);
  var clear = (0, _react.useCallback)(function () {
    if (timerRef.current) {
      clearInterval(timerRef.current);
    }
  }, []);
  (0, _react.useEffect)(function () {
    if (!(0, _utils.isNumber)(delay) || delay < 0) {
      return;
    }
    if (options.immediate) {
      timerCallback();
    }
    timerRef.current = setInterval(timerCallback, delay);
    return clear;
  }, [delay, options.immediate]);
  return clear;
};
var _default = exports["default"] = useInterval;