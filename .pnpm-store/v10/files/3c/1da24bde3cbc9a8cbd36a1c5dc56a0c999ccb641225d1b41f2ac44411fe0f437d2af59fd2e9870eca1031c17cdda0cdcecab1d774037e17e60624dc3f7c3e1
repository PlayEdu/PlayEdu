"use strict";
"use client";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault").default;
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _react = _interopRequireDefault(require("react"));
var _CalendarOutlined = _interopRequireDefault(require("@ant-design/icons/CalendarOutlined"));
var _ClockCircleOutlined = _interopRequireDefault(require("@ant-design/icons/ClockCircleOutlined"));
var _constant = require("../generatePicker/constant");
const SuffixIcon = ({
  picker,
  hasFeedback,
  feedbackIcon,
  suffixIcon
}) => {
  if (suffixIcon === null || suffixIcon === false) {
    return null;
  }
  if (suffixIcon === true || suffixIcon === undefined) {
    return /*#__PURE__*/_react.default.createElement(_react.default.Fragment, null, picker === _constant.TIME ? /*#__PURE__*/_react.default.createElement(_ClockCircleOutlined.default, null) : /*#__PURE__*/_react.default.createElement(_CalendarOutlined.default, null), hasFeedback && feedbackIcon);
  }
  return suffixIcon;
};
var _default = exports.default = SuffixIcon;