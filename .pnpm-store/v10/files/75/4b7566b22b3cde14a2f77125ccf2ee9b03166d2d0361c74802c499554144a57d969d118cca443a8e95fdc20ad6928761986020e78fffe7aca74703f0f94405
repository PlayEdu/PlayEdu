"use strict";

var _interopRequireWildcard = require("@babel/runtime/helpers/interopRequireWildcard").default;
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.useSyncState = void 0;
var React = _interopRequireWildcard(require("react"));
var _useForceUpdate = require("./useForceUpdate");
const useSyncState = initialValue => {
  const ref = React.useRef(initialValue);
  const [, forceUpdate] = (0, _useForceUpdate.useForceUpdate)();
  return [() => ref.current, newValue => {
    ref.current = newValue;
    // re-render
    forceUpdate();
  }];
};
exports.useSyncState = useSyncState;