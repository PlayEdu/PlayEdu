"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _useAutoRunPlugin = _interopRequireDefault(require("./plugins/useAutoRunPlugin"));
var _useCachePlugin = _interopRequireDefault(require("./plugins/useCachePlugin"));
var _useDebouncePlugin = _interopRequireDefault(require("./plugins/useDebouncePlugin"));
var _useLoadingDelayPlugin = _interopRequireDefault(require("./plugins/useLoadingDelayPlugin"));
var _usePollingPlugin = _interopRequireDefault(require("./plugins/usePollingPlugin"));
var _useRefreshOnWindowFocusPlugin = _interopRequireDefault(require("./plugins/useRefreshOnWindowFocusPlugin"));
var _useRetryPlugin = _interopRequireDefault(require("./plugins/useRetryPlugin"));
var _useThrottlePlugin = _interopRequireDefault(require("./plugins/useThrottlePlugin"));
var _useRequestImplement = _interopRequireDefault(require("./useRequestImplement"));
// function useRequest<TData, TParams extends any[], TFormated, TTFormated extends TFormated = any>(
//   service: Service<TData, TParams>,
//   options: OptionsWithFormat<TData, TParams, TFormated, TTFormated>,
//   plugins?: Plugin<TData, TParams>[],
// ): Result<TFormated, TParams>
// function useRequest<TData, TParams extends any[]>(
//   service: Service<TData, TParams>,
//   options?: OptionsWithoutFormat<TData, TParams>,
//   plugins?: Plugin<TData, TParams>[],
// ): Result<TData, TParams>
function useRequest(service, options, plugins) {
  return (0, _useRequestImplement["default"])(service, options, (0, _tslib.__spreadArray)((0, _tslib.__spreadArray)([], (0, _tslib.__read)(plugins || []), false), [_useDebouncePlugin["default"], _useLoadingDelayPlugin["default"], _usePollingPlugin["default"], _useRefreshOnWindowFocusPlugin["default"], _useThrottlePlugin["default"], _useAutoRunPlugin["default"], _useCachePlugin["default"], _useRetryPlugin["default"]], false));
}
var _default = exports["default"] = useRequest;