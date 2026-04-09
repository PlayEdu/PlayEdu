"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _createUseStorageState = require("../createUseStorageState");
var _isBrowser = _interopRequireDefault(require("../utils/isBrowser"));
var useSessionStorageState = (0, _createUseStorageState.createUseStorageState)(function () {
  return _isBrowser["default"] ? sessionStorage : undefined;
});
var _default = exports["default"] = useSessionStorageState;