"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _useThrottleFn = _interopRequireDefault(require("../useThrottleFn"));
var _useUpdateEffect = _interopRequireDefault(require("../useUpdateEffect"));
function useThrottleEffect(effect, deps, options) {
  var _a = (0, _tslib.__read)((0, _react.useState)({}), 2),
    flag = _a[0],
    setFlag = _a[1];
  var run = (0, _useThrottleFn["default"])(function () {
    setFlag({});
  }, options).run;
  (0, _react.useEffect)(function () {
    return run();
  }, deps);
  (0, _useUpdateEffect["default"])(effect, [flag]);
}
var _default = exports["default"] = useThrottleEffect;