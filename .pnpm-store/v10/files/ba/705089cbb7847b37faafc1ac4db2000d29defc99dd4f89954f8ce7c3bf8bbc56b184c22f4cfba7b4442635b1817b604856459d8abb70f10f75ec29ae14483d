import { __assign, __awaiter, __generator, __read, __spreadArray } from "tslib";
import { useMemo, useRef, useState } from 'react';
import useEventListener from '../useEventListener';
import useMemoizedFn from '../useMemoizedFn';
import useRequest from '../useRequest';
import useUpdateEffect from '../useUpdateEffect';
import { getTargetElement } from '../utils/domTarget';
import { getClientHeight, getScrollHeight, getScrollTop } from '../utils/rect';
var useInfiniteScroll = function (service, options) {
    if (options === void 0) { options = {}; }
    var target = options.target, isNoMore = options.isNoMore, _a = options.threshold, threshold = _a === void 0 ? 100 : _a, _b = options.direction, direction = _b === void 0 ? 'bottom' : _b, _c = options.reloadDeps, reloadDeps = _c === void 0 ? [] : _c, manual = options.manual, onBefore = options.onBefore, onSuccess = options.onSuccess, onError = options.onError, onFinally = options.onFinally;
    var _d = __read(useState(), 2), finalData = _d[0], setFinalData = _d[1];
    var _e = __read(useState(false), 2), loadingMore = _e[0], setLoadingMore = _e[1];
    var isScrollToTop = direction === 'top';
    // lastScrollTop is used to determine whether the scroll direction is up or down
    var lastScrollTop = useRef(undefined);
    // scrollBottom is used to record the distance from the bottom of the scroll bar
    var scrollBottom = useRef(0);
    var noMore = useMemo(function () {
        if (!isNoMore) {
            return false;
        }
        return isNoMore(finalData);
    }, [finalData]);
    var _f = useRequest(function (lastData) { return __awaiter(void 0, void 0, void 0, function () {
        var currentData;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0: return [4 /*yield*/, service(lastData)];
                case 1:
                    currentData = _a.sent();
                    return [2 /*return*/, { currentData: currentData, lastData: lastData }];
            }
        });
    }); }, {
        manual: manual,
        onFinally: function (_, d, e) {
            setLoadingMore(false);
            onFinally === null || onFinally === void 0 ? void 0 : onFinally(d === null || d === void 0 ? void 0 : d.currentData, e);
        },
        onBefore: function () { return onBefore === null || onBefore === void 0 ? void 0 : onBefore(); },
        onSuccess: function (d) {
            var _a, _b, _c;
            if (!d.lastData) {
                setFinalData(__assign(__assign({}, d.currentData), { list: __spreadArray([], __read(((_a = d.currentData.list) !== null && _a !== void 0 ? _a : [])), false) }));
            }
            else {
                setFinalData(__assign(__assign({}, d.currentData), { list: isScrollToTop
                        ? __spreadArray(__spreadArray([], __read(d.currentData.list), false), __read(((_b = d.lastData.list) !== null && _b !== void 0 ? _b : [])), false) : __spreadArray(__spreadArray([], __read(((_c = d.lastData.list) !== null && _c !== void 0 ? _c : [])), false), __read(d.currentData.list), false) }));
            }
            setTimeout(function () {
                // use requestAnimationFrame to ensure the scroll position is updated (To ensure compatibility react 19)
                requestAnimationFrame(function () {
                    if (isScrollToTop) {
                        var el = getTargetElement(target);
                        el = el === document ? document.documentElement : el;
                        if (el) {
                            var scrollHeight = getScrollHeight(el);
                            el.scrollTo(0, scrollHeight - scrollBottom.current);
                        }
                    }
                    else {
                        // eslint-disable-next-line @typescript-eslint/no-use-before-define
                        scrollMethod();
                    }
                });
            });
            onSuccess === null || onSuccess === void 0 ? void 0 : onSuccess(d.currentData);
        },
        onError: function (e) { return onError === null || onError === void 0 ? void 0 : onError(e); },
    }), loading = _f.loading, error = _f.error, run = _f.run, runAsync = _f.runAsync, cancel = _f.cancel;
    var loadMore = useMemoizedFn(function () {
        if (noMore) {
            return;
        }
        setLoadingMore(true);
        run(finalData);
    });
    var runAsyncForCurrent = function (data) { return __awaiter(void 0, void 0, void 0, function () {
        var res;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0: return [4 /*yield*/, runAsync(data)];
                case 1:
                    res = _a.sent();
                    return [2 /*return*/, res.currentData];
            }
        });
    }); };
    var loadMoreAsync = useMemoizedFn(function () {
        if (noMore) {
            return Promise.reject();
        }
        setLoadingMore(true);
        return runAsyncForCurrent(finalData);
    });
    var reload = function () {
        setLoadingMore(false);
        return run();
    };
    var reloadAsync = function () {
        setLoadingMore(false);
        return runAsyncForCurrent();
    };
    var scrollMethod = function () {
        var el = getTargetElement(target);
        if (!el) {
            return;
        }
        var targetEl = el === document ? document.documentElement : el;
        var scrollTop = getScrollTop(targetEl);
        var scrollHeight = getScrollHeight(targetEl);
        var clientHeight = getClientHeight(targetEl);
        if (isScrollToTop) {
            if (lastScrollTop.current !== undefined &&
                lastScrollTop.current > scrollTop &&
                scrollTop <= threshold) {
                loadMore();
            }
            lastScrollTop.current = scrollTop;
            scrollBottom.current = scrollHeight - scrollTop;
        }
        else if (scrollHeight - scrollTop <= clientHeight + threshold) {
            loadMore();
        }
    };
    useEventListener('scroll', function () {
        if (loading || loadingMore) {
            return;
        }
        scrollMethod();
    }, { target: target });
    useUpdateEffect(function () {
        run();
    }, __spreadArray([], __read(reloadDeps), false));
    return {
        data: finalData,
        loading: !loadingMore && loading,
        error: error,
        loadingMore: loadingMore,
        noMore: noMore,
        loadMore: loadMore,
        loadMoreAsync: loadMoreAsync,
        reload: useMemoizedFn(reload),
        reloadAsync: useMemoizedFn(reloadAsync),
        mutate: setFinalData,
        cancel: cancel,
    };
};
export default useInfiniteScroll;
