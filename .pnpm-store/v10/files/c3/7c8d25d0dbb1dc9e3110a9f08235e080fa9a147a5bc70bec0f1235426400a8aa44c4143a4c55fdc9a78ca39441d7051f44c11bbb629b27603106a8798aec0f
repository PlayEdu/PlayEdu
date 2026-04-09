"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.getTargetElement = getTargetElement;
var _index = require("./index");
var _isBrowser = _interopRequireDefault(require("./isBrowser"));
function getTargetElement(target, defaultElement) {
  if (!_isBrowser["default"]) {
    return undefined;
  }
  if (!target) {
    return defaultElement;
  }
  var targetElement;
  if ((0, _index.isFunction)(target)) {
    targetElement = target();
  } else if ('current' in target) {
    targetElement = target.current;
  } else {
    targetElement = target;
  }
  return targetElement;
}