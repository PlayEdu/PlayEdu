"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.setCache = exports.getCache = exports.clearCache = void 0;
var _tslib = require("tslib");
var cache = new Map();
var setCache = exports.setCache = function setCache(key, cacheTime, cachedData) {
  var currentCache = cache.get(key);
  if (currentCache === null || currentCache === void 0 ? void 0 : currentCache.timer) {
    clearTimeout(currentCache.timer);
  }
  var timer = undefined;
  if (cacheTime > -1) {
    // if cache out, clear it
    timer = setTimeout(function () {
      cache["delete"](key);
    }, cacheTime);
  }
  cache.set(key, (0, _tslib.__assign)((0, _tslib.__assign)({}, cachedData), {
    timer: timer
  }));
};
var getCache = exports.getCache = function getCache(key) {
  return cache.get(key);
};
var clearCache = exports.clearCache = function clearCache(key) {
  if (key) {
    var cacheKeys = Array.isArray(key) ? key : [key];
    cacheKeys.forEach(function (cacheKey) {
      return cache["delete"](cacheKey);
    });
  } else {
    cache.clear();
  }
};