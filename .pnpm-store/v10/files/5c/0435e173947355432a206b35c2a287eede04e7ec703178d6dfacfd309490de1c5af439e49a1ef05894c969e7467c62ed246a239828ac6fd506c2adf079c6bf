"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _isBrowser = _interopRequireDefault(require("./isBrowser"));
var _useEffectWithTarget = _interopRequireDefault(require("./useEffectWithTarget"));
var _useLayoutEffectWithTarget = _interopRequireDefault(require("./useLayoutEffectWithTarget"));
var useIsomorphicLayoutEffectWithTarget = _isBrowser["default"] ? _useLayoutEffectWithTarget["default"] : _useEffectWithTarget["default"];
var _default = exports["default"] = useIsomorphicLayoutEffectWithTarget;