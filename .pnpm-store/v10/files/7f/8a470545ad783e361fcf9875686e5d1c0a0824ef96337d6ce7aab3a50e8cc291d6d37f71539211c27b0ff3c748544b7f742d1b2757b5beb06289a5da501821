"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _useMemoizedFn = _interopRequireDefault(require("../useMemoizedFn"));
var _usePagination = _interopRequireDefault(require("../usePagination"));
var _useUpdateEffect = _interopRequireDefault(require("../useUpdateEffect"));
var useAntdTable = function useAntdTable(service, options) {
  var _a;
  if (options === void 0) {
    options = {};
  }
  var form = options.form,
    _b = options.defaultType,
    defaultType = _b === void 0 ? 'simple' : _b,
    defaultParams = options.defaultParams,
    _c = options.manual,
    manual = _c === void 0 ? false : _c,
    _d = options.refreshDeps,
    refreshDeps = _d === void 0 ? [] : _d,
    _e = options.ready,
    ready = _e === void 0 ? true : _e,
    rest = (0, _tslib.__rest)(options, ["form", "defaultType", "defaultParams", "manual", "refreshDeps", "ready"]);
  var result = (0, _usePagination["default"])(service, (0, _tslib.__assign)((0, _tslib.__assign)({
    ready: ready,
    manual: true
  }, rest), {
    onSuccess: function onSuccess() {
      var _a;
      var args = [];
      for (var _i = 0; _i < arguments.length; _i++) {
        args[_i] = arguments[_i];
      }
      // eslint-disable-next-line @typescript-eslint/no-use-before-define
      runSuccessRef.current = true;
      (_a = rest.onSuccess) === null || _a === void 0 ? void 0 : _a.call.apply(_a, (0, _tslib.__spreadArray)([rest], (0, _tslib.__read)(args), false));
    }
  }));
  var _f = result.params,
    params = _f === void 0 ? [] : _f,
    run = result.run;
  var cacheFormTableData = params[2] || {};
  var _g = (0, _tslib.__read)((0, _react.useState)((cacheFormTableData === null || cacheFormTableData === void 0 ? void 0 : cacheFormTableData.type) || defaultType), 2),
    type = _g[0],
    setType = _g[1];
  var allFormDataRef = (0, _react.useRef)({});
  var defaultDataSourceRef = (0, _react.useRef)([]);
  var runSuccessRef = (0, _react.useRef)(false);
  var isAntdV4 = !!(form === null || form === void 0 ? void 0 : form.getInternalHooks);
  // get current active field values
  var getActiveFieldValues = function getActiveFieldValues() {
    if (!form) {
      return {};
    }
    // antd 4
    if (isAntdV4) {
      return form.getFieldsValue(null, function () {
        return true;
      });
    }
    // antd 3
    var allFieldsValue = form.getFieldsValue();
    var activeFieldsValue = {};
    Object.keys(allFieldsValue).forEach(function (key) {
      if (form.getFieldInstance ? form.getFieldInstance(key) : true) {
        activeFieldsValue[key] = allFieldsValue[key];
      }
    });
    return activeFieldsValue;
  };
  var validateFields = function validateFields() {
    if (!form) {
      return Promise.resolve({});
    }
    var activeFieldsValue = getActiveFieldValues();
    var fields = Object.keys(activeFieldsValue);
    // antd 4
    if (isAntdV4) {
      return form.validateFields(fields);
    }
    // antd 3
    return new Promise(function (resolve, reject) {
      form.validateFields(fields, function (errors, values) {
        if (errors) {
          reject(errors);
        } else {
          resolve(values);
        }
      });
    });
  };
  var restoreForm = function restoreForm() {
    if (!form) {
      return;
    }
    // antd v4
    if (isAntdV4) {
      return form.setFieldsValue(allFormDataRef.current);
    }
    // antd v3
    var activeFieldsValue = {};
    Object.keys(allFormDataRef.current).forEach(function (key) {
      if (form.getFieldInstance ? form.getFieldInstance(key) : true) {
        activeFieldsValue[key] = allFormDataRef.current[key];
      }
    });
    form.setFieldsValue(activeFieldsValue);
  };
  var changeType = function changeType() {
    var activeFieldsValue = getActiveFieldValues();
    allFormDataRef.current = (0, _tslib.__assign)((0, _tslib.__assign)({}, allFormDataRef.current), activeFieldsValue);
    setType(function (t) {
      return t === 'simple' ? 'advance' : 'simple';
    });
  };
  var _submit = function _submit(initPagination) {
    if (!ready) {
      return;
    }
    setTimeout(function () {
      validateFields().then(function (values) {
        if (values === void 0) {
          values = {};
        }
        var pagination = initPagination || (0, _tslib.__assign)((0, _tslib.__assign)({
          pageSize: options.defaultPageSize || 10
        }, (params === null || params === void 0 ? void 0 : params[0]) || {}), {
          current: 1
        });
        if (!form) {
          // @ts-ignore
          run(pagination);
          return;
        }
        // record all form data
        allFormDataRef.current = (0, _tslib.__assign)((0, _tslib.__assign)({}, allFormDataRef.current), values);
        // @ts-ignore
        run(pagination, values, {
          allFormData: allFormDataRef.current,
          type: type
        });
      })["catch"](function (err) {
        return err;
      });
    });
  };
  var reset = function reset() {
    var _a, _b;
    if (form) {
      form.resetFields();
    }
    _submit((0, _tslib.__assign)((0, _tslib.__assign)({}, (defaultParams === null || defaultParams === void 0 ? void 0 : defaultParams[0]) || {}), {
      pageSize: options.defaultPageSize || ((_b = (_a = options.defaultParams) === null || _a === void 0 ? void 0 : _a[0]) === null || _b === void 0 ? void 0 : _b.pageSize) || 10,
      current: 1
    }));
  };
  var submit = function submit(e) {
    var _a, _b, _c;
    (_a = e === null || e === void 0 ? void 0 : e.preventDefault) === null || _a === void 0 ? void 0 : _a.call(e);
    _submit(runSuccessRef.current ? undefined : (0, _tslib.__assign)({
      pageSize: options.defaultPageSize || ((_c = (_b = options.defaultParams) === null || _b === void 0 ? void 0 : _b[0]) === null || _c === void 0 ? void 0 : _c.pageSize) || 10,
      current: 1
    }, (defaultParams === null || defaultParams === void 0 ? void 0 : defaultParams[0]) || {}));
  };
  var onTableChange = function onTableChange(pagination, filters, sorter, extra) {
    var _a = (0, _tslib.__read)(params || []),
      oldPaginationParams = _a[0],
      restParams = _a.slice(1);
    run.apply(void 0, (0, _tslib.__spreadArray)([(0, _tslib.__assign)((0, _tslib.__assign)({}, oldPaginationParams), {
      current: pagination.current,
      pageSize: pagination.pageSize,
      filters: filters,
      sorter: sorter,
      extra: extra
    })], (0, _tslib.__read)(restParams), false));
  };
  // init
  (0, _react.useEffect)(function () {
    // if has cache, use cached params. ignore manual and ready.
    if (params.length > 0) {
      allFormDataRef.current = (cacheFormTableData === null || cacheFormTableData === void 0 ? void 0 : cacheFormTableData.allFormData) || {};
      restoreForm();
      // @ts-ignore
      run.apply(void 0, (0, _tslib.__spreadArray)([], (0, _tslib.__read)(params), false));
      return;
    }
    if (ready) {
      allFormDataRef.current = (defaultParams === null || defaultParams === void 0 ? void 0 : defaultParams[1]) || {};
      restoreForm();
      if (!manual) {
        _submit(defaultParams === null || defaultParams === void 0 ? void 0 : defaultParams[0]);
      }
    }
  }, []);
  // change search type, restore form data
  (0, _useUpdateEffect["default"])(function () {
    if (!ready) {
      return;
    }
    restoreForm();
  }, [type]);
  // refresh & ready change on the same time
  var hasAutoRun = (0, _react.useRef)(false);
  hasAutoRun.current = false;
  (0, _useUpdateEffect["default"])(function () {
    if (!manual && ready) {
      hasAutoRun.current = true;
      if (form) {
        form.resetFields();
      }
      allFormDataRef.current = (defaultParams === null || defaultParams === void 0 ? void 0 : defaultParams[1]) || {};
      restoreForm();
      _submit(defaultParams === null || defaultParams === void 0 ? void 0 : defaultParams[0]);
    }
  }, [ready]);
  (0, _useUpdateEffect["default"])(function () {
    if (hasAutoRun.current) {
      return;
    }
    if (!ready) {
      return;
    }
    if (!manual) {
      hasAutoRun.current = true;
      if (options.refreshDepsAction) {
        options.refreshDepsAction();
      } else {
        result.pagination.changeCurrent(1);
      }
    }
  }, (0, _tslib.__spreadArray)([], (0, _tslib.__read)(refreshDeps), false));
  return (0, _tslib.__assign)((0, _tslib.__assign)({}, result), {
    tableProps: {
      dataSource: ((_a = result.data) === null || _a === void 0 ? void 0 : _a.list) || defaultDataSourceRef.current,
      loading: result.loading,
      onChange: (0, _useMemoizedFn["default"])(onTableChange),
      pagination: {
        current: result.pagination.current,
        pageSize: result.pagination.pageSize,
        total: result.pagination.total
      }
    },
    search: {
      submit: (0, _useMemoizedFn["default"])(submit),
      type: type,
      changeType: (0, _useMemoizedFn["default"])(changeType),
      reset: (0, _useMemoizedFn["default"])(reset)
    }
  });
};
var _default = exports["default"] = useAntdTable;