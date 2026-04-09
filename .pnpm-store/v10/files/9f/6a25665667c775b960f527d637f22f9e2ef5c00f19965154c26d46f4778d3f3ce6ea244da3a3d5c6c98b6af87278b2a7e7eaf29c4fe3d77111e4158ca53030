"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _react = require("react");
var useLoadingDelayPlugin = function useLoadingDelayPlugin(fetchInstance, _a) {
  var loadingDelay = _a.loadingDelay,
    ready = _a.ready;
  var timerRef = (0, _react.useRef)(undefined);
  if (!loadingDelay) {
    return {};
  }
  var cancelTimeout = function cancelTimeout() {
    if (timerRef.current) {
      clearTimeout(timerRef.current);
    }
  };
  return {
    onBefore: function onBefore() {
      cancelTimeout();
      // Two cases:
      // 1. ready === undefined
      // 2. ready === true
      if (ready !== false) {
        timerRef.current = setTimeout(function () {
          fetchInstance.setState({
            loading: true
          });
        }, loadingDelay);
      }
      return {
        loading: false
      };
    },
    onFinally: function onFinally() {
      cancelTimeout();
    },
    onCancel: function onCancel() {
      cancelTimeout();
    }
  };
};
var _default = exports["default"] = useLoadingDelayPlugin;