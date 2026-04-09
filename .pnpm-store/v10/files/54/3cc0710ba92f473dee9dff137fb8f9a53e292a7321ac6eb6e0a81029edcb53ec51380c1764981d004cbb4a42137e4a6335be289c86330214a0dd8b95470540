"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _useUpdateEffect = _interopRequireDefault(require("../../../useUpdateEffect"));
// support refreshDeps & ready
var useAutoRunPlugin = function useAutoRunPlugin(fetchInstance, _a) {
  var manual = _a.manual,
    _b = _a.ready,
    ready = _b === void 0 ? true : _b,
    _c = _a.defaultParams,
    defaultParams = _c === void 0 ? [] : _c,
    _d = _a.refreshDeps,
    refreshDeps = _d === void 0 ? [] : _d,
    refreshDepsAction = _a.refreshDepsAction;
  var hasAutoRun = (0, _react.useRef)(false);
  hasAutoRun.current = false;
  (0, _useUpdateEffect["default"])(function () {
    if (!manual && ready) {
      hasAutoRun.current = true;
      fetchInstance.run.apply(fetchInstance, (0, _tslib.__spreadArray)([], (0, _tslib.__read)(defaultParams), false));
    }
  }, [ready]);
  (0, _useUpdateEffect["default"])(function () {
    if (hasAutoRun.current) {
      return;
    }
    if (!manual) {
      hasAutoRun.current = true;
      if (refreshDepsAction) {
        refreshDepsAction();
      } else {
        fetchInstance.refresh();
      }
    }
  }, (0, _tslib.__spreadArray)([], (0, _tslib.__read)(refreshDeps), false));
  return {
    onBefore: function onBefore() {
      if (!ready) {
        return {
          stopNow: true
        };
      }
    }
  };
};
useAutoRunPlugin.onInit = function (_a) {
  var _b = _a.ready,
    ready = _b === void 0 ? true : _b,
    manual = _a.manual;
  return {
    loading: !manual && ready
  };
};
var _default = exports["default"] = useAutoRunPlugin;