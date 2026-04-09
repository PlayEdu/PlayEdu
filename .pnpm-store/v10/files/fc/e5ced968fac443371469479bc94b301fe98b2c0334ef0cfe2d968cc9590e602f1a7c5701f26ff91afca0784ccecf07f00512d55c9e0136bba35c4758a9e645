import { __awaiter, __generator } from "tslib";
import { useEffect } from 'react';
import { isFunction } from '../utils';
function isAsyncGenerator(val) {
    return isFunction(val[Symbol.asyncIterator]);
}
function useAsyncEffect(effect, deps) {
    useEffect(function () {
        var e = effect();
        var cancelled = false;
        function execute() {
            return __awaiter(this, void 0, void 0, function () {
                var result;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            if (!isAsyncGenerator(e)) return [3 /*break*/, 4];
                            _a.label = 1;
                        case 1:
                            if (!true) return [3 /*break*/, 3];
                            return [4 /*yield*/, e.next()];
                        case 2:
                            result = _a.sent();
                            if (result.done || cancelled) {
                                return [3 /*break*/, 3];
                            }
                            return [3 /*break*/, 1];
                        case 3: return [3 /*break*/, 6];
                        case 4: return [4 /*yield*/, e];
                        case 5:
                            _a.sent();
                            _a.label = 6;
                        case 6: return [2 /*return*/];
                    }
                });
            });
        }
        execute();
        return function () {
            cancelled = true;
        };
    }, deps);
}
export default useAsyncEffect;
