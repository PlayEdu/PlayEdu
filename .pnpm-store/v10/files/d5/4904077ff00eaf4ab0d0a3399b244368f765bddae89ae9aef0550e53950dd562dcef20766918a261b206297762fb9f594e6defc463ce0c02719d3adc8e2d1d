"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _domTarget = require("../utils/domTarget");
var _useEffectWithTarget = _interopRequireDefault(require("../utils/useEffectWithTarget"));
var initRect = {
  top: NaN,
  left: NaN,
  bottom: NaN,
  right: NaN,
  height: NaN,
  width: NaN
};
var initState = (0, _tslib.__assign)({
  text: ''
}, initRect);
function getRectFromSelection(selection) {
  if (!selection) {
    return initRect;
  }
  if (selection.rangeCount < 1) {
    return initRect;
  }
  var range = selection.getRangeAt(0);
  var _a = range.getBoundingClientRect(),
    height = _a.height,
    width = _a.width,
    top = _a.top,
    left = _a.left,
    right = _a.right,
    bottom = _a.bottom;
  return {
    height: height,
    width: width,
    top: top,
    left: left,
    right: right,
    bottom: bottom
  };
}
function useTextSelection(target) {
  var _a = (0, _tslib.__read)((0, _react.useState)(initState), 2),
    state = _a[0],
    setState = _a[1];
  var stateRef = (0, _react.useRef)(state);
  var isInRangeRef = (0, _react.useRef)(false);
  stateRef.current = state;
  (0, _useEffectWithTarget["default"])(function () {
    var el = (0, _domTarget.getTargetElement)(target, document);
    if (!el) {
      return;
    }
    var mouseupHandler = function mouseupHandler() {
      var selObj = null;
      var text = '';
      var rect = initRect;
      if (!window.getSelection) {
        return;
      }
      selObj = window.getSelection();
      text = selObj ? selObj.toString() : '';
      if (text && isInRangeRef.current) {
        rect = getRectFromSelection(selObj);
        setState((0, _tslib.__assign)((0, _tslib.__assign)((0, _tslib.__assign)({}, state), {
          text: text
        }), rect));
      }
    };
    // 任意点击都需要清空之前的 range
    var mousedownHandler = function mousedownHandler(e) {
      // 如果是鼠标右键需要跳过 这样选中的数据就不会被清空
      if (e.button === 2) {
        return;
      }
      if (!window.getSelection) {
        return;
      }
      if (stateRef.current.text) {
        setState((0, _tslib.__assign)({}, initState));
      }
      isInRangeRef.current = false;
      var selObj = window.getSelection();
      if (!selObj) {
        return;
      }
      selObj.removeAllRanges();
      isInRangeRef.current = el.contains(e.target);
    };
    el.addEventListener('mouseup', mouseupHandler);
    document.addEventListener('mousedown', mousedownHandler);
    return function () {
      el.removeEventListener('mouseup', mouseupHandler);
      document.removeEventListener('mousedown', mousedownHandler);
    };
  }, [], target);
  return state;
}
var _default = exports["default"] = useTextSelection;