"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
Object.defineProperty(exports, "debounce", {
  enumerable: true,
  get: function get() {
    return _debounce["default"];
  }
});
var _typeof2 = _interopRequireDefault(require("@babel/runtime/helpers/typeof"));
var _debounce = _interopRequireDefault(require("lodash/debounce"));
function isNodeOrWeb() {
  var freeGlobal = (typeof global === 'undefined' ? 'undefined' : typeof global === "undefined" ? "undefined" : (0, _typeof2["default"])(global)) == 'object' && global && global.Object === Object && global;
  var freeSelf = (typeof self === "undefined" ? "undefined" : (0, _typeof2["default"])(self)) == 'object' && self && self.Object === Object && self;
  return freeGlobal || freeSelf;
}
if (!isNodeOrWeb()) {
  global.Date = Date;
}