"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _react = require("react");
var _useUpdateEffect = _interopRequireDefault(require("../../../useUpdateEffect"));
var _isDocumentVisible = _interopRequireDefault(require("../utils/isDocumentVisible"));
var _subscribeReVisible = _interopRequireDefault(require("../utils/subscribeReVisible"));
var usePollingPlugin = function usePollingPlugin(fetchInstance, _a) {
  var pollingInterval = _a.pollingInterval,
    _b = _a.pollingWhenHidden,
    pollingWhenHidden = _b === void 0 ? true : _b,
    _c = _a.pollingErrorRetryCount,
    pollingErrorRetryCount = _c === void 0 ? -1 : _c;
  var timerRef = (0, _react.useRef)(undefined);
  var unsubscribeRef = (0, _react.useRef)(undefined);
  var countRef = (0, _react.useRef)(0);
  var stopPolling = function stopPolling() {
    var _a;
    if (timerRef.current) {
      clearTimeout(timerRef.current);
    }
    (_a = unsubscribeRef.current) === null || _a === void 0 ? void 0 : _a.call(unsubscribeRef);
  };
  (0, _useUpdateEffect["default"])(function () {
    if (!pollingInterval) {
      stopPolling();
    }
  }, [pollingInterval]);
  if (!pollingInterval) {
    return {};
  }
  return {
    onBefore: function onBefore() {
      stopPolling();
    },
    onError: function onError() {
      countRef.current += 1;
    },
    onSuccess: function onSuccess() {
      countRef.current = 0;
    },
    onFinally: function onFinally() {
      if (pollingErrorRetryCount === -1 ||
      // When an error occurs, the request is not repeated after pollingErrorRetryCount retries
      pollingErrorRetryCount !== -1 && countRef.current <= pollingErrorRetryCount) {
        timerRef.current = setTimeout(function () {
          // if pollingWhenHidden = false && document is hidden, then stop polling and subscribe revisible
          if (!pollingWhenHidden && !(0, _isDocumentVisible["default"])()) {
            unsubscribeRef.current = (0, _subscribeReVisible["default"])(function () {
              fetchInstance.refresh();
            });
          } else {
            fetchInstance.refresh();
          }
        }, pollingInterval);
      } else {
        countRef.current = 0;
      }
    },
    onCancel: function onCancel() {
      stopPolling();
    }
  };
};
var _default = exports["default"] = usePollingPlugin;