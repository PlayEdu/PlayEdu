"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
var _exportNames = {
  renderHook: true
};
exports.renderHook = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _react2 = require("@testing-library/react");
Object.keys(_react2).forEach(function (key) {
  if (key === "default" || key === "__esModule") return;
  if (Object.prototype.hasOwnProperty.call(_exportNames, key)) return;
  if (key in exports && exports[key] === _react2[key]) return;
  Object.defineProperty(exports, key, {
    enumerable: true,
    get: function get() {
      return _react2[key];
    }
  });
});
var Wrapper = process.env.REACT_MODE === 'strict' ? _react.StrictMode : undefined;
var customRender = exports.renderHook = function customRender(ui, options) {
  return (0, _react2.renderHook)(ui, (0, _tslib.__assign)({
    wrapper: Wrapper
  }, options));
};