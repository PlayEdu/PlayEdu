"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _useRafState = _interopRequireDefault(require("../useRafState"));
var _useEventListener = _interopRequireDefault(require("../useEventListener"));
var _domTarget = require("../utils/domTarget");
var initState = {
  screenX: NaN,
  screenY: NaN,
  clientX: NaN,
  clientY: NaN,
  pageX: NaN,
  pageY: NaN,
  elementX: NaN,
  elementY: NaN,
  elementH: NaN,
  elementW: NaN,
  elementPosX: NaN,
  elementPosY: NaN
};
var _default = exports["default"] = function _default(target) {
  var _a = (0, _tslib.__read)((0, _useRafState["default"])(initState), 2),
    state = _a[0],
    setState = _a[1];
  (0, _useEventListener["default"])('mousemove', function (event) {
    var screenX = event.screenX,
      screenY = event.screenY,
      clientX = event.clientX,
      clientY = event.clientY,
      pageX = event.pageX,
      pageY = event.pageY;
    var newState = {
      screenX: screenX,
      screenY: screenY,
      clientX: clientX,
      clientY: clientY,
      pageX: pageX,
      pageY: pageY,
      elementX: NaN,
      elementY: NaN,
      elementH: NaN,
      elementW: NaN,
      elementPosX: NaN,
      elementPosY: NaN
    };
    var targetElement = (0, _domTarget.getTargetElement)(target);
    if (targetElement) {
      var _a = targetElement.getBoundingClientRect(),
        left = _a.left,
        top_1 = _a.top,
        width = _a.width,
        height = _a.height;
      newState.elementPosX = left + window.pageXOffset;
      newState.elementPosY = top_1 + window.pageYOffset;
      newState.elementX = pageX - newState.elementPosX;
      newState.elementY = pageY - newState.elementPosY;
      newState.elementW = width;
      newState.elementH = height;
    }
    setState(newState);
  }, {
    target: function target() {
      return document;
    }
  });
  return state;
};