"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _useBoolean = _interopRequireDefault(require("../useBoolean"));
var _useEventListener = _interopRequireDefault(require("../useEventListener"));
var _default = exports["default"] = function _default(target, options) {
  var _a = options || {},
    onEnter = _a.onEnter,
    onLeave = _a.onLeave,
    onChange = _a.onChange;
  var _b = (0, _tslib.__read)((0, _useBoolean["default"])(false), 2),
    state = _b[0],
    _c = _b[1],
    setTrue = _c.setTrue,
    setFalse = _c.setFalse;
  (0, _useEventListener["default"])('mouseenter', function () {
    onEnter === null || onEnter === void 0 ? void 0 : onEnter();
    setTrue();
    onChange === null || onChange === void 0 ? void 0 : onChange(true);
  }, {
    target: target
  });
  (0, _useEventListener["default"])('mouseleave', function () {
    onLeave === null || onLeave === void 0 ? void 0 : onLeave();
    setFalse();
    onChange === null || onChange === void 0 ? void 0 : onChange(false);
  }, {
    target: target
  });
  return state;
};