"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _useMemoizedFn = _interopRequireDefault(require("../useMemoizedFn"));
var _utils = require("../utils");
function getTargetValue(val, options) {
  if (options === void 0) {
    options = {};
  }
  var min = options.min,
    max = options.max;
  var target = val;
  if ((0, _utils.isNumber)(max)) {
    target = Math.min(max, target);
  }
  if ((0, _utils.isNumber)(min)) {
    target = Math.max(min, target);
  }
  return target;
}
function useCounter(initialValue, options) {
  if (initialValue === void 0) {
    initialValue = 0;
  }
  if (options === void 0) {
    options = {};
  }
  var min = options.min,
    max = options.max;
  var _a = (0, _tslib.__read)((0, _react.useState)(function () {
      return getTargetValue(initialValue, {
        min: min,
        max: max
      });
    }), 2),
    current = _a[0],
    setCurrent = _a[1];
  var setValue = function setValue(value) {
    setCurrent(function (c) {
      var target = (0, _utils.isNumber)(value) ? value : value(c);
      return getTargetValue(target, {
        max: max,
        min: min
      });
    });
  };
  var inc = function inc(delta) {
    if (delta === void 0) {
      delta = 1;
    }
    setValue(function (c) {
      return c + delta;
    });
  };
  var dec = function dec(delta) {
    if (delta === void 0) {
      delta = 1;
    }
    setValue(function (c) {
      return c - delta;
    });
  };
  var set = function set(value) {
    setValue(value);
  };
  var reset = function reset() {
    setValue(initialValue);
  };
  return [current, {
    inc: (0, _useMemoizedFn["default"])(inc),
    dec: (0, _useMemoizedFn["default"])(dec),
    set: (0, _useMemoizedFn["default"])(set),
    reset: (0, _useMemoizedFn["default"])(reset)
  }];
}
var _default = exports["default"] = useCounter;