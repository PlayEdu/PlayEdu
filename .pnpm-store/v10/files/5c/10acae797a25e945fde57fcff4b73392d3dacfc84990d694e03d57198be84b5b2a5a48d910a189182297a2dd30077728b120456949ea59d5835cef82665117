"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _useEventListener = _interopRequireDefault(require("../useEventListener"));
var _useLatest = _interopRequireDefault(require("../useLatest"));
var _useMemoizedFn = _interopRequireDefault(require("../useMemoizedFn"));
var _useSize = _interopRequireDefault(require("../useSize"));
var _domTarget = require("../utils/domTarget");
var _utils = require("../utils");
var _useUpdateEffect = _interopRequireDefault(require("../useUpdateEffect"));
var useVirtualList = function useVirtualList(list, options) {
  var containerTarget = options.containerTarget,
    wrapperTarget = options.wrapperTarget,
    itemHeight = options.itemHeight,
    _a = options.overscan,
    overscan = _a === void 0 ? 5 : _a;
  var itemHeightRef = (0, _useLatest["default"])(itemHeight);
  var size = (0, _useSize["default"])(containerTarget);
  var scrollTriggerByScrollToFunc = (0, _react.useRef)(false);
  var _b = (0, _tslib.__read)((0, _react.useState)([]), 2),
    targetList = _b[0],
    setTargetList = _b[1];
  var _c = (0, _tslib.__read)((0, _react.useState)({}), 2),
    wrapperStyle = _c[0],
    setWrapperStyle = _c[1];
  var getVisibleCount = function getVisibleCount(containerHeight, fromIndex) {
    if ((0, _utils.isNumber)(itemHeightRef.current)) {
      return Math.ceil(containerHeight / itemHeightRef.current);
    }
    var sum = 0;
    var endIndex = 0;
    for (var i = fromIndex; i < list.length; i++) {
      var height = itemHeightRef.current(i, list[i]);
      sum += height;
      endIndex = i;
      if (sum >= containerHeight) {
        break;
      }
    }
    return endIndex - fromIndex;
  };
  var getOffset = function getOffset(scrollTop) {
    if ((0, _utils.isNumber)(itemHeightRef.current)) {
      return Math.floor(scrollTop / itemHeightRef.current);
    }
    var sum = 0;
    var offset = 0;
    for (var i = 0; i < list.length; i++) {
      var height = itemHeightRef.current(i, list[i]);
      sum += height;
      if (sum >= scrollTop) {
        offset = i;
        break;
      }
    }
    return offset + 1;
  };
  // 获取上部高度
  var getDistanceTop = function getDistanceTop(index) {
    if ((0, _utils.isNumber)(itemHeightRef.current)) {
      var height_1 = index * itemHeightRef.current;
      return height_1;
    }
    var height = list.slice(0, index).reduce(function (sum, _, i) {
      return sum + itemHeightRef.current(i, list[i]);
    }, 0);
    return height;
  };
  var totalHeight = (0, _react.useMemo)(function () {
    if ((0, _utils.isNumber)(itemHeightRef.current)) {
      return list.length * itemHeightRef.current;
    }
    return list.reduce(function (sum, _, index) {
      return sum + itemHeightRef.current(index, list[index]);
    }, 0);
  }, [list]);
  var calculateRange = function calculateRange() {
    var container = (0, _domTarget.getTargetElement)(containerTarget);
    if (container) {
      var scrollTop = container.scrollTop,
        clientHeight = container.clientHeight;
      var offset = getOffset(scrollTop);
      var visibleCount = getVisibleCount(clientHeight, offset);
      var start_1 = Math.max(0, offset - overscan);
      var end = Math.min(list.length, offset + visibleCount + overscan);
      var offsetTop = getDistanceTop(start_1);
      setWrapperStyle({
        height: totalHeight - offsetTop + 'px',
        marginTop: offsetTop + 'px'
      });
      setTargetList(list.slice(start_1, end).map(function (ele, index) {
        return {
          data: ele,
          index: index + start_1
        };
      }));
    }
  };
  (0, _useUpdateEffect["default"])(function () {
    var wrapper = (0, _domTarget.getTargetElement)(wrapperTarget);
    if (wrapper) {
      Object.keys(wrapperStyle).forEach(function (key) {
        return wrapper.style[key] = wrapperStyle[key];
      });
    }
  }, [wrapperStyle]);
  (0, _react.useEffect)(function () {
    if (!(size === null || size === void 0 ? void 0 : size.width) || !(size === null || size === void 0 ? void 0 : size.height)) {
      return;
    }
    calculateRange();
  }, [size === null || size === void 0 ? void 0 : size.width, size === null || size === void 0 ? void 0 : size.height, list]);
  (0, _useEventListener["default"])('scroll', function (e) {
    if (scrollTriggerByScrollToFunc.current) {
      scrollTriggerByScrollToFunc.current = false;
      return;
    }
    e.preventDefault();
    calculateRange();
  }, {
    target: containerTarget
  });
  var scrollTo = function scrollTo(index) {
    var container = (0, _domTarget.getTargetElement)(containerTarget);
    if (container) {
      scrollTriggerByScrollToFunc.current = true;
      container.scrollTop = getDistanceTop(index);
      calculateRange();
    }
  };
  return [targetList, (0, _useMemoizedFn["default"])(scrollTo)];
};
var _default = exports["default"] = useVirtualList;