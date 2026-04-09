"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _useCreation = _interopRequireDefault(require("../../../useCreation"));
var _useUnmount = _interopRequireDefault(require("../../../useUnmount"));
var _cache = require("../utils/cache");
var _cachePromise = require("../utils/cachePromise");
var _cacheSubscribe = require("../utils/cacheSubscribe");
var useCachePlugin = function useCachePlugin(fetchInstance, _a) {
  var cacheKey = _a.cacheKey,
    _b = _a.cacheTime,
    cacheTime = _b === void 0 ? 5 * 60 * 1000 : _b,
    _c = _a.staleTime,
    staleTime = _c === void 0 ? 0 : _c,
    customSetCache = _a.setCache,
    customGetCache = _a.getCache;
  var unSubscribeRef = (0, _react.useRef)(undefined);
  var currentPromiseRef = (0, _react.useRef)(undefined);
  var _setCache = function _setCache(key, cachedData) {
    if (customSetCache) {
      customSetCache(cachedData);
    } else {
      (0, _cache.setCache)(key, cacheTime, cachedData);
    }
    (0, _cacheSubscribe.trigger)(key, cachedData.data);
  };
  var _getCache = function _getCache(key, params) {
    if (params === void 0) {
      params = [];
    }
    if (customGetCache) {
      return customGetCache(params);
    }
    return (0, _cache.getCache)(key);
  };
  (0, _useCreation["default"])(function () {
    if (!cacheKey) {
      return;
    }
    // get data from cache when init
    var cacheData = _getCache(cacheKey);
    if (cacheData && Object.hasOwnProperty.call(cacheData, 'data')) {
      fetchInstance.state.data = cacheData.data;
      fetchInstance.state.params = cacheData.params;
      if (staleTime === -1 || Date.now() - cacheData.time <= staleTime) {
        fetchInstance.state.loading = false;
      }
    }
    // subscribe same cachekey update, trigger update
    unSubscribeRef.current = (0, _cacheSubscribe.subscribe)(cacheKey, function (data) {
      fetchInstance.setState({
        data: data
      });
    });
  }, []);
  (0, _useUnmount["default"])(function () {
    var _a;
    (_a = unSubscribeRef.current) === null || _a === void 0 ? void 0 : _a.call(unSubscribeRef);
  });
  if (!cacheKey) {
    return {};
  }
  return {
    onBefore: function onBefore(params) {
      var cacheData = _getCache(cacheKey, params);
      if (!cacheData || !Object.hasOwnProperty.call(cacheData, 'data')) {
        return {};
      }
      // If the data is fresh, stop request
      if (staleTime === -1 || Date.now() - cacheData.time <= staleTime) {
        return {
          loading: false,
          data: cacheData === null || cacheData === void 0 ? void 0 : cacheData.data,
          error: undefined,
          returnNow: true
        };
      } else {
        // If the data is stale, return data, and request continue
        return {
          data: cacheData === null || cacheData === void 0 ? void 0 : cacheData.data,
          error: undefined
        };
      }
    },
    onRequest: function onRequest(service, args) {
      var servicePromise = (0, _cachePromise.getCachePromise)(cacheKey);
      // If has servicePromise, and is not trigger by self, then use it
      if (servicePromise && servicePromise !== currentPromiseRef.current) {
        return {
          servicePromise: servicePromise
        };
      }
      servicePromise = service.apply(void 0, (0, _tslib.__spreadArray)([], (0, _tslib.__read)(args), false));
      currentPromiseRef.current = servicePromise;
      (0, _cachePromise.setCachePromise)(cacheKey, servicePromise);
      return {
        servicePromise: servicePromise
      };
    },
    onSuccess: function onSuccess(data, params) {
      var _a;
      if (cacheKey) {
        // cancel subscribe, avoid trgger self
        (_a = unSubscribeRef.current) === null || _a === void 0 ? void 0 : _a.call(unSubscribeRef);
        _setCache(cacheKey, {
          data: data,
          params: params,
          time: Date.now()
        });
        // resubscribe
        unSubscribeRef.current = (0, _cacheSubscribe.subscribe)(cacheKey, function (d) {
          fetchInstance.setState({
            data: d
          });
        });
      }
    },
    onMutate: function onMutate(data) {
      var _a;
      if (cacheKey) {
        // cancel subscribe, avoid trigger self
        (_a = unSubscribeRef.current) === null || _a === void 0 ? void 0 : _a.call(unSubscribeRef);
        _setCache(cacheKey, {
          data: data,
          params: fetchInstance.state.params,
          time: Date.now()
        });
        // resubscribe
        unSubscribeRef.current = (0, _cacheSubscribe.subscribe)(cacheKey, function (d) {
          fetchInstance.setState({
            data: d
          });
        });
      }
    }
  };
};
var _default = exports["default"] = useCachePlugin;