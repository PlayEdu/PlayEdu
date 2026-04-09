"use strict";
"use client";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault").default;
var _interopRequireWildcard = require("@babel/runtime/helpers/interopRequireWildcard").default;
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = Indicator;
var React = _interopRequireWildcard(require("react"));
var _classnames = _interopRequireDefault(require("classnames"));
var _reactNode = require("../../_util/reactNode");
var _Looper = _interopRequireDefault(require("./Looper"));
function Indicator(props) {
  var _a;
  const {
    prefixCls,
    indicator,
    percent
  } = props;
  const dotClassName = `${prefixCls}-dot`;
  if (indicator && /*#__PURE__*/React.isValidElement(indicator)) {
    return (0, _reactNode.cloneElement)(indicator, {
      className: (0, _classnames.default)((_a = indicator.props) === null || _a === void 0 ? void 0 : _a.className, dotClassName),
      percent
    });
  }
  return /*#__PURE__*/React.createElement(_Looper.default, {
    prefixCls: prefixCls,
    percent: percent
  });
}