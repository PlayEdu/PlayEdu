"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _useUnmountedRef = _interopRequireDefault(require("../useUnmountedRef"));
function useSafeState(initialState) {
  var unmountedRef = (0, _useUnmountedRef["default"])();
  var _a = (0, _tslib.__read)((0, _react.useState)(initialState), 2),
    state = _a[0],
    setState = _a[1];
  var setCurrentState = (0, _react.useCallback)(function (currentState) {
    /** if component is unmounted, stop update */
    if (unmountedRef.current) {
      return;
    }
    setState(currentState);
  }, []);
  return [state, setCurrentState];
}
var _default = exports["default"] = useSafeState;