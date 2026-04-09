"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _react = require("react");
var useRetryPlugin = function useRetryPlugin(fetchInstance, _a) {
  var retryInterval = _a.retryInterval,
    retryCount = _a.retryCount;
  var timerRef = (0, _react.useRef)(undefined);
  var countRef = (0, _react.useRef)(0);
  var triggerByRetry = (0, _react.useRef)(false);
  if (!retryCount) {
    return {};
  }
  return {
    onBefore: function onBefore() {
      if (!triggerByRetry.current) {
        countRef.current = 0;
      }
      triggerByRetry.current = false;
      if (timerRef.current) {
        clearTimeout(timerRef.current);
      }
    },
    onSuccess: function onSuccess() {
      countRef.current = 0;
    },
    onError: function onError() {
      countRef.current += 1;
      if (retryCount === -1 || countRef.current <= retryCount) {
        // Exponential backoff
        var timeout = retryInterval !== null && retryInterval !== void 0 ? retryInterval : Math.min(1000 * Math.pow(2, countRef.current), 30000);
        timerRef.current = setTimeout(function () {
          triggerByRetry.current = true;
          fetchInstance.refresh();
        }, timeout);
      } else {
        countRef.current = 0;
      }
    },
    onCancel: function onCancel() {
      countRef.current = 0;
      if (timerRef.current) {
        clearTimeout(timerRef.current);
      }
    }
  };
};
var _default = exports["default"] = useRetryPlugin;