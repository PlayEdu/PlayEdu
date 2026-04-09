"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _throttle = _interopRequireDefault(require("lodash/throttle"));
var _react = require("react");
var useThrottlePlugin = function useThrottlePlugin(fetchInstance, _a) {
  var throttleWait = _a.throttleWait,
    throttleLeading = _a.throttleLeading,
    throttleTrailing = _a.throttleTrailing;
  var throttledRef = (0, _react.useRef)(undefined);
  var options = {};
  if (throttleLeading !== undefined) {
    options.leading = throttleLeading;
  }
  if (throttleTrailing !== undefined) {
    options.trailing = throttleTrailing;
  }
  (0, _react.useEffect)(function () {
    if (throttleWait) {
      var _originRunAsync_1 = fetchInstance.runAsync.bind(fetchInstance);
      throttledRef.current = (0, _throttle["default"])(function (callback) {
        callback();
      }, throttleWait, options);
      // throttle runAsync should be promise
      // https://github.com/lodash/lodash/issues/4400#issuecomment-834800398
      fetchInstance.runAsync = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
          args[_i] = arguments[_i];
        }
        return new Promise(function (resolve, reject) {
          var _a;
          (_a = throttledRef.current) === null || _a === void 0 ? void 0 : _a.call(throttledRef, function () {
            _originRunAsync_1.apply(void 0, (0, _tslib.__spreadArray)([], (0, _tslib.__read)(args), false)).then(resolve)["catch"](reject);
          });
        });
      };
      return function () {
        var _a;
        fetchInstance.runAsync = _originRunAsync_1;
        (_a = throttledRef.current) === null || _a === void 0 ? void 0 : _a.cancel();
      };
    }
  }, [throttleWait, throttleLeading, throttleTrailing]);
  if (!throttleWait) {
    return {};
  }
  return {
    onCancel: function onCancel() {
      var _a;
      (_a = throttledRef.current) === null || _a === void 0 ? void 0 : _a.cancel();
    }
  };
};
var _default = exports["default"] = useThrottlePlugin;