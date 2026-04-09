"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = useFocusWithin;
var _tslib = require("tslib");
var _react = require("react");
var _useEventListener = _interopRequireDefault(require("../useEventListener"));
function useFocusWithin(target, options) {
  var _a = (0, _tslib.__read)((0, _react.useState)(false), 2),
    isFocusWithin = _a[0],
    setIsFocusWithin = _a[1];
  var _b = options || {},
    onFocus = _b.onFocus,
    onBlur = _b.onBlur,
    onChange = _b.onChange;
  (0, _useEventListener["default"])('focusin', function (e) {
    if (!isFocusWithin) {
      onFocus === null || onFocus === void 0 ? void 0 : onFocus(e);
      onChange === null || onChange === void 0 ? void 0 : onChange(true);
      setIsFocusWithin(true);
    }
  }, {
    target: target
  });
  (0, _useEventListener["default"])('focusout', function (e) {
    var _a, _b;
    if (isFocusWithin && !((_b = (_a = e.currentTarget) === null || _a === void 0 ? void 0 : _a.contains) === null || _b === void 0 ? void 0 : _b.call(_a, e.relatedTarget))) {
      onBlur === null || onBlur === void 0 ? void 0 : onBlur(e);
      onChange === null || onChange === void 0 ? void 0 : onChange(false);
      setIsFocusWithin(false);
    }
  }, {
    target: target
  });
  return isFocusWithin;
}