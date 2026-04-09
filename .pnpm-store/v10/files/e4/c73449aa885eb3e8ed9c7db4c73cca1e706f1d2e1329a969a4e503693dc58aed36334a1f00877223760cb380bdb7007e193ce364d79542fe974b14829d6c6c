import { __assign } from "tslib";
export var fieldAdapter = function (field) {
    return ({
        getFieldInstance: function (name) { return field.getNames().includes(name); },
        setFieldsValue: field.setValues,
        getFieldsValue: field.getValues,
        resetFields: field.resetToDefault,
        validateFields: function (fields, callback) {
            field.validate(fields, callback);
        },
    });
};
export var resultAdapter = function (result) {
    var tableProps = {
        dataSource: result.tableProps.dataSource,
        loading: result.tableProps.loading,
        onSort: function (dataIndex, order) {
            var _a;
            result.tableProps.onChange({ current: result.pagination.current, pageSize: result.pagination.pageSize }, (_a = result.params[0]) === null || _a === void 0 ? void 0 : _a.filters, {
                field: dataIndex,
                order: order,
            });
        },
        onFilter: function (filterParams) {
            var _a;
            result.tableProps.onChange({ current: result.pagination.current, pageSize: result.pagination.pageSize }, filterParams, (_a = result.params[0]) === null || _a === void 0 ? void 0 : _a.sorter);
        },
    };
    var paginationProps = {
        onChange: result.pagination.changeCurrent,
        onPageSizeChange: result.pagination.changePageSize,
        current: result.pagination.current,
        pageSize: result.pagination.pageSize,
        total: result.pagination.total,
    };
    return __assign(__assign({}, result), { tableProps: tableProps, paginationProps: paginationProps });
};
