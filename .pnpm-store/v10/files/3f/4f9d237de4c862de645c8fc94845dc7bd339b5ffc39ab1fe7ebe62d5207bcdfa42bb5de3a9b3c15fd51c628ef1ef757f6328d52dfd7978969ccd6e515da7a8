"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.configResponsive = configResponsive;
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _isBrowser = _interopRequireDefault(require("../utils/isBrowser"));
var subscribers = new Set();
var info;
var responsiveConfig = {
  xs: 0,
  sm: 576,
  md: 768,
  lg: 992,
  xl: 1200
};
function handleResize() {
  var e_1, _a;
  var oldInfo = info;
  calculate();
  if (oldInfo === info) {
    return;
  }
  try {
    for (var subscribers_1 = (0, _tslib.__values)(subscribers), subscribers_1_1 = subscribers_1.next(); !subscribers_1_1.done; subscribers_1_1 = subscribers_1.next()) {
      var subscriber = subscribers_1_1.value;
      subscriber();
    }
  } catch (e_1_1) {
    e_1 = {
      error: e_1_1
    };
  } finally {
    try {
      if (subscribers_1_1 && !subscribers_1_1.done && (_a = subscribers_1["return"])) _a.call(subscribers_1);
    } finally {
      if (e_1) throw e_1.error;
    }
  }
}
var listening = false;
function calculate() {
  var e_2, _a;
  var width = window.innerWidth;
  var newInfo = {};
  var shouldUpdate = false;
  try {
    for (var _b = (0, _tslib.__values)(Object.keys(responsiveConfig)), _c = _b.next(); !_c.done; _c = _b.next()) {
      var key = _c.value;
      newInfo[key] = width >= responsiveConfig[key];
      if (newInfo[key] !== info[key]) {
        shouldUpdate = true;
      }
    }
  } catch (e_2_1) {
    e_2 = {
      error: e_2_1
    };
  } finally {
    try {
      if (_c && !_c.done && (_a = _b["return"])) _a.call(_b);
    } finally {
      if (e_2) throw e_2.error;
    }
  }
  if (shouldUpdate) {
    info = newInfo;
  }
}
function configResponsive(config) {
  responsiveConfig = config;
  if (info) calculate();
}
function useResponsive() {
  if (_isBrowser["default"] && !listening) {
    info = {};
    calculate();
    window.addEventListener('resize', handleResize);
    listening = true;
  }
  var _a = (0, _tslib.__read)((0, _react.useState)(info), 2),
    state = _a[0],
    setState = _a[1];
  (0, _react.useEffect)(function () {
    if (!_isBrowser["default"]) {
      return;
    }
    // In React 18's StrictMode, useEffect perform twice, resize listener is remove, so handleResize is never perform.
    // https://github.com/alibaba/hooks/issues/1910
    if (!listening) {
      window.addEventListener('resize', handleResize);
    }
    var subscriber = function subscriber() {
      setState(info);
    };
    subscribers.add(subscriber);
    return function () {
      subscribers["delete"](subscriber);
      if (subscribers.size === 0) {
        window.removeEventListener('resize', handleResize);
        listening = false;
      }
    };
  }, []);
  return state;
}
var _default = exports["default"] = useResponsive;