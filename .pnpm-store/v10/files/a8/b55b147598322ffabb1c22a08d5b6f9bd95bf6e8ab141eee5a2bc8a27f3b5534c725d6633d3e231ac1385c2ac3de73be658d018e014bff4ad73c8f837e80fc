"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _utils = require("../utils");
var _useMemoizedFn = _interopRequireDefault(require("../useMemoizedFn"));
var _useUpdate = _interopRequireDefault(require("../useUpdate"));
function useControllableValue(defaultProps, options) {
  if (options === void 0) {
    options = {};
  }
  var props = defaultProps !== null && defaultProps !== void 0 ? defaultProps : {};
  var defaultValue = options.defaultValue,
    _a = options.defaultValuePropName,
    defaultValuePropName = _a === void 0 ? 'defaultValue' : _a,
    _b = options.valuePropName,
    valuePropName = _b === void 0 ? 'value' : _b,
    _c = options.trigger,
    trigger = _c === void 0 ? 'onChange' : _c;
  var value = props[valuePropName];
  var isControlled = Object.prototype.hasOwnProperty.call(props, valuePropName);
  var initialValue = (0, _react.useMemo)(function () {
    if (isControlled) {
      return value;
    }
    if (Object.prototype.hasOwnProperty.call(props, defaultValuePropName)) {
      return props[defaultValuePropName];
    }
    return defaultValue;
  }, []);
  var stateRef = (0, _react.useRef)(initialValue);
  if (isControlled) {
    stateRef.current = value;
  }
  var update = (0, _useUpdate["default"])();
  function setState(v) {
    var args = [];
    for (var _i = 1; _i < arguments.length; _i++) {
      args[_i - 1] = arguments[_i];
    }
    var r = (0, _utils.isFunction)(v) ? v(stateRef.current) : v;
    if (!isControlled) {
      stateRef.current = r;
      update();
    }
    if (props[trigger]) {
      props[trigger].apply(props, (0, _tslib.__spreadArray)([r], (0, _tslib.__read)(args), false));
    }
  }
  return [stateRef.current, (0, _useMemoizedFn["default"])(setState)];
}
var _default = exports["default"] = useControllableValue;