"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.ThemeMode = void 0;
exports["default"] = useTheme;
var _tslib = require("tslib");
var _react = require("react");
var _useMemoizedFn = _interopRequireDefault(require("../useMemoizedFn"));
var _isBrowser = _interopRequireDefault(require("../utils/isBrowser"));
var ThemeMode;
(function (ThemeMode) {
  ThemeMode["LIGHT"] = "light";
  ThemeMode["DARK"] = "dark";
  ThemeMode["SYSTEM"] = "system";
})(ThemeMode || (exports.ThemeMode = ThemeMode = {}));
var useCurrentTheme = function useCurrentTheme() {
  var matchMedia = _isBrowser["default"] ? window.matchMedia('(prefers-color-scheme: dark)') : undefined;
  var _a = (0, _tslib.__read)((0, _react.useState)(function () {
      if (_isBrowser["default"]) {
        return (matchMedia === null || matchMedia === void 0 ? void 0 : matchMedia.matches) ? ThemeMode.DARK : ThemeMode.LIGHT;
      } else {
        return ThemeMode.LIGHT;
      }
    }), 2),
    theme = _a[0],
    setTheme = _a[1];
  (0, _react.useEffect)(function () {
    var onThemeChange = function onThemeChange(event) {
      if (event.matches) {
        setTheme(ThemeMode.DARK);
      } else {
        setTheme(ThemeMode.LIGHT);
      }
    };
    matchMedia === null || matchMedia === void 0 ? void 0 : matchMedia.addEventListener('change', onThemeChange);
    return function () {
      matchMedia === null || matchMedia === void 0 ? void 0 : matchMedia.removeEventListener('change', onThemeChange);
    };
  }, []);
  return theme;
};
function useTheme(options) {
  if (options === void 0) {
    options = {};
  }
  var localStorageKey = options.localStorageKey;
  var _a = (0, _tslib.__read)((0, _react.useState)(function () {
      var preferredThemeMode = (localStorageKey === null || localStorageKey === void 0 ? void 0 : localStorageKey.length) && localStorage.getItem(localStorageKey);
      return preferredThemeMode || ThemeMode.SYSTEM;
    }), 2),
    themeMode = _a[0],
    setThemeMode = _a[1];
  var setThemeModeWithLocalStorage = function setThemeModeWithLocalStorage(mode) {
    setThemeMode(mode);
    if (localStorageKey === null || localStorageKey === void 0 ? void 0 : localStorageKey.length) {
      localStorage.setItem(localStorageKey, mode);
    }
  };
  var currentTheme = useCurrentTheme();
  var theme = themeMode === ThemeMode.SYSTEM ? currentTheme : themeMode;
  return {
    theme: theme,
    themeMode: themeMode,
    setThemeMode: (0, _useMemoizedFn["default"])(setThemeModeWithLocalStorage)
  };
}