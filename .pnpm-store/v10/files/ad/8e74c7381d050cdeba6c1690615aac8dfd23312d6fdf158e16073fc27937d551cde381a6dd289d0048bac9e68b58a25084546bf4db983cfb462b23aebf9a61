"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _react = require("react");
var defaultShouldUpdate = function defaultShouldUpdate(a, b) {
  return !Object.is(a, b);
};
function usePrevious(state, shouldUpdate) {
  if (shouldUpdate === void 0) {
    shouldUpdate = defaultShouldUpdate;
  }
  var prevRef = (0, _react.useRef)(undefined);
  var curRef = (0, _react.useRef)(undefined);
  if (shouldUpdate(curRef.current, state)) {
    prevRef.current = curRef.current;
    curRef.current = state;
  }
  return prevRef.current;
}
var _default = exports["default"] = usePrevious;