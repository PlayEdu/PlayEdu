import { __read } from "tslib";
import isPlainObject from 'lodash/isPlainObject';
import useMemoizedFn from '../useMemoizedFn';
import { isFunction, isString } from '../utils';
import { useMemo, useState } from 'react';
function useSelections(items, options) {
    var _a, _b;
    var defaultSelected = [];
    var itemKey;
    if (Array.isArray(options)) {
        defaultSelected = options;
    }
    else if (isPlainObject(options)) {
        defaultSelected = (_a = options === null || options === void 0 ? void 0 : options.defaultSelected) !== null && _a !== void 0 ? _a : defaultSelected;
        itemKey = (_b = options === null || options === void 0 ? void 0 : options.itemKey) !== null && _b !== void 0 ? _b : itemKey;
    }
    var getKey = function (item) {
        if (isFunction(itemKey)) {
            return itemKey(item);
        }
        if (isString(itemKey) && isPlainObject(item)) {
            return item[itemKey];
        }
        return item;
    };
    var _c = __read(useState(defaultSelected), 2), selected = _c[0], setSelected = _c[1];
    var selectedMap = useMemo(function () {
        var keyToItemMap = new Map();
        if (!Array.isArray(selected)) {
            return keyToItemMap;
        }
        selected.forEach(function (item) {
            keyToItemMap.set(getKey(item), item);
        });
        return keyToItemMap;
    }, [selected]);
    var isSelected = function (item) { return selectedMap.has(getKey(item)); };
    var select = function (item) {
        selectedMap.set(getKey(item), item);
        setSelected(Array.from(selectedMap.values()));
    };
    var unSelect = function (item) {
        selectedMap.delete(getKey(item));
        setSelected(Array.from(selectedMap.values()));
    };
    var toggle = function (item) {
        if (isSelected(item)) {
            unSelect(item);
        }
        else {
            select(item);
        }
    };
    var selectAll = function () {
        items.forEach(function (item) {
            selectedMap.set(getKey(item), item);
        });
        setSelected(Array.from(selectedMap.values()));
    };
    var unSelectAll = function () {
        items.forEach(function (item) {
            selectedMap.delete(getKey(item));
        });
        setSelected(Array.from(selectedMap.values()));
    };
    var noneSelected = useMemo(function () { return items.every(function (item) { return !selectedMap.has(getKey(item)); }); }, [items, selectedMap]);
    var allSelected = useMemo(function () { return items.every(function (item) { return selectedMap.has(getKey(item)); }) && !noneSelected; }, [items, selectedMap, noneSelected]);
    var partiallySelected = useMemo(function () { return !noneSelected && !allSelected; }, [noneSelected, allSelected]);
    var toggleAll = function () { return (allSelected ? unSelectAll() : selectAll()); };
    var clearAll = function () {
        selectedMap.clear();
        setSelected([]);
    };
    return {
        selected: selected,
        noneSelected: noneSelected,
        allSelected: allSelected,
        partiallySelected: partiallySelected,
        setSelected: setSelected,
        isSelected: isSelected,
        select: useMemoizedFn(select),
        unSelect: useMemoizedFn(unSelect),
        toggle: useMemoizedFn(toggle),
        selectAll: useMemoizedFn(selectAll),
        unSelectAll: useMemoizedFn(unSelectAll),
        clearAll: useMemoizedFn(clearAll),
        toggleAll: useMemoizedFn(toggleAll),
    };
}
export default useSelections;
