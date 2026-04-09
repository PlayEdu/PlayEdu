import { getTargetElement } from '../utils/domTarget';
var checkIfAllInShadow = function (targets) {
    return targets.every(function (item) {
        var targetElement = getTargetElement(item);
        if (!targetElement) {
            return false;
        }
        if (targetElement.getRootNode() instanceof ShadowRoot) {
            return true;
        }
        return false;
    });
};
var getShadow = function (node) {
    if (!node) {
        return document;
    }
    return node.getRootNode();
};
var getDocumentOrShadow = function (target) {
    if (!target || !document.getRootNode) {
        return document;
    }
    var targets = Array.isArray(target) ? target : [target];
    if (checkIfAllInShadow(targets)) {
        return getShadow(getTargetElement(targets[0]));
    }
    return document;
};
export default getDocumentOrShadow;
