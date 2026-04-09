import { __assign } from "tslib";
var cache = new Map();
var setCache = function (key, cacheTime, cachedData) {
    var currentCache = cache.get(key);
    if (currentCache === null || currentCache === void 0 ? void 0 : currentCache.timer) {
        clearTimeout(currentCache.timer);
    }
    var timer = undefined;
    if (cacheTime > -1) {
        // if cache out, clear it
        timer = setTimeout(function () {
            cache.delete(key);
        }, cacheTime);
    }
    cache.set(key, __assign(__assign({}, cachedData), { timer: timer }));
};
var getCache = function (key) {
    return cache.get(key);
};
var clearCache = function (key) {
    if (key) {
        var cacheKeys = Array.isArray(key) ? key : [key];
        cacheKeys.forEach(function (cacheKey) { return cache.delete(cacheKey); });
    }
    else {
        cache.clear();
    }
};
export { getCache, setCache, clearCache };
