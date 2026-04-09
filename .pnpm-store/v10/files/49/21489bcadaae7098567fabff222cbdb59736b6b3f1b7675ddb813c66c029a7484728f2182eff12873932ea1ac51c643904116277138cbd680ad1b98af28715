"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _isPlainObject = _interopRequireDefault(require("lodash/isPlainObject"));
var _useMemoizedFn = _interopRequireDefault(require("../useMemoizedFn"));
var _utils = require("../utils");
var _react = require("react");
function useSelections(items, options) {
  var _a, _b;
  var defaultSelected = [];
  var itemKey;
  if (Array.isArray(options)) {
    defaultSelected = options;
  } else if ((0, _isPlainObject["default"])(options)) {
    defaultSelected = (_a = options === null || options === void 0 ? void 0 : options.defaultSelected) !== null && _a !== void 0 ? _a : defaultSelected;
    itemKey = (_b = options === null || options === void 0 ? void 0 : options.itemKey) !== null && _b !== void 0 ? _b : itemKey;
  }
  var getKey = function getKey(item) {
    if ((0, _utils.isFunction)(itemKey)) {
      return itemKey(item);
    }
    if ((0, _utils.isString)(itemKey) && (0, _isPlainObject["default"])(item)) {
      return item[itemKey];
    }
    return item;
  };
  var _c = (0, _tslib.__read)((0, _react.useState)(defaultSelected), 2),
    selected = _c[0],
    setSelected = _c[1];
  var selectedMap = (0, _react.useMemo)(function () {
    var keyToItemMap = new Map();
    if (!Array.isArray(selected)) {
      return keyToItemMap;
    }
    selected.forEach(function (item) {
      keyToItemMap.set(getKey(item), item);
    });
    return keyToItemMap;
  }, [selected]);
  var isSelected = function isSelected(item) {
    return selectedMap.has(getKey(item));
  };
  var select = function select(item) {
    selectedMap.set(getKey(item), item);
    setSelected(Array.from(selectedMap.values()));
  };
  var unSelect = function unSelect(item) {
    selectedMap["delete"](getKey(item));
    setSelected(Array.from(selectedMap.values()));
  };
  var toggle = function toggle(item) {
    if (isSelected(item)) {
      unSelect(item);
    } else {
      select(item);
    }
  };
  var selectAll = function selectAll() {
    items.forEach(function (item) {
      selectedMap.set(getKey(item), item);
    });
    setSelected(Array.from(selectedMap.values()));
  };
  var unSelectAll = function unSelectAll() {
    items.forEach(function (item) {
      selectedMap["delete"](getKey(item));
    });
    setSelected(Array.from(selectedMap.values()));
  };
  var noneSelected = (0, _react.useMemo)(function () {
    return items.every(function (item) {
      return !selectedMap.has(getKey(item));
    });
  }, [items, selectedMap]);
  var allSelected = (0, _react.useMemo)(function () {
    return items.every(function (item) {
      return selectedMap.has(getKey(item));
    }) && !noneSelected;
  }, [items, selectedMap, noneSelected]);
  var partiallySelected = (0, _react.useMemo)(function () {
    return !noneSelected && !allSelected;
  }, [noneSelected, allSelected]);
  var toggleAll = function toggleAll() {
    return allSelected ? unSelectAll() : selectAll();
  };
  var clearAll = function clearAll() {
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
    select: (0, _useMemoizedFn["default"])(select),
    unSelect: (0, _useMemoizedFn["default"])(unSelect),
    toggle: (0, _useMemoizedFn["default"])(toggle),
    selectAll: (0, _useMemoizedFn["default"])(selectAll),
    unSelectAll: (0, _useMemoizedFn["default"])(unSelectAll),
    clearAll: (0, _useMemoizedFn["default"])(clearAll),
    toggleAll: (0, _useMemoizedFn["default"])(toggleAll)
  };
}
var _default = exports["default"] = useSelections;