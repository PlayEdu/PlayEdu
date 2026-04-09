"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.setCachePromise = exports.getCachePromise = void 0;
var cachePromise = new Map();
var getCachePromise = exports.getCachePromise = function getCachePromise(cacheKey) {
  return cachePromise.get(cacheKey);
};
var setCachePromise = exports.setCachePromise = function setCachePromise(cacheKey, promise) {
  // Should cache the same promise, cannot be promise.finally
  // Because the promise.finally will change the reference of the promise
  cachePromise.set(cacheKey, promise);
  // no use promise.finally for compatibility
  promise.then(function (res) {
    cachePromise["delete"](cacheKey);
    return res;
  })["catch"](function () {
    cachePromise["delete"](cacheKey);
  });
};