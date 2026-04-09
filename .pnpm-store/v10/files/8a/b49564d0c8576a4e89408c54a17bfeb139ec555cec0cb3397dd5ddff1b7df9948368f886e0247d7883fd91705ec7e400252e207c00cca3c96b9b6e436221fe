"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _isBrowser = _interopRequireDefault(require("../../../utils/isBrowser"));
var _isDocumentVisible = _interopRequireDefault(require("./isDocumentVisible"));
var _isOnline = _interopRequireDefault(require("./isOnline"));
// from swr

var listeners = new Set();
function subscribe(listener) {
  listeners.add(listener);
  return function unsubscribe() {
    listeners.has(listener) && listeners["delete"](listener);
  };
}
if (_isBrowser["default"]) {
  var revalidate = function revalidate() {
    if (!(0, _isDocumentVisible["default"])() || !(0, _isOnline["default"])()) return;
    listeners.forEach(function (listener) {
      return listener();
    });
  };
  window.addEventListener('visibilitychange', revalidate, false);
  window.addEventListener('focus', revalidate, false);
}
var _default = exports["default"] = subscribe;