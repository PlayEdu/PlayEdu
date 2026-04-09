"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _react = require("react");
var _useEffectWithTarget = _interopRequireDefault(require("./useEffectWithTarget"));
var _depsEqual = require("./depsEqual");
var useDeepCompareEffectWithTarget = function useDeepCompareEffectWithTarget(effect, deps, target) {
  var ref = (0, _react.useRef)(undefined);
  var signalRef = (0, _react.useRef)(0);
  if (!(0, _depsEqual.depsEqual)(deps, ref.current)) {
    signalRef.current += 1;
  }
  ref.current = deps;
  (0, _useEffectWithTarget["default"])(effect, [signalRef.current], target);
};
var _default = exports["default"] = useDeepCompareEffectWithTarget;