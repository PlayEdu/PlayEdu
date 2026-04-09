"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _useDebounceFn = _interopRequireDefault(require("../useDebounceFn"));
var _useUpdateEffect = _interopRequireDefault(require("../useUpdateEffect"));
function useDebounceEffect(effect, deps, options) {
  var _a = (0, _tslib.__read)((0, _react.useState)({}), 2),
    flag = _a[0],
    setFlag = _a[1];
  var run = (0, _useDebounceFn["default"])(function () {
    setFlag({});
  }, options).run;
  (0, _react.useEffect)(function () {
    return run();
  }, deps);
  (0, _useUpdateEffect["default"])(effect, [flag]);
}
var _default = exports["default"] = useDebounceEffect;