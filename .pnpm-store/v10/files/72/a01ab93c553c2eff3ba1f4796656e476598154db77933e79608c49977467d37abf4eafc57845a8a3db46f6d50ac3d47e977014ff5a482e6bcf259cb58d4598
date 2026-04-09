"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _useLatest = _interopRequireDefault(require("../useLatest"));
function useGetState(initialState) {
  var _a = (0, _tslib.__read)((0, _react.useState)(initialState), 2),
    state = _a[0],
    setState = _a[1];
  var stateRef = (0, _useLatest["default"])(state);
  var getState = (0, _react.useCallback)(function () {
    return stateRef.current;
  }, []);
  return [state, setState, getState];
}
var _default = exports["default"] = useGetState;