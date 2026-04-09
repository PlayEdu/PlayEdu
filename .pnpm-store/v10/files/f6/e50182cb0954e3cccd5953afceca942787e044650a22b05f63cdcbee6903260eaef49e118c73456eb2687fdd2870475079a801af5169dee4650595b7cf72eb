import { __assign } from "tslib";
import useAntdTable from '../useAntdTable';
import { fieldAdapter, resultAdapter } from './fusionAdapter';
var useFusionTable = function (service, options) {
    if (options === void 0) { options = {}; }
    var ret = useAntdTable(service, __assign(__assign({}, options), { form: options.field ? fieldAdapter(options.field) : undefined }));
    return resultAdapter(ret);
};
export default useFusionTable;
