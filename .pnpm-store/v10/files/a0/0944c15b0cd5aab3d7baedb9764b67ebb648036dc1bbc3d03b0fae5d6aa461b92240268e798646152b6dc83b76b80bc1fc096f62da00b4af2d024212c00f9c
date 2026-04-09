"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.resultAdapter = exports.fieldAdapter = void 0;
var _tslib = require("tslib");
var fieldAdapter = exports.fieldAdapter = function fieldAdapter(field) {
  return {
    getFieldInstance: function getFieldInstance(name) {
      return field.getNames().includes(name);
    },
    setFieldsValue: field.setValues,
    getFieldsValue: field.getValues,
    resetFields: field.resetToDefault,
    validateFields: function validateFields(fields, callback) {
      field.validate(fields, callback);
    }
  };
};
var resultAdapter = exports.resultAdapter = function resultAdapter(result) {
  var tableProps = {
    dataSource: result.tableProps.dataSource,
    loading: result.tableProps.loading,
    onSort: function onSort(dataIndex, order) {
      var _a;
      result.tableProps.onChange({
        current: result.pagination.current,
        pageSize: result.pagination.pageSize
      }, (_a = result.params[0]) === null || _a === void 0 ? void 0 : _a.filters, {
        field: dataIndex,
        order: order
      });
    },
    onFilter: function onFilter(filterParams) {
      var _a;
      result.tableProps.onChange({
        current: result.pagination.current,
        pageSize: result.pagination.pageSize
      }, filterParams, (_a = result.params[0]) === null || _a === void 0 ? void 0 : _a.sorter);
    }
  };
  var paginationProps = {
    onChange: result.pagination.changeCurrent,
    onPageSizeChange: result.pagination.changePageSize,
    current: result.pagination.current,
    pageSize: result.pagination.pageSize,
    total: result.pagination.total
  };
  return (0, _tslib.__assign)((0, _tslib.__assign)({}, result), {
    tableProps: tableProps,
    paginationProps: paginationProps
  });
};