"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _resizeObserverPolyfill = _interopRequireDefault(require("resize-observer-polyfill"));
var _useRafState = _interopRequireDefault(require("../useRafState"));
var _domTarget = require("../utils/domTarget");
var _useIsomorphicLayoutEffectWithTarget = _interopRequireDefault(require("../utils/useIsomorphicLayoutEffectWithTarget"));
function useSize(target) {
  var _a = (0, _tslib.__read)((0, _useRafState["default"])(function () {
      var el = (0, _domTarget.getTargetElement)(target);
      return el ? {
        width: el.clientWidth,
        height: el.clientHeight
      } : undefined;
    }), 2),
    state = _a[0],
    setState = _a[1];
  (0, _useIsomorphicLayoutEffectWithTarget["default"])(function () {
    var el = (0, _domTarget.getTargetElement)(target);
    if (!el) {
      return;
    }
    var resizeObserver = new _resizeObserverPolyfill["default"](function (entries) {
      entries.forEach(function (entry) {
        var _a = entry.target,
          clientWidth = _a.clientWidth,
          clientHeight = _a.clientHeight;
        setState({
          width: clientWidth,
          height: clientHeight
        });
      });
    });
    resizeObserver.observe(el);
    return function () {
      resizeObserver.disconnect();
    };
  }, [], target);
  return state;
}
var _default = exports["default"] = useSize;