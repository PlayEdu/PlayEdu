"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _typeof2 = _interopRequireDefault(require("@babel/runtime/helpers/typeof"));
var _react = require("react");
var _useLatest = _interopRequireDefault(require("../useLatest"));
var _utils = require("../utils");
var _isDev = _interopRequireDefault(require("../utils/isDev"));
var useUnmount = function useUnmount(fn) {
  if (_isDev["default"]) {
    if (!(0, _utils.isFunction)(fn)) {
      console.error("useUnmount expected parameter is a function, got ".concat((0, _typeof2["default"])(fn)));
    }
  }
  var fnRef = (0, _useLatest["default"])(fn);
  (0, _react.useEffect)(function () {
    return function () {
      fnRef.current();
    };
  }, []);
};
var _default = exports["default"] = useUnmount;