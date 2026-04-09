"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _useDebounceFn = _interopRequireDefault(require("../useDebounceFn"));
function useDebounce(value, options) {
  var _a = (0, _tslib.__read)((0, _react.useState)(value), 2),
    debounced = _a[0],
    setDebounced = _a[1];
  var run = (0, _useDebounceFn["default"])(function () {
    setDebounced(value);
  }, options).run;
  (0, _react.useEffect)(function () {
    run();
  }, [value]);
  return debounced;
}
var _default = exports["default"] = useDebounce;