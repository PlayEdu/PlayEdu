import { __read } from "tslib";
import { useState } from 'react';
import useMemoizedFn from '../useMemoizedFn';
function useMap(initialValue) {
    var getInitValue = function () { return new Map(initialValue); };
    var _a = __read(useState(getInitValue), 2), map = _a[0], setMap = _a[1];
    var set = function (key, entry) {
        setMap(function (prev) {
            var temp = new Map(prev);
            temp.set(key, entry);
            return temp;
        });
    };
    var setAll = function (newMap) {
        setMap(new Map(newMap));
    };
    var remove = function (key) {
        setMap(function (prev) {
            var temp = new Map(prev);
            temp.delete(key);
            return temp;
        });
    };
    var reset = function () { return setMap(getInitValue()); };
    var get = function (key) { return map.get(key); };
    return [
        map,
        {
            set: useMemoizedFn(set),
            setAll: useMemoizedFn(setAll),
            remove: useMemoizedFn(remove),
            reset: useMemoizedFn(reset),
            get: useMemoizedFn(get),
        },
    ];
}
export default useMap;
