import useLatest from '../useLatest';
import { getTargetElement } from '../utils/domTarget';
import getDocumentOrShadow from '../utils/getDocumentOrShadow';
import useEffectWithTarget from '../utils/useEffectWithTarget';
export default function useClickAway(onClickAway, target, eventName) {
    if (eventName === void 0) { eventName = 'click'; }
    var onClickAwayRef = useLatest(onClickAway);
    useEffectWithTarget(function () {
        var handler = function (event) {
            var targets = Array.isArray(target) ? target : [target];
            if (targets.some(function (item) {
                var targetElement = getTargetElement(item);
                return !targetElement || targetElement.contains(event.target);
            })) {
                return;
            }
            onClickAwayRef.current(event);
        };
        var documentOrShadow = getDocumentOrShadow(target);
        var eventNames = Array.isArray(eventName) ? eventName : [eventName];
        eventNames.forEach(function (event) { return documentOrShadow.addEventListener(event, handler); });
        return function () {
            eventNames.forEach(function (event) { return documentOrShadow.removeEventListener(event, handler); });
        };
    }, Array.isArray(eventName) ? eventName : [eventName], target);
}
