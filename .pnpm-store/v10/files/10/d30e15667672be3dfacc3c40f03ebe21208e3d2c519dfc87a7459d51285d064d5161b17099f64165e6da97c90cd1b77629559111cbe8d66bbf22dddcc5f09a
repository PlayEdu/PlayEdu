"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _useEventListener = _interopRequireDefault(require("../useEventListener"));
var _useMemoizedFn = _interopRequireDefault(require("../useMemoizedFn"));
var _useRequest = _interopRequireDefault(require("../useRequest"));
var _useUpdateEffect = _interopRequireDefault(require("../useUpdateEffect"));
var _domTarget = require("../utils/domTarget");
var _rect = require("../utils/rect");
var useInfiniteScroll = function useInfiniteScroll(service, options) {
  if (options === void 0) {
    options = {};
  }
  var target = options.target,
    isNoMore = options.isNoMore,
    _a = options.threshold,
    threshold = _a === void 0 ? 100 : _a,
    _b = options.direction,
    direction = _b === void 0 ? 'bottom' : _b,
    _c = options.reloadDeps,
    reloadDeps = _c === void 0 ? [] : _c,
    manual = options.manual,
    _onBefore = options.onBefore,
    _onSuccess = options.onSuccess,
    _onError = options.onError,
    _onFinally = options.onFinally;
  var _d = (0, _tslib.__read)((0, _react.useState)(), 2),
    finalData = _d[0],
    setFinalData = _d[1];
  var _e = (0, _tslib.__read)((0, _react.useState)(false), 2),
    loadingMore = _e[0],
    setLoadingMore = _e[1];
  var isScrollToTop = direction === 'top';
  // lastScrollTop is used to determine whether the scroll direction is up or down
  var lastScrollTop = (0, _react.useRef)(undefined);
  // scrollBottom is used to record the distance from the bottom of the scroll bar
  var scrollBottom = (0, _react.useRef)(0);
  var noMore = (0, _react.useMemo)(function () {
    if (!isNoMore) {
      return false;
    }
    return isNoMore(finalData);
  }, [finalData]);
  var _f = (0, _useRequest["default"])(function (lastData) {
      return (0, _tslib.__awaiter)(void 0, void 0, void 0, function () {
        var currentData;
        return (0, _tslib.__generator)(this, function (_a) {
          switch (_a.label) {
            case 0:
              return [4 /*yield*/, service(lastData)];
            case 1:
              currentData = _a.sent();
              return [2 /*return*/, {
                currentData: currentData,
                lastData: lastData
              }];
          }
        });
      });
    }, {
      manual: manual,
      onFinally: function onFinally(_, d, e) {
        setLoadingMore(false);
        _onFinally === null || _onFinally === void 0 ? void 0 : _onFinally(d === null || d === void 0 ? void 0 : d.currentData, e);
      },
      onBefore: function onBefore() {
        return _onBefore === null || _onBefore === void 0 ? void 0 : _onBefore();
      },
      onSuccess: function onSuccess(d) {
        var _a, _b, _c;
        if (!d.lastData) {
          setFinalData((0, _tslib.__assign)((0, _tslib.__assign)({}, d.currentData), {
            list: (0, _tslib.__spreadArray)([], (0, _tslib.__read)((_a = d.currentData.list) !== null && _a !== void 0 ? _a : []), false)
          }));
        } else {
          setFinalData((0, _tslib.__assign)((0, _tslib.__assign)({}, d.currentData), {
            list: isScrollToTop ? (0, _tslib.__spreadArray)((0, _tslib.__spreadArray)([], (0, _tslib.__read)(d.currentData.list), false), (0, _tslib.__read)((_b = d.lastData.list) !== null && _b !== void 0 ? _b : []), false) : (0, _tslib.__spreadArray)((0, _tslib.__spreadArray)([], (0, _tslib.__read)((_c = d.lastData.list) !== null && _c !== void 0 ? _c : []), false), (0, _tslib.__read)(d.currentData.list), false)
          }));
        }
        setTimeout(function () {
          // use requestAnimationFrame to ensure the scroll position is updated (To ensure compatibility react 19)
          requestAnimationFrame(function () {
            if (isScrollToTop) {
              var el = (0, _domTarget.getTargetElement)(target);
              el = el === document ? document.documentElement : el;
              if (el) {
                var scrollHeight = (0, _rect.getScrollHeight)(el);
                el.scrollTo(0, scrollHeight - scrollBottom.current);
              }
            } else {
              // eslint-disable-next-line @typescript-eslint/no-use-before-define
              scrollMethod();
            }
          });
        });
        _onSuccess === null || _onSuccess === void 0 ? void 0 : _onSuccess(d.currentData);
      },
      onError: function onError(e) {
        return _onError === null || _onError === void 0 ? void 0 : _onError(e);
      }
    }),
    loading = _f.loading,
    error = _f.error,
    run = _f.run,
    runAsync = _f.runAsync,
    cancel = _f.cancel;
  var loadMore = (0, _useMemoizedFn["default"])(function () {
    if (noMore) {
      return;
    }
    setLoadingMore(true);
    run(finalData);
  });
  var runAsyncForCurrent = function runAsyncForCurrent(data) {
    return (0, _tslib.__awaiter)(void 0, void 0, void 0, function () {
      var res;
      return (0, _tslib.__generator)(this, function (_a) {
        switch (_a.label) {
          case 0:
            return [4 /*yield*/, runAsync(data)];
          case 1:
            res = _a.sent();
            return [2 /*return*/, res.currentData];
        }
      });
    });
  };
  var loadMoreAsync = (0, _useMemoizedFn["default"])(function () {
    if (noMore) {
      return Promise.reject();
    }
    setLoadingMore(true);
    return runAsyncForCurrent(finalData);
  });
  var reload = function reload() {
    setLoadingMore(false);
    return run();
  };
  var reloadAsync = function reloadAsync() {
    setLoadingMore(false);
    return runAsyncForCurrent();
  };
  var scrollMethod = function scrollMethod() {
    var el = (0, _domTarget.getTargetElement)(target);
    if (!el) {
      return;
    }
    var targetEl = el === document ? document.documentElement : el;
    var scrollTop = (0, _rect.getScrollTop)(targetEl);
    var scrollHeight = (0, _rect.getScrollHeight)(targetEl);
    var clientHeight = (0, _rect.getClientHeight)(targetEl);
    if (isScrollToTop) {
      if (lastScrollTop.current !== undefined && lastScrollTop.current > scrollTop && scrollTop <= threshold) {
        loadMore();
      }
      lastScrollTop.current = scrollTop;
      scrollBottom.current = scrollHeight - scrollTop;
    } else if (scrollHeight - scrollTop <= clientHeight + threshold) {
      loadMore();
    }
  };
  (0, _useEventListener["default"])('scroll', function () {
    if (loading || loadingMore) {
      return;
    }
    scrollMethod();
  }, {
    target: target
  });
  (0, _useUpdateEffect["default"])(function () {
    run();
  }, (0, _tslib.__spreadArray)([], (0, _tslib.__read)(reloadDeps), false));
  return {
    data: finalData,
    loading: !loadingMore && loading,
    error: error,
    loadingMore: loadingMore,
    noMore: noMore,
    loadMore: loadMore,
    loadMoreAsync: loadMoreAsync,
    reload: (0, _useMemoizedFn["default"])(reload),
    reloadAsync: (0, _useMemoizedFn["default"])(reloadAsync),
    mutate: setFinalData,
    cancel: cancel
  };
};
var _default = exports["default"] = useInfiniteScroll;