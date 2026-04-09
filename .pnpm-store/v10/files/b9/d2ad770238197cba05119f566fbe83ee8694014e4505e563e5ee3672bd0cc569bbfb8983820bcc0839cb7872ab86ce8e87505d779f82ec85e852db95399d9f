"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _jsCookie = _interopRequireDefault(require("js-cookie"));
var _react = require("react");
var _useMemoizedFn = _interopRequireDefault(require("../useMemoizedFn"));
var _utils = require("../utils");
function useCookieState(cookieKey, options) {
  if (options === void 0) {
    options = {};
  }
  var _a = (0, _tslib.__read)((0, _react.useState)(function () {
      var cookieValue = _jsCookie["default"].get(cookieKey);
      if ((0, _utils.isString)(cookieValue)) {
        return cookieValue;
      }
      if ((0, _utils.isFunction)(options.defaultValue)) {
        return options.defaultValue();
      }
      return options.defaultValue;
    }), 2),
    state = _a[0],
    setState = _a[1];
  var updateState = (0, _useMemoizedFn["default"])(function (newValue, newOptions) {
    if (newOptions === void 0) {
      newOptions = {};
    }
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    var _a = (0, _tslib.__assign)((0, _tslib.__assign)({}, options), newOptions),
      defaultValue = _a.defaultValue,
      restOptions = (0, _tslib.__rest)(_a, ["defaultValue"]);
    var value = (0, _utils.isFunction)(newValue) ? newValue(state) : newValue;
    setState(value);
    if (value === undefined) {
      _jsCookie["default"].remove(cookieKey);
    } else {
      _jsCookie["default"].set(cookieKey, value, restOptions);
    }
  });
  return [state, updateState];
}
var _default = exports["default"] = useCookieState;