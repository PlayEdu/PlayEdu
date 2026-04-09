"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _react = require("react");
var _useMemoizedFn = _interopRequireDefault(require("../useMemoizedFn"));
var _utils = require("../utils");
var useTimeout = function useTimeout(fn, delay) {
  var timerCallback = (0, _useMemoizedFn["default"])(fn);
  var timerRef = (0, _react.useRef)(null);
  var clear = (0, _react.useCallback)(function () {
    if (timerRef.current) {
      clearTimeout(timerRef.current);
    }
  }, []);
  (0, _react.useEffect)(function () {
    if (!(0, _utils.isNumber)(delay) || delay < 0) {
      return;
    }
    timerRef.current = setTimeout(timerCallback, delay);
    return clear;
  }, [delay]);
  return clear;
};
var _default = exports["default"] = useTimeout;