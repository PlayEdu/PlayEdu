"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
var _typeof = require("@babel/runtime/helpers/typeof");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = MeasureRow;
var React = _interopRequireWildcard(require("react"));
var _rcResizeObserver = _interopRequireDefault(require("rc-resize-observer"));
var _MeasureCell = _interopRequireDefault(require("./MeasureCell"));
var _isVisible = _interopRequireDefault(require("rc-util/lib/Dom/isVisible"));
var _context = require("@rc-component/context");
var _TableContext = _interopRequireDefault(require("../context/TableContext"));
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function _getRequireWildcardCache(e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != _typeof(e) && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
function MeasureRow(_ref) {
  var prefixCls = _ref.prefixCls,
    columnsKey = _ref.columnsKey,
    onColumnResize = _ref.onColumnResize,
    columns = _ref.columns;
  var ref = React.useRef(null);
  var _useContext = (0, _context.useContext)(_TableContext.default, ['measureRowRender']),
    measureRowRender = _useContext.measureRowRender;
  var measureRow = /*#__PURE__*/React.createElement("tr", {
    "aria-hidden": "true",
    className: "".concat(prefixCls, "-measure-row"),
    ref: ref,
    tabIndex: -1
  }, /*#__PURE__*/React.createElement(_rcResizeObserver.default.Collection, {
    onBatchResize: function onBatchResize(infoList) {
      if ((0, _isVisible.default)(ref.current)) {
        infoList.forEach(function (_ref2) {
          var columnKey = _ref2.data,
            size = _ref2.size;
          onColumnResize(columnKey, size.offsetWidth);
        });
      }
    }
  }, columnsKey.map(function (columnKey) {
    var column = columns.find(function (col) {
      return col.key === columnKey;
    });
    var rawTitle = column === null || column === void 0 ? void 0 : column.title;
    var titleForMeasure = /*#__PURE__*/React.isValidElement(rawTitle) ? /*#__PURE__*/React.cloneElement(rawTitle, {
      ref: null
    }) : rawTitle;
    return /*#__PURE__*/React.createElement(_MeasureCell.default, {
      prefixCls: prefixCls,
      key: columnKey,
      columnKey: columnKey,
      onColumnResize: onColumnResize,
      title: titleForMeasure
    });
  })));
  return measureRowRender ? measureRowRender(measureRow) : measureRow;
}