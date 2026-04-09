"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault").default;
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.useQRCode = void 0;
var _toConsumableArray2 = _interopRequireDefault(require("@babel/runtime/helpers/toConsumableArray"));
var _qrcodegen = require("../libs/qrcodegen");
var _utils = require("../utils");
var _react = _interopRequireDefault(require("react"));
var useQRCode = exports.useQRCode = function useQRCode(opt) {
  var value = opt.value,
    level = opt.level,
    minVersion = opt.minVersion,
    includeMargin = opt.includeMargin,
    marginSize = opt.marginSize,
    imageSettings = opt.imageSettings,
    size = opt.size,
    boostLevel = opt.boostLevel;
  var memoizedQrcode = _react.default.useMemo(function () {
    var values = Array.isArray(value) ? value : [value];
    var segments = values.reduce(function (acc, val) {
      acc.push.apply(acc, (0, _toConsumableArray2.default)(_qrcodegen.QrSegment.makeSegments(val)));
      return acc;
    }, []);
    return _qrcodegen.QrCode.encodeSegments(segments, _utils.ERROR_LEVEL_MAP[level], minVersion, undefined, undefined, boostLevel);
  }, [value, level, minVersion, boostLevel]);
  return _react.default.useMemo(function () {
    var cs = memoizedQrcode.getModules();
    var mg = (0, _utils.getMarginSize)(includeMargin, marginSize);
    var ncs = cs.length + mg * 2;
    var cis = (0, _utils.getImageSettings)(cs, size, mg, imageSettings);
    return {
      cells: cs,
      margin: mg,
      numCells: ncs,
      calculatedImageSettings: cis,
      qrcode: memoizedQrcode
    };
  }, [memoizedQrcode, size, imageSettings, includeMargin, marginSize]);
};