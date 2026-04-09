"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _utils = require("../utils");
var _useMemoizedFn = _interopRequireDefault(require("../useMemoizedFn"));
var _useCreation = _interopRequireDefault(require("../useCreation"));
var useResetState = function useResetState(initialState) {
  var initialStateRef = (0, _react.useRef)(initialState);
  var initialStateMemo = (0, _useCreation["default"])(function () {
    return (0, _utils.isFunction)(initialStateRef.current) ? initialStateRef.current() : initialStateRef.current;
  }, []);
  var _a = (0, _tslib.__read)((0, _react.useState)(initialStateMemo), 2),
    state = _a[0],
    setState = _a[1];
  var resetState = (0, _useMemoizedFn["default"])(function () {
    setState(initialStateMemo);
  });
  return [state, setState, resetState];
};
var _default = exports["default"] = useResetState;