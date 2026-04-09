"use strict";
"use client";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault").default;
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _react = _interopRequireDefault(require("react"));
var _ContextIsolator = _interopRequireDefault(require("../_util/ContextIsolator"));
function usePopupRender(renderFn) {
  return _react.default.useMemo(() => {
    if (!renderFn) {
      return undefined;
    }
    return (...args) => /*#__PURE__*/_react.default.createElement(_ContextIsolator.default, {
      space: true
    }, renderFn.apply(void 0, args));
  }, [renderFn]);
}
var _default = exports.default = usePopupRender;