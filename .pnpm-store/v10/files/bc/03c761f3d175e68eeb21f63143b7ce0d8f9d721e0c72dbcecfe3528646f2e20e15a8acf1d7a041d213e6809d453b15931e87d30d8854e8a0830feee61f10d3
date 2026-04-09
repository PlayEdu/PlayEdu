import { __read } from "tslib";
import { useEffect, useState } from 'react';
import useMemoizedFn from '../useMemoizedFn';
import isBrowser from '../utils/isBrowser';
export var ThemeMode;
(function (ThemeMode) {
    ThemeMode["LIGHT"] = "light";
    ThemeMode["DARK"] = "dark";
    ThemeMode["SYSTEM"] = "system";
})(ThemeMode || (ThemeMode = {}));
var useCurrentTheme = function () {
    var matchMedia = isBrowser ? window.matchMedia('(prefers-color-scheme: dark)') : undefined;
    var _a = __read(useState(function () {
        if (isBrowser) {
            return (matchMedia === null || matchMedia === void 0 ? void 0 : matchMedia.matches) ? ThemeMode.DARK : ThemeMode.LIGHT;
        }
        else {
            return ThemeMode.LIGHT;
        }
    }), 2), theme = _a[0], setTheme = _a[1];
    useEffect(function () {
        var onThemeChange = function (event) {
            if (event.matches) {
                setTheme(ThemeMode.DARK);
            }
            else {
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
export default function useTheme(options) {
    if (options === void 0) { options = {}; }
    var localStorageKey = options.localStorageKey;
    var _a = __read(useState(function () {
        var preferredThemeMode = (localStorageKey === null || localStorageKey === void 0 ? void 0 : localStorageKey.length) && localStorage.getItem(localStorageKey);
        return preferredThemeMode || ThemeMode.SYSTEM;
    }), 2), themeMode = _a[0], setThemeMode = _a[1];
    var setThemeModeWithLocalStorage = function (mode) {
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
        setThemeMode: useMemoizedFn(setThemeModeWithLocalStorage),
    };
}
