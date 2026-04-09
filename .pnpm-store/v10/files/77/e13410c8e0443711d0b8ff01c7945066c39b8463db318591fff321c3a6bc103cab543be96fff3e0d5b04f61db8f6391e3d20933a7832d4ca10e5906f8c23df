"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _useLatest = _interopRequireDefault(require("../useLatest"));
var _utils = require("../utils");
function useEventTarget(options) {
  var _a = options || {},
    initialValue = _a.initialValue,
    transformer = _a.transformer;
  var _b = (0, _tslib.__read)((0, _react.useState)(initialValue), 2),
    value = _b[0],
    setValue = _b[1];
  var transformerRef = (0, _useLatest["default"])(transformer);
  var reset = (0, _react.useCallback)(function () {
    return setValue(initialValue);
  }, []);
  var onChange = (0, _react.useCallback)(function (e) {
    var _value = e.target.value;
    if ((0, _utils.isFunction)(transformerRef.current)) {
      return setValue(transformerRef.current(_value));
    }
    // no transformer => U and T should be the same
    return setValue(_value);
  }, []);
  return [value, {
    onChange: onChange,
    reset: reset
  }];
}
var _default = exports["default"] = useEventTarget;