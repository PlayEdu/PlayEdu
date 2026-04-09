"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _useRafState = _interopRequireDefault(require("../useRafState"));
var _useLatest = _interopRequireDefault(require("../useLatest"));
var _domTarget = require("../utils/domTarget");
var _useEffectWithTarget = _interopRequireDefault(require("../utils/useEffectWithTarget"));
function useScroll(target, shouldUpdate) {
  if (shouldUpdate === void 0) {
    shouldUpdate = function shouldUpdate() {
      return true;
    };
  }
  var _a = (0, _tslib.__read)((0, _useRafState["default"])(), 2),
    position = _a[0],
    setPosition = _a[1];
  var shouldUpdateRef = (0, _useLatest["default"])(shouldUpdate);
  (0, _useEffectWithTarget["default"])(function () {
    var el = (0, _domTarget.getTargetElement)(target, document);
    if (!el) {
      return;
    }
    var updatePosition = function updatePosition() {
      var newPosition;
      if (el === document) {
        if (document.scrollingElement) {
          newPosition = {
            left: document.scrollingElement.scrollLeft,
            top: document.scrollingElement.scrollTop
          };
        } else {
          // When in quirks mode, the scrollingElement attribute returns the HTML body element if it exists and is potentially scrollable, otherwise it returns null.
          // https://developer.mozilla.org/zh-CN/docs/Web/API/Document/scrollingElement
          // https://stackoverflow.com/questions/28633221/document-body-scrolltop-firefox-returns-0-only-js
          newPosition = {
            left: Math.max(window.pageXOffset, document.documentElement.scrollLeft, document.body.scrollLeft),
            top: Math.max(window.pageYOffset, document.documentElement.scrollTop, document.body.scrollTop)
          };
        }
      } else {
        newPosition = {
          left: el.scrollLeft,
          top: el.scrollTop
        };
      }
      if (shouldUpdateRef.current(newPosition)) {
        setPosition(newPosition);
      }
    };
    updatePosition();
    el.addEventListener('scroll', updatePosition);
    return function () {
      el.removeEventListener('scroll', updatePosition);
    };
  }, [], target);
  return position;
}
var _default = exports["default"] = useScroll;