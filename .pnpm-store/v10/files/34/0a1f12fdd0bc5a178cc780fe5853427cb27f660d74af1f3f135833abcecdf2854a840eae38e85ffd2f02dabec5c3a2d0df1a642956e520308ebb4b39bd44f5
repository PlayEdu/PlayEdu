"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _useAntdTable = _interopRequireDefault(require("../useAntdTable"));
var _fusionAdapter = require("./fusionAdapter");
var useFusionTable = function useFusionTable(service, options) {
  if (options === void 0) {
    options = {};
  }
  var ret = (0, _useAntdTable["default"])(service, (0, _tslib.__assign)((0, _tslib.__assign)({}, options), {
    form: options.field ? (0, _fusionAdapter.fieldAdapter)(options.field) : undefined
  }));
  return (0, _fusionAdapter.resultAdapter)(ret);
};
var _default = exports["default"] = useFusionTable;