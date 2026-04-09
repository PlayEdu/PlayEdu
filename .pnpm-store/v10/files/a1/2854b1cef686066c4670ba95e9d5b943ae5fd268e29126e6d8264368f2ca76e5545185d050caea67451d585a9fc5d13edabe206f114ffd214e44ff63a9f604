"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = useBoolean;
var _tslib = require("tslib");
var _react = require("react");
var _useToggle = _interopRequireDefault(require("../useToggle"));
function useBoolean(defaultValue) {
  if (defaultValue === void 0) {
    defaultValue = false;
  }
  var _a = (0, _tslib.__read)((0, _useToggle["default"])(!!defaultValue), 2),
    state = _a[0],
    _b = _a[1],
    toggle = _b.toggle,
    _set = _b.set;
  var actions = (0, _react.useMemo)(function () {
    var setTrue = function setTrue() {
      return _set(true);
    };
    var setFalse = function setFalse() {
      return _set(false);
    };
    return {
      toggle: toggle,
      set: function set(v) {
        return _set(!!v);
      },
      setTrue: setTrue,
      setFalse: setFalse
    };
  }, []);
  return [state, actions];
}