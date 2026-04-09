"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.createDeepCompareEffect = void 0;
var _react = require("react");
var _depsEqual = require("../utils/depsEqual");
var createDeepCompareEffect = exports.createDeepCompareEffect = function createDeepCompareEffect(hook) {
  return function (effect, deps) {
    var ref = (0, _react.useRef)(undefined);
    var signalRef = (0, _react.useRef)(0);
    if (deps === undefined || !(0, _depsEqual.depsEqual)(deps, ref.current)) {
      signalRef.current += 1;
    }
    ref.current = deps;
    hook(effect, [signalRef.current]);
  };
};