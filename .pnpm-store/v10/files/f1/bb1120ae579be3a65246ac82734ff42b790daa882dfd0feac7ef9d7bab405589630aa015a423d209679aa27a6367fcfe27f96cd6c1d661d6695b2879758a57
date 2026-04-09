import { __assign, __read, __rest, __spreadArray } from "tslib";
import useCreation from '../../useCreation';
import useLatest from '../../useLatest';
import useMemoizedFn from '../../useMemoizedFn';
import useMount from '../../useMount';
import useUnmount from '../../useUnmount';
import useUpdate from '../../useUpdate';
import isDev from '../../utils/isDev';
import Fetch from './Fetch';
function useRequestImplement(service, options, plugins) {
    if (options === void 0) { options = {}; }
    if (plugins === void 0) { plugins = []; }
    var _a = options.manual, manual = _a === void 0 ? false : _a, _b = options.ready, ready = _b === void 0 ? true : _b, rest = __rest(options, ["manual", "ready"]);
    if (isDev) {
        if (options.defaultParams && !Array.isArray(options.defaultParams)) {
            console.warn("expected defaultParams is array, got ".concat(typeof options.defaultParams));
        }
    }
    var fetchOptions = __assign({ manual: manual, ready: ready }, rest);
    var serviceRef = useLatest(service);
    var update = useUpdate();
    var fetchInstance = useCreation(function () {
        var initState = plugins.map(function (p) { var _a; return (_a = p === null || p === void 0 ? void 0 : p.onInit) === null || _a === void 0 ? void 0 : _a.call(p, fetchOptions); }).filter(Boolean);
        return new Fetch(serviceRef, fetchOptions, update, Object.assign.apply(Object, __spreadArray([{}], __read(initState), false)));
    }, []);
    fetchInstance.options = fetchOptions;
    // run all plugins hooks
    fetchInstance.pluginImpls = plugins.map(function (p) { return p(fetchInstance, fetchOptions); });
    useMount(function () {
        if (!manual && ready) {
            // useCachePlugin can set fetchInstance.state.params from cache when init
            var params = fetchInstance.state.params || options.defaultParams || [];
            // @ts-ignore
            fetchInstance.run.apply(fetchInstance, __spreadArray([], __read(params), false));
        }
    });
    useUnmount(function () {
        fetchInstance.cancel();
    });
    return {
        loading: fetchInstance.state.loading,
        data: fetchInstance.state.data,
        error: fetchInstance.state.error,
        params: fetchInstance.state.params || [],
        cancel: useMemoizedFn(fetchInstance.cancel.bind(fetchInstance)),
        refresh: useMemoizedFn(fetchInstance.refresh.bind(fetchInstance)),
        refreshAsync: useMemoizedFn(fetchInstance.refreshAsync.bind(fetchInstance)),
        run: useMemoizedFn(fetchInstance.run.bind(fetchInstance)),
        runAsync: useMemoizedFn(fetchInstance.runAsync.bind(fetchInstance)),
        mutate: useMemoizedFn(fetchInstance.mutate.bind(fetchInstance)),
    };
}
export default useRequestImplement;
