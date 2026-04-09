import { __assign, __read } from "tslib";
import { useRef, useState } from 'react';
import { getTargetElement } from '../utils/domTarget';
import useEffectWithTarget from '../utils/useEffectWithTarget';
var initRect = {
    top: NaN,
    left: NaN,
    bottom: NaN,
    right: NaN,
    height: NaN,
    width: NaN,
};
var initState = __assign({ text: '' }, initRect);
function getRectFromSelection(selection) {
    if (!selection) {
        return initRect;
    }
    if (selection.rangeCount < 1) {
        return initRect;
    }
    var range = selection.getRangeAt(0);
    var _a = range.getBoundingClientRect(), height = _a.height, width = _a.width, top = _a.top, left = _a.left, right = _a.right, bottom = _a.bottom;
    return {
        height: height,
        width: width,
        top: top,
        left: left,
        right: right,
        bottom: bottom,
    };
}
function useTextSelection(target) {
    var _a = __read(useState(initState), 2), state = _a[0], setState = _a[1];
    var stateRef = useRef(state);
    var isInRangeRef = useRef(false);
    stateRef.current = state;
    useEffectWithTarget(function () {
        var el = getTargetElement(target, document);
        if (!el) {
            return;
        }
        var mouseupHandler = function () {
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
                setState(__assign(__assign(__assign({}, state), { text: text }), rect));
            }
        };
        // 任意点击都需要清空之前的 range
        var mousedownHandler = function (e) {
            // 如果是鼠标右键需要跳过 这样选中的数据就不会被清空
            if (e.button === 2) {
                return;
            }
            if (!window.getSelection) {
                return;
            }
            if (stateRef.current.text) {
                setState(__assign({}, initState));
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
export default useTextSelection;
