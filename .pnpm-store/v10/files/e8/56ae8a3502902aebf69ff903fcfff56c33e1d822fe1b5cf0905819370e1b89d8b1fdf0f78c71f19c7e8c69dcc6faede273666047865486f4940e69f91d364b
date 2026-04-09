"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _typeof2 = _interopRequireDefault(require("@babel/runtime/helpers/typeof"));
var _react = require("react");
var _utils = require("../utils");
var _isDev = _interopRequireDefault(require("../utils/isDev"));
var useMount = function useMount(fn) {
  if (_isDev["default"]) {
    if (!(0, _utils.isFunction)(fn)) {
      console.error("useMount: parameter `fn` expected to be a function, but got \"".concat((0, _typeof2["default"])(fn), "\"."));
    }
  }
  (0, _react.useEffect)(function () {
    var result = fn === null || fn === void 0 ? void 0 : fn();
    // If fn returns a Promise, don't return it as cleanup function
    if (result && (0, _typeof2["default"])(result) === 'object' && typeof result.then === 'function') {
      return;
    }
    return result;
  }, []);
};
var _default = exports["default"] = useMount;