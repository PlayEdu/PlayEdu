"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = extractStyle;
var _slicedToArray2 = _interopRequireDefault(require("@babel/runtime/helpers/slicedToArray"));
var _defineProperty2 = _interopRequireDefault(require("@babel/runtime/helpers/defineProperty"));
var _useCacheToken = require("./hooks/useCacheToken");
var _useCSSVarRegister = require("./hooks/useCSSVarRegister");
var _useStyleRegister = require("./hooks/useStyleRegister");
var _util = require("./util");
var _cacheMapUtil = require("./util/cacheMapUtil");
var ExtractStyleFns = (0, _defineProperty2.default)((0, _defineProperty2.default)((0, _defineProperty2.default)({}, _useStyleRegister.STYLE_PREFIX, _useStyleRegister.extract), _useCacheToken.TOKEN_PREFIX, _useCacheToken.extract), _useCSSVarRegister.CSS_VAR_PREFIX, _useCSSVarRegister.extract);
function isNotNull(value) {
  return value !== null;
}
function extractStyle(cache, options) {
  var _ref = typeof options === 'boolean' ? {
      plain: options
    } : options || {},
    _ref$plain = _ref.plain,
    plain = _ref$plain === void 0 ? false : _ref$plain,
    _ref$types = _ref.types,
    types = _ref$types === void 0 ? ['style', 'token', 'cssVar'] : _ref$types,
    _ref$once = _ref.once,
    once = _ref$once === void 0 ? false : _ref$once;
  var matchPrefixRegexp = new RegExp("^(".concat((typeof types === 'string' ? [types] : types).join('|'), ")%"));

  // prefix with `style` is used for `useStyleRegister` to cache style context
  var styleKeys = Array.from(cache.cache.keys()).filter(function (key) {
    return matchPrefixRegexp.test(key);
  });

  // Common effect styles like animation
  var effectStyles = {};

  // Mapping of cachePath to style hash
  var cachePathMap = {};
  var styleText = '';
  styleKeys.map(function (key) {
    if (once && cache.extracted.has(key)) {
      return null; // Skip if already extracted
    }
    var cachePath = key.replace(matchPrefixRegexp, '').replace(/%/g, '|');
    var _key$split = key.split('%'),
      _key$split2 = (0, _slicedToArray2.default)(_key$split, 1),
      prefix = _key$split2[0];
    var extractFn = ExtractStyleFns[prefix];
    var extractedStyle = extractFn(cache.cache.get(key)[1], effectStyles, {
      plain: plain
    });
    if (!extractedStyle) {
      return null;
    }
    var _extractedStyle = (0, _slicedToArray2.default)(extractedStyle, 3),
      order = _extractedStyle[0],
      styleId = _extractedStyle[1],
      styleStr = _extractedStyle[2];
    if (key.startsWith('style')) {
      cachePathMap[cachePath] = styleId;
    }

    // record that this style has been extracted
    cache.extracted.add(key);
    return [order, styleStr];
  }).filter(isNotNull).sort(function (_ref2, _ref3) {
    var _ref4 = (0, _slicedToArray2.default)(_ref2, 1),
      o1 = _ref4[0];
    var _ref5 = (0, _slicedToArray2.default)(_ref3, 1),
      o2 = _ref5[0];
    return o1 - o2;
  }).forEach(function (_ref6) {
    var _ref7 = (0, _slicedToArray2.default)(_ref6, 2),
      style = _ref7[1];
    styleText += style;
  });

  // ==================== Fill Cache Path ====================
  styleText += (0, _util.toStyleStr)(".".concat(_cacheMapUtil.ATTR_CACHE_MAP, "{content:\"").concat((0, _cacheMapUtil.serialize)(cachePathMap), "\";}"), undefined, undefined, (0, _defineProperty2.default)({}, _cacheMapUtil.ATTR_CACHE_MAP, _cacheMapUtil.ATTR_CACHE_MAP), plain);
  return styleText;
}