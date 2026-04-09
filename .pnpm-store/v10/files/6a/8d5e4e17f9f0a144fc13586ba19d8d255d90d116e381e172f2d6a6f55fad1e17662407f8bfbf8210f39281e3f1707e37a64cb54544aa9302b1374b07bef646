"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _react = require("react");
var _depsAreSame = _interopRequireDefault(require("../utils/depsAreSame"));
var useCreation = function useCreation(factory, deps) {
  var current = (0, _react.useRef)({
    deps: deps,
    obj: undefined,
    initialized: false
  }).current;
  if (current.initialized === false || !(0, _depsAreSame["default"])(current.deps, deps)) {
    current.deps = deps;
    current.obj = factory();
    current.initialized = true;
  }
  return current.obj;
};
var _default = exports["default"] = useCreation;