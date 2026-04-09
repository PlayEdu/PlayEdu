"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _domTarget = require("../utils/domTarget");
var _useDeepCompareWithTarget = _interopRequireDefault(require("../utils/useDeepCompareWithTarget"));
var _useLatest = _interopRequireDefault(require("../useLatest"));
var useMutationObserver = function useMutationObserver(callback, target, options) {
  if (options === void 0) {
    options = {};
  }
  var callbackRef = (0, _useLatest["default"])(callback);
  (0, _useDeepCompareWithTarget["default"])(function () {
    var element = (0, _domTarget.getTargetElement)(target);
    if (!element) {
      return;
    }
    var observer = new MutationObserver(callbackRef.current);
    observer.observe(element, options);
    return function () {
      observer === null || observer === void 0 ? void 0 : observer.disconnect();
    };
  }, [options], target);
};
var _default = exports["default"] = useMutationObserver;