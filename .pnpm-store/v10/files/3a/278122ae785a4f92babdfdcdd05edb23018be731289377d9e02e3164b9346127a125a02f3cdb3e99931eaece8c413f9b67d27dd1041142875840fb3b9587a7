"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _react = require("react");
var _useUnmount = _interopRequireDefault(require("../../../useUnmount"));
var _limit = _interopRequireDefault(require("../utils/limit"));
var _subscribeFocus = _interopRequireDefault(require("../utils/subscribeFocus"));
var useRefreshOnWindowFocusPlugin = function useRefreshOnWindowFocusPlugin(fetchInstance, _a) {
  var refreshOnWindowFocus = _a.refreshOnWindowFocus,
    _b = _a.focusTimespan,
    focusTimespan = _b === void 0 ? 5000 : _b;
  var unsubscribeRef = (0, _react.useRef)(undefined);
  var stopSubscribe = function stopSubscribe() {
    var _a;
    (_a = unsubscribeRef.current) === null || _a === void 0 ? void 0 : _a.call(unsubscribeRef);
  };
  (0, _react.useEffect)(function () {
    if (refreshOnWindowFocus) {
      var limitRefresh_1 = (0, _limit["default"])(fetchInstance.refresh.bind(fetchInstance), focusTimespan);
      unsubscribeRef.current = (0, _subscribeFocus["default"])(function () {
        limitRefresh_1();
      });
    }
    return function () {
      stopSubscribe();
    };
  }, [refreshOnWindowFocus, focusTimespan]);
  (0, _useUnmount["default"])(function () {
    stopSubscribe();
  });
  return {};
};
var _default = exports["default"] = useRefreshOnWindowFocusPlugin;