"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
var _typeof = require("@babel/runtime/helpers/typeof");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _defineProperty2 = _interopRequireDefault(require("@babel/runtime/helpers/defineProperty"));
var _objectSpread2 = _interopRequireDefault(require("@babel/runtime/helpers/objectSpread2"));
var _toConsumableArray2 = _interopRequireDefault(require("@babel/runtime/helpers/toConsumableArray"));
var _objectWithoutProperties2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutProperties"));
var _context = require("@rc-component/context");
var _classnames = _interopRequireDefault(require("classnames"));
var _ref2 = require("rc-util/lib/ref");
var _react = _interopRequireWildcard(require("react"));
var React = _react;
var _ColGroup = _interopRequireDefault(require("../ColGroup"));
var _TableContext = _interopRequireDefault(require("../context/TableContext"));
var _useRenderTimes = _interopRequireDefault(require("../hooks/useRenderTimes"));
var _excluded = ["className", "noData", "columns", "flattenColumns", "colWidths", "colGroup", "columCount", "stickyOffsets", "direction", "fixHeader", "stickyTopOffset", "stickyBottomOffset", "stickyClassName", "scrollX", "tableLayout", "onScroll", "children"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function _getRequireWildcardCache(e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != _typeof(e) && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
function useColumnWidth(colWidths, columCount) {
  return (0, _react.useMemo)(function () {
    var cloneColumns = [];
    for (var i = 0; i < columCount; i += 1) {
      var val = colWidths[i];
      if (val !== undefined) {
        cloneColumns[i] = val;
      } else {
        return null;
      }
    }
    return cloneColumns;
  }, [colWidths.join('_'), columCount]);
}
var FixedHolder = /*#__PURE__*/React.forwardRef(function (props, ref) {
  if (process.env.NODE_ENV !== 'production') {
    (0, _useRenderTimes.default)(props);
  }
  var className = props.className,
    noData = props.noData,
    columns = props.columns,
    flattenColumns = props.flattenColumns,
    colWidths = props.colWidths,
    colGroup = props.colGroup,
    columCount = props.columCount,
    stickyOffsets = props.stickyOffsets,
    direction = props.direction,
    fixHeader = props.fixHeader,
    stickyTopOffset = props.stickyTopOffset,
    stickyBottomOffset = props.stickyBottomOffset,
    stickyClassName = props.stickyClassName,
    scrollX = props.scrollX,
    _props$tableLayout = props.tableLayout,
    tableLayout = _props$tableLayout === void 0 ? 'fixed' : _props$tableLayout,
    onScroll = props.onScroll,
    children = props.children,
    restProps = (0, _objectWithoutProperties2.default)(props, _excluded);
  var _useContext = (0, _context.useContext)(_TableContext.default, ['prefixCls', 'scrollbarSize', 'isSticky', 'getComponent']),
    prefixCls = _useContext.prefixCls,
    scrollbarSize = _useContext.scrollbarSize,
    isSticky = _useContext.isSticky,
    getComponent = _useContext.getComponent;
  var TableComponent = getComponent(['header', 'table'], 'table');
  var combinationScrollBarSize = isSticky && !fixHeader ? 0 : scrollbarSize;

  // Pass wheel to scroll event
  var scrollRef = React.useRef(null);
  var setScrollRef = React.useCallback(function (element) {
    (0, _ref2.fillRef)(ref, element);
    (0, _ref2.fillRef)(scrollRef, element);
  }, []);
  React.useEffect(function () {
    function onWheel(e) {
      var _ref = e,
        currentTarget = _ref.currentTarget,
        deltaX = _ref.deltaX;
      if (deltaX) {
        onScroll({
          currentTarget: currentTarget,
          scrollLeft: currentTarget.scrollLeft + deltaX
        });
        e.preventDefault();
      }
    }
    var scrollEle = scrollRef.current;
    scrollEle === null || scrollEle === void 0 || scrollEle.addEventListener('wheel', onWheel, {
      passive: false
    });
    return function () {
      scrollEle === null || scrollEle === void 0 || scrollEle.removeEventListener('wheel', onWheel);
    };
  }, []);

  // Add scrollbar column
  var lastColumn = flattenColumns[flattenColumns.length - 1];
  var ScrollBarColumn = {
    fixed: lastColumn ? lastColumn.fixed : null,
    scrollbar: true,
    onHeaderCell: function onHeaderCell() {
      return {
        className: "".concat(prefixCls, "-cell-scrollbar")
      };
    }
  };
  var columnsWithScrollbar = (0, _react.useMemo)(function () {
    return combinationScrollBarSize ? [].concat((0, _toConsumableArray2.default)(columns), [ScrollBarColumn]) : columns;
  }, [combinationScrollBarSize, columns]);
  var flattenColumnsWithScrollbar = (0, _react.useMemo)(function () {
    return combinationScrollBarSize ? [].concat((0, _toConsumableArray2.default)(flattenColumns), [ScrollBarColumn]) : flattenColumns;
  }, [combinationScrollBarSize, flattenColumns]);

  // Calculate the sticky offsets
  var headerStickyOffsets = (0, _react.useMemo)(function () {
    var right = stickyOffsets.right,
      left = stickyOffsets.left;
    return (0, _objectSpread2.default)((0, _objectSpread2.default)({}, stickyOffsets), {}, {
      left: direction === 'rtl' ? [].concat((0, _toConsumableArray2.default)(left.map(function (width) {
        return width + combinationScrollBarSize;
      })), [0]) : left,
      right: direction === 'rtl' ? right : [].concat((0, _toConsumableArray2.default)(right.map(function (width) {
        return width + combinationScrollBarSize;
      })), [0]),
      isSticky: isSticky
    });
  }, [combinationScrollBarSize, stickyOffsets, isSticky]);
  var mergedColumnWidth = useColumnWidth(colWidths, columCount);
  var isColGroupEmpty = (0, _react.useMemo)(function () {
    // use original ColGroup if no data or no calculated column width, otherwise use calculated column width
    // Return original colGroup if no data, or mergedColumnWidth is empty, or all widths are falsy
    var noWidth = !mergedColumnWidth || !mergedColumnWidth.length || mergedColumnWidth.every(function (w) {
      return !w;
    });
    return noData || noWidth;
  }, [noData, mergedColumnWidth]);
  return /*#__PURE__*/React.createElement("div", {
    style: (0, _objectSpread2.default)({
      overflow: 'hidden'
    }, isSticky ? {
      top: stickyTopOffset,
      bottom: stickyBottomOffset
    } : {}),
    ref: setScrollRef,
    className: (0, _classnames.default)(className, (0, _defineProperty2.default)({}, stickyClassName, !!stickyClassName))
  }, /*#__PURE__*/React.createElement(TableComponent, {
    style: {
      tableLayout: tableLayout,
      minWidth: '100%',
      // https://github.com/ant-design/ant-design/issues/54894
      width: scrollX
    }
  }, isColGroupEmpty ? colGroup : /*#__PURE__*/React.createElement(_ColGroup.default, {
    colWidths: [].concat((0, _toConsumableArray2.default)(mergedColumnWidth), [combinationScrollBarSize]),
    columCount: columCount + 1,
    columns: flattenColumnsWithScrollbar
  }), children((0, _objectSpread2.default)((0, _objectSpread2.default)({}, restProps), {}, {
    stickyOffsets: headerStickyOffsets,
    columns: columnsWithScrollbar,
    flattenColumns: flattenColumnsWithScrollbar
  }))));
});
if (process.env.NODE_ENV !== 'production') {
  FixedHolder.displayName = 'FixedHolder';
}

/** Return a table in div as fixed element which contains sticky info */
// export default responseImmutable(FixedHolder);
var _default = exports.default = /*#__PURE__*/React.memo(FixedHolder);