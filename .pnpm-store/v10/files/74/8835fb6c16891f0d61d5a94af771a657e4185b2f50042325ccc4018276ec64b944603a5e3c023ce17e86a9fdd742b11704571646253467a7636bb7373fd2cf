"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = useClickAway;
var _useLatest = _interopRequireDefault(require("../useLatest"));
var _domTarget = require("../utils/domTarget");
var _getDocumentOrShadow = _interopRequireDefault(require("../utils/getDocumentOrShadow"));
var _useEffectWithTarget = _interopRequireDefault(require("../utils/useEffectWithTarget"));
function useClickAway(onClickAway, target, eventName) {
  if (eventName === void 0) {
    eventName = 'click';
  }
  var onClickAwayRef = (0, _useLatest["default"])(onClickAway);
  (0, _useEffectWithTarget["default"])(function () {
    var handler = function handler(event) {
      var targets = Array.isArray(target) ? target : [target];
      if (targets.some(function (item) {
        var targetElement = (0, _domTarget.getTargetElement)(item);
        return !targetElement || targetElement.contains(event.target);
      })) {
        return;
      }
      onClickAwayRef.current(event);
    };
    var documentOrShadow = (0, _getDocumentOrShadow["default"])(target);
    var eventNames = Array.isArray(eventName) ? eventName : [eventName];
    eventNames.forEach(function (event) {
      return documentOrShadow.addEventListener(event, handler);
    });
    return function () {
      eventNames.forEach(function (event) {
        return documentOrShadow.removeEventListener(event, handler);
      });
    };
  }, Array.isArray(eventName) ? eventName : [eventName], target);
}