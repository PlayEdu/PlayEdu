"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _useEventListener = _interopRequireDefault(require("../useEventListener"));
var _isBrowser = _interopRequireDefault(require("../utils/isBrowser"));
var getVisibility = function getVisibility() {
  if (!_isBrowser["default"]) {
    return 'visible';
  }
  return document.visibilityState;
};
function useDocumentVisibility() {
  var _a = (0, _tslib.__read)((0, _react.useState)(getVisibility), 2),
    documentVisibility = _a[0],
    setDocumentVisibility = _a[1];
  (0, _useEventListener["default"])('visibilitychange', function () {
    setDocumentVisibility(getVisibility());
  }, {
    target: function target() {
      return document;
    }
  });
  return documentVisibility;
}
var _default = exports["default"] = useDocumentVisibility;