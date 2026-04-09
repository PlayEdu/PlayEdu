"use strict";
"use client";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault").default;
var _interopRequireWildcard = require("@babel/runtime/helpers/interopRequireWildcard").default;
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var React = _interopRequireWildcard(require("react"));
var _classnames = _interopRequireDefault(require("classnames"));
var _warning = require("../_util/warning");
var _context = require("../config-provider/context");
var _locale = require("../locale");
var _empty = _interopRequireDefault(require("./empty"));
var _simple = _interopRequireDefault(require("./simple"));
var _style = _interopRequireDefault(require("./style"));
var __rest = void 0 && (void 0).__rest || function (s, e) {
  var t = {};
  for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p) && e.indexOf(p) < 0) t[p] = s[p];
  if (s != null && typeof Object.getOwnPropertySymbols === "function") for (var i = 0, p = Object.getOwnPropertySymbols(s); i < p.length; i++) {
    if (e.indexOf(p[i]) < 0 && Object.prototype.propertyIsEnumerable.call(s, p[i])) t[p[i]] = s[p[i]];
  }
  return t;
};
const defaultEmptyImg = /*#__PURE__*/React.createElement(_empty.default, null);
const simpleEmptyImg = /*#__PURE__*/React.createElement(_simple.default, null);
const Empty = props => {
  var _a;
  const {
      className,
      rootClassName,
      prefixCls: customizePrefixCls,
      image,
      description,
      children,
      imageStyle,
      style,
      classNames: emptyClassNames,
      styles
    } = props,
    restProps = __rest(props, ["className", "rootClassName", "prefixCls", "image", "description", "children", "imageStyle", "style", "classNames", "styles"]);
  const {
    getPrefixCls,
    direction,
    className: contextClassName,
    style: contextStyle,
    classNames: contextClassNames,
    styles: contextStyles,
    image: contextImage
  } = (0, _context.useComponentConfig)('empty');
  const prefixCls = getPrefixCls('empty', customizePrefixCls);
  const [wrapCSSVar, hashId, cssVarCls] = (0, _style.default)(prefixCls);
  const [locale] = (0, _locale.useLocale)('Empty');
  const des = typeof description !== 'undefined' ? description : locale === null || locale === void 0 ? void 0 : locale.description;
  const alt = typeof des === 'string' ? des : 'empty';
  const mergedImage = (_a = image !== null && image !== void 0 ? image : contextImage) !== null && _a !== void 0 ? _a : defaultEmptyImg;
  let imageNode = null;
  if (typeof mergedImage === 'string') {
    imageNode = /*#__PURE__*/React.createElement("img", {
      draggable: false,
      alt: alt,
      src: mergedImage
    });
  } else {
    imageNode = mergedImage;
  }
  // ============================= Warning ==============================
  if (process.env.NODE_ENV !== 'production') {
    const warning = (0, _warning.devUseWarning)('Empty');
    [['imageStyle', 'styles: { image: {} }']].forEach(([deprecatedName, newName]) => {
      warning.deprecated(!(deprecatedName in props), deprecatedName, newName);
    });
  }
  return wrapCSSVar(/*#__PURE__*/React.createElement("div", Object.assign({
    className: (0, _classnames.default)(hashId, cssVarCls, prefixCls, contextClassName, {
      [`${prefixCls}-normal`]: mergedImage === simpleEmptyImg,
      [`${prefixCls}-rtl`]: direction === 'rtl'
    }, className, rootClassName, contextClassNames.root, emptyClassNames === null || emptyClassNames === void 0 ? void 0 : emptyClassNames.root),
    style: Object.assign(Object.assign(Object.assign(Object.assign({}, contextStyles.root), contextStyle), styles === null || styles === void 0 ? void 0 : styles.root), style)
  }, restProps), /*#__PURE__*/React.createElement("div", {
    className: (0, _classnames.default)(`${prefixCls}-image`, contextClassNames.image, emptyClassNames === null || emptyClassNames === void 0 ? void 0 : emptyClassNames.image),
    style: Object.assign(Object.assign(Object.assign({}, imageStyle), contextStyles.image), styles === null || styles === void 0 ? void 0 : styles.image)
  }, imageNode), des && (/*#__PURE__*/React.createElement("div", {
    className: (0, _classnames.default)(`${prefixCls}-description`, contextClassNames.description, emptyClassNames === null || emptyClassNames === void 0 ? void 0 : emptyClassNames.description),
    style: Object.assign(Object.assign({}, contextStyles.description), styles === null || styles === void 0 ? void 0 : styles.description)
  }, des)), children && (/*#__PURE__*/React.createElement("div", {
    className: (0, _classnames.default)(`${prefixCls}-footer`, contextClassNames.footer, emptyClassNames === null || emptyClassNames === void 0 ? void 0 : emptyClassNames.footer),
    style: Object.assign(Object.assign({}, contextStyles.footer), styles === null || styles === void 0 ? void 0 : styles.footer)
  }, children))));
};
Empty.PRESENTED_IMAGE_DEFAULT = defaultEmptyImg;
Empty.PRESENTED_IMAGE_SIMPLE = simpleEmptyImg;
if (process.env.NODE_ENV !== 'production') {
  Empty.displayName = 'Empty';
}
var _default = exports.default = Empty;