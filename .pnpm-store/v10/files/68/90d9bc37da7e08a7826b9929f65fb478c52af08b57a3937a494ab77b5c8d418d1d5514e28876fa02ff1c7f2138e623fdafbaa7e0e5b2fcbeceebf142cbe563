"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _debounce = _interopRequireDefault(require("lodash/debounce"));
var _react = require("react");
var useDebouncePlugin = function useDebouncePlugin(fetchInstance, _a) {
  var debounceWait = _a.debounceWait,
    debounceLeading = _a.debounceLeading,
    debounceTrailing = _a.debounceTrailing,
    debounceMaxWait = _a.debounceMaxWait;
  var debouncedRef = (0, _react.useRef)(undefined);
  var options = (0, _react.useMemo)(function () {
    var ret = {};
    if (debounceLeading !== undefined) {
      ret.leading = debounceLeading;
    }
    if (debounceTrailing !== undefined) {
      ret.trailing = debounceTrailing;
    }
    if (debounceMaxWait !== undefined) {
      ret.maxWait = debounceMaxWait;
    }
    return ret;
  }, [debounceLeading, debounceTrailing, debounceMaxWait]);
  (0, _react.useEffect)(function () {
    if (debounceWait) {
      var _originRunAsync_1 = fetchInstance.runAsync.bind(fetchInstance);
      debouncedRef.current = (0, _debounce["default"])(function (callback) {
        callback();
      }, debounceWait, options);
      // debounce runAsync should be promise
      // https://github.com/lodash/lodash/issues/4400#issuecomment-834800398
      fetchInstance.runAsync = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
          args[_i] = arguments[_i];
        }
        return new Promise(function (resolve, reject) {
          var _a;
          (_a = debouncedRef.current) === null || _a === void 0 ? void 0 : _a.call(debouncedRef, function () {
            _originRunAsync_1.apply(void 0, (0, _tslib.__spreadArray)([], (0, _tslib.__read)(args), false)).then(resolve)["catch"](reject);
          });
        });
      };
      return function () {
        var _a;
        (_a = debouncedRef.current) === null || _a === void 0 ? void 0 : _a.cancel();
        fetchInstance.runAsync = _originRunAsync_1;
      };
    }
  }, [debounceWait, options]);
  if (!debounceWait) {
    return {};
  }
  return {
    onCancel: function onCancel() {
      var _a;
      (_a = debouncedRef.current) === null || _a === void 0 ? void 0 : _a.cancel();
    }
  };
};
var _default = exports["default"] = useDebouncePlugin;