import { __assign, __read, __rest, __spreadArray } from "tslib";
import { useMemo } from 'react';
import useMemoizedFn from '../useMemoizedFn';
import useRequest from '../useRequest';
var usePagination = function (service, options) {
    var _a;
    if (options === void 0) { options = {}; }
    var _b = options.defaultPageSize, defaultPageSize = _b === void 0 ? 10 : _b, _c = options.defaultCurrent, defaultCurrent = _c === void 0 ? 1 : _c, rest = __rest(options, ["defaultPageSize", "defaultCurrent"]);
    var result = useRequest(service, __assign({ defaultParams: [{ current: defaultCurrent, pageSize: defaultPageSize }], refreshDepsAction: function () {
            // eslint-disable-next-line @typescript-eslint/no-use-before-define
            changeCurrent(1);
        } }, rest));
    var _d = result.params[0] || {}, _e = _d.current, current = _e === void 0 ? 1 : _e, _f = _d.pageSize, pageSize = _f === void 0 ? defaultPageSize : _f;
    var total = ((_a = result.data) === null || _a === void 0 ? void 0 : _a.total) || 0;
    var totalPage = useMemo(function () { return Math.ceil(total / pageSize); }, [pageSize, total]);
    var onChange = function (c, p) {
        var toCurrent = c <= 0 ? 1 : c;
        var toPageSize = p <= 0 ? 1 : p;
        var tempTotalPage = Math.ceil(total / toPageSize);
        if (toCurrent > tempTotalPage) {
            toCurrent = Math.max(1, tempTotalPage);
        }
        var _a = __read(result.params || []), _b = _a[0], oldPaginationParams = _b === void 0 ? {} : _b, restParams = _a.slice(1);
        result.run.apply(result, __spreadArray([], __read(__spreadArray([
            __assign(__assign({}, oldPaginationParams), { current: toCurrent, pageSize: toPageSize })
        ], __read(restParams), false)), false));
    };
    var changeCurrent = function (c) {
        onChange(c, pageSize);
    };
    var changePageSize = function (p) {
        onChange(current, p);
    };
    return __assign(__assign({}, result), { pagination: {
            current: current,
            pageSize: pageSize,
            total: total,
            totalPage: totalPage,
            onChange: useMemoizedFn(onChange),
            changeCurrent: useMemoizedFn(changeCurrent),
            changePageSize: useMemoizedFn(changePageSize),
        } });
};
export default usePagination;
