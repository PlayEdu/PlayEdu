"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault").default;
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = useSizes;
exports.getPtg = getPtg;
var _react = _interopRequireDefault(require("react"));
var _sizeUtil = require("./sizeUtil");
function getPtg(str) {
  return Number(str.slice(0, -1)) / 100;
}
function isPtg(itemSize) {
  return typeof itemSize === 'string' && itemSize.endsWith('%');
}
/**
 * Save the size state.
 * Align the size into flex percentage base.
 */
function useSizes(items, containerSize) {
  const propSizes = items.map(item => item.size);
  const itemsCount = items.length;
  const mergedContainerSize = containerSize || 0;
  const ptg2px = ptg => ptg * mergedContainerSize;
  // We do not need care the size state match the `items` length in `useState`.
  // It will calculate later.
  const [innerSizes, setInnerSizes] = _react.default.useState(() => items.map(item => item.defaultSize));
  const sizes = _react.default.useMemo(() => {
    var _a;
    const mergedSizes = [];
    for (let i = 0; i < itemsCount; i += 1) {
      mergedSizes[i] = (_a = propSizes[i]) !== null && _a !== void 0 ? _a : innerSizes[i];
    }
    return mergedSizes;
  }, [itemsCount, innerSizes, propSizes]);
  const postPercentMinSizes = _react.default.useMemo(() => items.map(item => {
    if (isPtg(item.min)) {
      return getPtg(item.min);
    }
    return (item.min || 0) / mergedContainerSize;
  }), [items, mergedContainerSize]);
  const postPercentMaxSizes = _react.default.useMemo(() => items.map(item => {
    if (isPtg(item.max)) {
      return getPtg(item.max);
    }
    return (item.max || mergedContainerSize) / mergedContainerSize;
  }), [items, mergedContainerSize]);
  // Post handle the size. Will do:
  // 1. Convert all the px into percentage if not empty.
  // 2. Get rest percentage for exist percentage.
  // 3. Fill the rest percentage into empty item.
  const postPercentSizes = _react.default.useMemo(() => {
    const ptgList = [];
    // Fill default percentage
    for (let i = 0; i < itemsCount; i += 1) {
      const itemSize = sizes[i];
      if (isPtg(itemSize)) {
        ptgList[i] = getPtg(itemSize);
      } else if (itemSize || itemSize === 0) {
        const num = Number(itemSize);
        if (!Number.isNaN(num)) {
          ptgList[i] = num / mergedContainerSize;
        }
      } else {
        ptgList[i] = undefined;
      }
    }
    // Use autoPtgSizes to handle the undefined sizes
    return (0, _sizeUtil.autoPtgSizes)(ptgList, postPercentMinSizes, postPercentMaxSizes);
  }, [sizes, mergedContainerSize, postPercentMinSizes, postPercentMaxSizes]);
  const postPxSizes = _react.default.useMemo(() => postPercentSizes.map(ptg2px), [postPercentSizes, mergedContainerSize]);
  // If ssr, we will use the size from developer config first.
  const panelSizes = _react.default.useMemo(() => containerSize ? postPxSizes : sizes, [postPxSizes, containerSize]);
  return [panelSizes, postPxSizes, postPercentSizes, postPercentMinSizes, postPercentMaxSizes, setInnerSizes];
}