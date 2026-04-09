"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault").default;
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.parseColor = parseColor;
var _classnames = _interopRequireDefault(require("classnames"));
var _colors = require("../_util/colors");
var _util = require("../color-picker/util");
function parseColor(prefixCls, color) {
  const isInternalColor = (0, _colors.isPresetColor)(color);
  const className = (0, _classnames.default)({
    [`${prefixCls}-${color}`]: color && isInternalColor
  });
  const overlayStyle = {};
  const arrowStyle = {};
  const rgb = (0, _util.generateColor)(color).toRgb();
  const luminance = (0.299 * rgb.r + 0.587 * rgb.g + 0.114 * rgb.b) / 255;
  const textColor = luminance < 0.5 ? '#FFF' : '#000';
  if (color && !isInternalColor) {
    overlayStyle.background = color;
    overlayStyle['--ant-tooltip-color'] = textColor;
    // @ts-ignore
    arrowStyle['--antd-arrow-background-color'] = color;
  }
  return {
    className,
    overlayStyle,
    arrowStyle
  };
}