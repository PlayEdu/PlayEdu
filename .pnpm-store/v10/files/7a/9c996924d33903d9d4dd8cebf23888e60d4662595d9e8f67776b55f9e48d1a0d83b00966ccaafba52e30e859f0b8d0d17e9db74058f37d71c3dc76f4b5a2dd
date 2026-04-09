"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _useMemoizedFn = _interopRequireDefault(require("../useMemoizedFn"));
var _useRequest = _interopRequireDefault(require("../useRequest"));
var usePagination = function usePagination(service, options) {
  var _a;
  if (options === void 0) {
    options = {};
  }
  var _b = options.defaultPageSize,
    defaultPageSize = _b === void 0 ? 10 : _b,
    _c = options.defaultCurrent,
    defaultCurrent = _c === void 0 ? 1 : _c,
    rest = (0, _tslib.__rest)(options, ["defaultPageSize", "defaultCurrent"]);
  var result = (0, _useRequest["default"])(service, (0, _tslib.__assign)({
    defaultParams: [{
      current: defaultCurrent,
      pageSize: defaultPageSize
    }],
    refreshDepsAction: function refreshDepsAction() {
      // eslint-disable-next-line @typescript-eslint/no-use-before-define
      changeCurrent(1);
    }
  }, rest));
  var _d = result.params[0] || {},
    _e = _d.current,
    current = _e === void 0 ? 1 : _e,
    _f = _d.pageSize,
    pageSize = _f === void 0 ? defaultPageSize : _f;
  var total = ((_a = result.data) === null || _a === void 0 ? void 0 : _a.total) || 0;
  var totalPage = (0, _react.useMemo)(function () {
    return Math.ceil(total / pageSize);
  }, [pageSize, total]);
  var onChange = function onChange(c, p) {
    var toCurrent = c <= 0 ? 1 : c;
    var toPageSize = p <= 0 ? 1 : p;
    var tempTotalPage = Math.ceil(total / toPageSize);
    if (toCurrent > tempTotalPage) {
      toCurrent = Math.max(1, tempTotalPage);
    }
    var _a = (0, _tslib.__read)(result.params || []),
      _b = _a[0],
      oldPaginationParams = _b === void 0 ? {} : _b,
      restParams = _a.slice(1);
    result.run.apply(result, (0, _tslib.__spreadArray)([], (0, _tslib.__read)((0, _tslib.__spreadArray)([(0, _tslib.__assign)((0, _tslib.__assign)({}, oldPaginationParams), {
      current: toCurrent,
      pageSize: toPageSize
    })], (0, _tslib.__read)(restParams), false)), false));
  };
  var changeCurrent = function changeCurrent(c) {
    onChange(c, pageSize);
  };
  var changePageSize = function changePageSize(p) {
    onChange(current, p);
  };
  return (0, _tslib.__assign)((0, _tslib.__assign)({}, result), {
    pagination: {
      current: current,
      pageSize: pageSize,
      total: total,
      totalPage: totalPage,
      onChange: (0, _useMemoizedFn["default"])(onChange),
      changeCurrent: (0, _useMemoizedFn["default"])(changeCurrent),
      changePageSize: (0, _useMemoizedFn["default"])(changePageSize)
    }
  });
};
var _default = exports["default"] = usePagination;