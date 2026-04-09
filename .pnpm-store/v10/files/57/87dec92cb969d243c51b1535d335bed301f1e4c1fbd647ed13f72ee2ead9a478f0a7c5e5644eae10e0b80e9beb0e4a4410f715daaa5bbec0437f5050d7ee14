"use strict";
"use client";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault").default;
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _react = _interopRequireDefault(require("react"));
var _classnames = _interopRequireDefault(require("classnames"));
var _configProvider = require("../config-provider");
var _Compact = require("./Compact");
var _addon = _interopRequireDefault(require("./style/addon"));
var __rest = void 0 && (void 0).__rest || function (s, e) {
  var t = {};
  for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p) && e.indexOf(p) < 0) t[p] = s[p];
  if (s != null && typeof Object.getOwnPropertySymbols === "function") for (var i = 0, p = Object.getOwnPropertySymbols(s); i < p.length; i++) {
    if (e.indexOf(p[i]) < 0 && Object.prototype.propertyIsEnumerable.call(s, p[i])) t[p[i]] = s[p[i]];
  }
  return t;
};
const SpaceAddon = /*#__PURE__*/_react.default.forwardRef((props, ref) => {
  const {
      className,
      children,
      style,
      prefixCls: customizePrefixCls
    } = props,
    restProps = __rest(props, ["className", "children", "style", "prefixCls"]);
  const {
    getPrefixCls,
    direction: directionConfig
  } = _react.default.useContext(_configProvider.ConfigContext);
  const prefixCls = getPrefixCls('space-addon', customizePrefixCls);
  const [wrapCSSVar, hashId, cssVarCls] = (0, _addon.default)(prefixCls);
  const {
    compactItemClassnames,
    compactSize
  } = (0, _Compact.useCompactItemContext)(prefixCls, directionConfig);
  const classes = (0, _classnames.default)(prefixCls, hashId, compactItemClassnames, cssVarCls, {
    [`${prefixCls}-${compactSize}`]: compactSize
  }, className);
  return wrapCSSVar(/*#__PURE__*/_react.default.createElement("div", Object.assign({
    ref: ref,
    className: classes,
    style: style
  }, restProps), children));
});
var _default = exports.default = SpaceAddon;