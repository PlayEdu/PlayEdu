"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _useUnmount = _interopRequireDefault(require("../useUnmount"));
function useRafState(initialState) {
  var ref = (0, _react.useRef)(0);
  var _a = (0, _tslib.__read)((0, _react.useState)(initialState), 2),
    state = _a[0],
    setState = _a[1];
  var setRafState = (0, _react.useCallback)(function (value) {
    cancelAnimationFrame(ref.current);
    ref.current = requestAnimationFrame(function () {
      setState(value);
    });
  }, []);
  (0, _useUnmount["default"])(function () {
    cancelAnimationFrame(ref.current);
  });
  return [state, setRafState];
}
var _default = exports["default"] = useRafState;