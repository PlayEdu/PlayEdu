"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _useLatest = _interopRequireDefault(require("../useLatest"));
var _domTarget = require("../utils/domTarget");
var _useEffectWithTarget = _interopRequireDefault(require("../utils/useEffectWithTarget"));
function useEventListener(eventName, handler, options) {
  if (options === void 0) {
    options = {};
  }
  var _a = options.enable,
    enable = _a === void 0 ? true : _a;
  var handlerRef = (0, _useLatest["default"])(handler);
  (0, _useEffectWithTarget["default"])(function () {
    if (!enable) {
      return;
    }
    var targetElement = (0, _domTarget.getTargetElement)(options.target, window);
    if (!(targetElement === null || targetElement === void 0 ? void 0 : targetElement.addEventListener)) {
      return;
    }
    var eventListener = function eventListener(event) {
      return handlerRef.current(event);
    };
    var eventNameArray = Array.isArray(eventName) ? eventName : [eventName];
    eventNameArray.forEach(function (event) {
      targetElement.addEventListener(event, eventListener, {
        capture: options.capture,
        once: options.once,
        passive: options.passive
      });
    });
    return function () {
      eventNameArray.forEach(function (event) {
        targetElement.removeEventListener(event, eventListener, {
          capture: options.capture
        });
      });
    };
  }, [eventName, options.capture, options.once, options.passive, enable], options.target);
}
var _default = exports["default"] = useEventListener;