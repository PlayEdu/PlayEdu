"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = isDocumentVisible;
var _isBrowser = _interopRequireDefault(require("../../../utils/isBrowser"));
function isDocumentVisible() {
  if (_isBrowser["default"]) {
    return document.visibilityState !== 'hidden';
  }
  return true;
}