"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _typeof2 = _interopRequireDefault(require("@babel/runtime/helpers/typeof"));
var _tslib = require("tslib");
var _useCreation = _interopRequireDefault(require("../../useCreation"));
var _useLatest = _interopRequireDefault(require("../../useLatest"));
var _useMemoizedFn = _interopRequireDefault(require("../../useMemoizedFn"));
var _useMount = _interopRequireDefault(require("../../useMount"));
var _useUnmount = _interopRequireDefault(require("../../useUnmount"));
var _useUpdate = _interopRequireDefault(require("../../useUpdate"));
var _isDev = _interopRequireDefault(require("../../utils/isDev"));
var _Fetch = _interopRequireDefault(require("./Fetch"));
function useRequestImplement(service, options, plugins) {
  if (options === void 0) {
    options = {};
  }
  if (plugins === void 0) {
    plugins = [];
  }
  var _a = options.manual,
    manual = _a === void 0 ? false : _a,
    _b = options.ready,
    ready = _b === void 0 ? true : _b,
    rest = (0, _tslib.__rest)(options, ["manual", "ready"]);
  if (_isDev["default"]) {
    if (options.defaultParams && !Array.isArray(options.defaultParams)) {
      console.warn("expected defaultParams is array, got ".concat((0, _typeof2["default"])(options.defaultParams)));
    }
  }
  var fetchOptions = (0, _tslib.__assign)({
    manual: manual,
    ready: ready
  }, rest);
  var serviceRef = (0, _useLatest["default"])(service);
  var update = (0, _useUpdate["default"])();
  var fetchInstance = (0, _useCreation["default"])(function () {
    var initState = plugins.map(function (p) {
      var _a;
      return (_a = p === null || p === void 0 ? void 0 : p.onInit) === null || _a === void 0 ? void 0 : _a.call(p, fetchOptions);
    }).filter(Boolean);
    return new _Fetch["default"](serviceRef, fetchOptions, update, Object.assign.apply(Object, (0, _tslib.__spreadArray)([{}], (0, _tslib.__read)(initState), false)));
  }, []);
  fetchInstance.options = fetchOptions;
  // run all plugins hooks
  fetchInstance.pluginImpls = plugins.map(function (p) {
    return p(fetchInstance, fetchOptions);
  });
  (0, _useMount["default"])(function () {
    if (!manual && ready) {
      // useCachePlugin can set fetchInstance.state.params from cache when init
      var params = fetchInstance.state.params || options.defaultParams || [];
      // @ts-ignore
      fetchInstance.run.apply(fetchInstance, (0, _tslib.__spreadArray)([], (0, _tslib.__read)(params), false));
    }
  });
  (0, _useUnmount["default"])(function () {
    fetchInstance.cancel();
  });
  return {
    loading: fetchInstance.state.loading,
    data: fetchInstance.state.data,
    error: fetchInstance.state.error,
    params: fetchInstance.state.params || [],
    cancel: (0, _useMemoizedFn["default"])(fetchInstance.cancel.bind(fetchInstance)),
    refresh: (0, _useMemoizedFn["default"])(fetchInstance.refresh.bind(fetchInstance)),
    refreshAsync: (0, _useMemoizedFn["default"])(fetchInstance.refreshAsync.bind(fetchInstance)),
    run: (0, _useMemoizedFn["default"])(fetchInstance.run.bind(fetchInstance)),
    runAsync: (0, _useMemoizedFn["default"])(fetchInstance.runAsync.bind(fetchInstance)),
    mutate: (0, _useMemoizedFn["default"])(fetchInstance.mutate.bind(fetchInstance))
  };
}
var _default = exports["default"] = useRequestImplement;