import { __read, __spreadArray } from "tslib";
import { useRef } from 'react';
import useCreation from '../../../useCreation';
import useUnmount from '../../../useUnmount';
import { setCache, getCache } from '../utils/cache';
import { setCachePromise, getCachePromise } from '../utils/cachePromise';
import { trigger, subscribe } from '../utils/cacheSubscribe';
var useCachePlugin = function (fetchInstance, _a) {
    var cacheKey = _a.cacheKey, _b = _a.cacheTime, cacheTime = _b === void 0 ? 5 * 60 * 1000 : _b, _c = _a.staleTime, staleTime = _c === void 0 ? 0 : _c, customSetCache = _a.setCache, customGetCache = _a.getCache;
    var unSubscribeRef = useRef(undefined);
    var currentPromiseRef = useRef(undefined);
    var _setCache = function (key, cachedData) {
        if (customSetCache) {
            customSetCache(cachedData);
        }
        else {
            setCache(key, cacheTime, cachedData);
        }
        trigger(key, cachedData.data);
    };
    var _getCache = function (key, params) {
        if (params === void 0) { params = []; }
        if (customGetCache) {
            return customGetCache(params);
        }
        return getCache(key);
    };
    useCreation(function () {
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
        unSubscribeRef.current = subscribe(cacheKey, function (data) {
            fetchInstance.setState({ data: data });
        });
    }, []);
    useUnmount(function () {
        var _a;
        (_a = unSubscribeRef.current) === null || _a === void 0 ? void 0 : _a.call(unSubscribeRef);
    });
    if (!cacheKey) {
        return {};
    }
    return {
        onBefore: function (params) {
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
                    returnNow: true,
                };
            }
            else {
                // If the data is stale, return data, and request continue
                return {
                    data: cacheData === null || cacheData === void 0 ? void 0 : cacheData.data,
                    error: undefined,
                };
            }
        },
        onRequest: function (service, args) {
            var servicePromise = getCachePromise(cacheKey);
            // If has servicePromise, and is not trigger by self, then use it
            if (servicePromise && servicePromise !== currentPromiseRef.current) {
                return { servicePromise: servicePromise };
            }
            servicePromise = service.apply(void 0, __spreadArray([], __read(args), false));
            currentPromiseRef.current = servicePromise;
            setCachePromise(cacheKey, servicePromise);
            return { servicePromise: servicePromise };
        },
        onSuccess: function (data, params) {
            var _a;
            if (cacheKey) {
                // cancel subscribe, avoid trgger self
                (_a = unSubscribeRef.current) === null || _a === void 0 ? void 0 : _a.call(unSubscribeRef);
                _setCache(cacheKey, {
                    data: data,
                    params: params,
                    time: Date.now(),
                });
                // resubscribe
                unSubscribeRef.current = subscribe(cacheKey, function (d) {
                    fetchInstance.setState({ data: d });
                });
            }
        },
        onMutate: function (data) {
            var _a;
            if (cacheKey) {
                // cancel subscribe, avoid trigger self
                (_a = unSubscribeRef.current) === null || _a === void 0 ? void 0 : _a.call(unSubscribeRef);
                _setCache(cacheKey, {
                    data: data,
                    params: fetchInstance.state.params,
                    time: Date.now(),
                });
                // resubscribe
                unSubscribeRef.current = subscribe(cacheKey, function (d) {
                    fetchInstance.setState({ data: d });
                });
            }
        },
    };
};
export default useCachePlugin;
