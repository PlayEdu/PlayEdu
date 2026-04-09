"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = limit;
var _tslib = require("tslib");
function limit(fn, timespan) {
  var pending = false;
  return function () {
    var args = [];
    for (var _i = 0; _i < arguments.length; _i++) {
      args[_i] = arguments[_i];
    }
    if (pending) return;
    pending = true;
    fn.apply(void 0, (0, _tslib.__spreadArray)([], (0, _tslib.__read)(args), false));
    setTimeout(function () {
      pending = false;
    }, timespan);
  };
}