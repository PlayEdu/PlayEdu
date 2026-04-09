"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
require("intersection-observer");
var _react = require("react");
var _domTarget = require("../utils/domTarget");
var _useEffectWithTarget = _interopRequireDefault(require("../utils/useEffectWithTarget"));
function useInViewport(target, options) {
  var _a = options || {},
    callback = _a.callback,
    option = (0, _tslib.__rest)(_a, ["callback"]);
  var _b = (0, _tslib.__read)((0, _react.useState)(), 2),
    state = _b[0],
    setState = _b[1];
  var _c = (0, _tslib.__read)((0, _react.useState)(), 2),
    ratio = _c[0],
    setRatio = _c[1];
  (0, _useEffectWithTarget["default"])(function () {
    var targets = Array.isArray(target) ? target : [target];
    var els = targets.map(function (element) {
      return (0, _domTarget.getTargetElement)(element);
    }).filter(Boolean);
    if (!els.length) {
      return;
    }
    var observer = new IntersectionObserver(function (entries) {
      var e_1, _a;
      try {
        for (var entries_1 = (0, _tslib.__values)(entries), entries_1_1 = entries_1.next(); !entries_1_1.done; entries_1_1 = entries_1.next()) {
          var entry = entries_1_1.value;
          setRatio(entry.intersectionRatio);
          setState(entry.isIntersecting);
          callback === null || callback === void 0 ? void 0 : callback(entry);
        }
      } catch (e_1_1) {
        e_1 = {
          error: e_1_1
        };
      } finally {
        try {
          if (entries_1_1 && !entries_1_1.done && (_a = entries_1["return"])) _a.call(entries_1);
        } finally {
          if (e_1) throw e_1.error;
        }
      }
    }, (0, _tslib.__assign)((0, _tslib.__assign)({}, option), {
      root: (0, _domTarget.getTargetElement)(options === null || options === void 0 ? void 0 : options.root)
    }));
    els.forEach(function (el) {
      return observer.observe(el);
    });
    return function () {
      observer.disconnect();
    };
  }, [options === null || options === void 0 ? void 0 : options.rootMargin, options === null || options === void 0 ? void 0 : options.threshold, callback], target);
  return [state, ratio];
}
var _default = exports["default"] = useInViewport;