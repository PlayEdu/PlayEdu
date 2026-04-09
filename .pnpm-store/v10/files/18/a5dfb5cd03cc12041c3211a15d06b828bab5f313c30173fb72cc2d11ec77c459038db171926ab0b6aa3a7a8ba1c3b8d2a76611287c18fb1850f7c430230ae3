"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _useMemoizedFn = _interopRequireDefault(require("../useMemoizedFn"));
var _utils = require("../utils");
var useSetState = function useSetState(initialState) {
  var _a = (0, _tslib.__read)((0, _react.useState)(initialState), 2),
    state = _a[0],
    setState = _a[1];
  var setMergeState = (0, _useMemoizedFn["default"])(function (patch) {
    setState(function (prevState) {
      var newState = (0, _utils.isFunction)(patch) ? patch(prevState) : patch;
      return newState ? (0, _tslib.__assign)((0, _tslib.__assign)({}, prevState), newState) : prevState;
    });
  });
  return [state, setMergeState];
};
var _default = exports["default"] = useSetState;