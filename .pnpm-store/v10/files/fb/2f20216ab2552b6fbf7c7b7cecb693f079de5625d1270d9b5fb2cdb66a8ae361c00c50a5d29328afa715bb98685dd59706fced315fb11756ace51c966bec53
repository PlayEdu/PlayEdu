"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _react = require("react");
var _useLatest = _interopRequireDefault(require("../useLatest"));
var _useMount = _interopRequireDefault(require("../useMount"));
var _utils = require("../utils");
var _domTarget = require("../utils/domTarget");
var _useEffectWithTarget = _interopRequireDefault(require("../utils/useEffectWithTarget"));
var useDrag = function useDrag(data, target, options) {
  if (options === void 0) {
    options = {};
  }
  var optionsRef = (0, _useLatest["default"])(options);
  var dataRef = (0, _useLatest["default"])(data);
  var imageElementRef = (0, _react.useRef)(undefined);
  var dragImage = optionsRef.current.dragImage;
  (0, _useMount["default"])(function () {
    if (dragImage === null || dragImage === void 0 ? void 0 : dragImage.image) {
      var image = dragImage.image;
      if ((0, _utils.isString)(image)) {
        var imageElement = new Image();
        imageElement.src = image;
        imageElementRef.current = imageElement;
      } else {
        imageElementRef.current = image;
      }
    }
  });
  (0, _useEffectWithTarget["default"])(function () {
    var targetElement = (0, _domTarget.getTargetElement)(target);
    if (!(targetElement === null || targetElement === void 0 ? void 0 : targetElement.addEventListener)) {
      return;
    }
    var onDragStart = function onDragStart(event) {
      var _a, _b;
      (_b = (_a = optionsRef.current).onDragStart) === null || _b === void 0 ? void 0 : _b.call(_a, event);
      event.dataTransfer.setData('custom', JSON.stringify(dataRef.current));
      if ((dragImage === null || dragImage === void 0 ? void 0 : dragImage.image) && imageElementRef.current) {
        var _c = dragImage.offsetX,
          offsetX = _c === void 0 ? 0 : _c,
          _d = dragImage.offsetY,
          offsetY = _d === void 0 ? 0 : _d;
        event.dataTransfer.setDragImage(imageElementRef.current, offsetX, offsetY);
      }
    };
    var onDragEnd = function onDragEnd(event) {
      var _a, _b;
      (_b = (_a = optionsRef.current).onDragEnd) === null || _b === void 0 ? void 0 : _b.call(_a, event);
    };
    targetElement.setAttribute('draggable', 'true');
    targetElement.addEventListener('dragstart', onDragStart);
    targetElement.addEventListener('dragend', onDragEnd);
    return function () {
      targetElement.removeEventListener('dragstart', onDragStart);
      targetElement.removeEventListener('dragend', onDragEnd);
    };
  }, [], target);
};
var _default = exports["default"] = useDrag;