import { useRef } from 'react';
import useLatest from '../useLatest';
import useMount from '../useMount';
import { isString } from '../utils';
import { getTargetElement } from '../utils/domTarget';
import useEffectWithTarget from '../utils/useEffectWithTarget';
var useDrag = function (data, target, options) {
    if (options === void 0) { options = {}; }
    var optionsRef = useLatest(options);
    var dataRef = useLatest(data);
    var imageElementRef = useRef(undefined);
    var dragImage = optionsRef.current.dragImage;
    useMount(function () {
        if (dragImage === null || dragImage === void 0 ? void 0 : dragImage.image) {
            var image = dragImage.image;
            if (isString(image)) {
                var imageElement = new Image();
                imageElement.src = image;
                imageElementRef.current = imageElement;
            }
            else {
                imageElementRef.current = image;
            }
        }
    });
    useEffectWithTarget(function () {
        var targetElement = getTargetElement(target);
        if (!(targetElement === null || targetElement === void 0 ? void 0 : targetElement.addEventListener)) {
            return;
        }
        var onDragStart = function (event) {
            var _a, _b;
            (_b = (_a = optionsRef.current).onDragStart) === null || _b === void 0 ? void 0 : _b.call(_a, event);
            event.dataTransfer.setData('custom', JSON.stringify(dataRef.current));
            if ((dragImage === null || dragImage === void 0 ? void 0 : dragImage.image) && imageElementRef.current) {
                var _c = dragImage.offsetX, offsetX = _c === void 0 ? 0 : _c, _d = dragImage.offsetY, offsetY = _d === void 0 ? 0 : _d;
                event.dataTransfer.setDragImage(imageElementRef.current, offsetX, offsetY);
            }
        };
        var onDragEnd = function (event) {
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
export default useDrag;
