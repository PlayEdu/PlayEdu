"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _react = require("react");
var _useUnmount = _interopRequireDefault(require("../useUnmount"));
var _depsAreSame = _interopRequireDefault(require("./depsAreSame"));
var _domTarget = require("./domTarget");
var createEffectWithTarget = function createEffectWithTarget(useEffectType) {
  /**
   *
   * @param effect
   * @param deps
   * @param target target should compare ref.current vs ref.current, dom vs dom, ()=>dom vs ()=>dom
   */
  var useEffectWithTarget = function useEffectWithTarget(effect, deps, target) {
    var hasInitRef = (0, _react.useRef)(false);
    var lastElementRef = (0, _react.useRef)([]);
    var lastDepsRef = (0, _react.useRef)([]);
    var unLoadRef = (0, _react.useRef)(undefined);
    useEffectType(function () {
      var _a;
      var targets = Array.isArray(target) ? target : [target];
      var els = targets.map(function (item) {
        return (0, _domTarget.getTargetElement)(item);
      });
      // init run
      if (!hasInitRef.current) {
        hasInitRef.current = true;
        lastElementRef.current = els;
        lastDepsRef.current = deps;
        unLoadRef.current = effect();
        return;
      }
      if (els.length !== lastElementRef.current.length || !(0, _depsAreSame["default"])(lastElementRef.current, els) || !(0, _depsAreSame["default"])(lastDepsRef.current, deps)) {
        (_a = unLoadRef.current) === null || _a === void 0 ? void 0 : _a.call(unLoadRef);
        lastElementRef.current = els;
        lastDepsRef.current = deps;
        unLoadRef.current = effect();
      }
    });
    (0, _useUnmount["default"])(function () {
      var _a;
      (_a = unLoadRef.current) === null || _a === void 0 ? void 0 : _a.call(unLoadRef);
      // for react-refresh
      hasInitRef.current = false;
    });
  };
  return useEffectWithTarget;
};
var _default = exports["default"] = createEffectWithTarget;