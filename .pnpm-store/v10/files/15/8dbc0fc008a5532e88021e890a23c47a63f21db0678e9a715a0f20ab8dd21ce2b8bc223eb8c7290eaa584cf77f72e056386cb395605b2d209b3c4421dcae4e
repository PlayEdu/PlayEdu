"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _useThrottleFn = _interopRequireDefault(require("../useThrottleFn"));
function useThrottle(value, options) {
  var _a = (0, _tslib.__read)((0, _react.useState)(value), 2),
    throttled = _a[0],
    setThrottled = _a[1];
  var run = (0, _useThrottleFn["default"])(function () {
    setThrottled(value);
  }, options).run;
  (0, _react.useEffect)(function () {
    run();
  }, [value]);
  return throttled;
}
var _default = exports["default"] = useThrottle;