import { __values } from "tslib";
import useLatest from '../useLatest';
import { isFunction, isNumber, isString } from '../utils';
import { getTargetElement } from '../utils/domTarget';
import useDeepCompareEffectWithTarget from '../utils/useDeepCompareWithTarget';
import isAppleDevice from '../utils/isAppleDevice';
// 键盘事件 keyCode 别名
var aliasKeyCodeMap = {
    '0': 48,
    '1': 49,
    '2': 50,
    '3': 51,
    '4': 52,
    '5': 53,
    '6': 54,
    '7': 55,
    '8': 56,
    '9': 57,
    backspace: 8,
    tab: 9,
    enter: 13,
    shift: 16,
    // Legacy alias kept for compatibility; standard name is "control".
    ctrl: 17,
    control: 17,
    alt: 18,
    // Legacy alias kept for compatibility; standard name is "pause".
    pausebreak: 19,
    pause: 19,
    capslock: 20,
    // Legacy alias kept for compatibility; standard name is "escape".
    esc: 27,
    escape: 27,
    // Legacy alias kept for compatibility; standard name is "spacebar" (non-standard but widely used).
    space: 32,
    spacebar: 32,
    pageup: 33,
    pagedown: 34,
    end: 35,
    home: 36,
    // Legacy aliases kept for compatibility; standard names are "arrowleft/arrowup/arrowright/arrowdown".
    leftarrow: 37,
    arrowleft: 37,
    uparrow: 38,
    arrowup: 38,
    rightarrow: 39,
    arrowright: 39,
    downarrow: 40,
    arrowdown: 40,
    insert: 45,
    delete: 46,
    a: 65,
    b: 66,
    c: 67,
    d: 68,
    e: 69,
    f: 70,
    g: 71,
    h: 72,
    i: 73,
    j: 74,
    k: 75,
    l: 76,
    m: 77,
    n: 78,
    o: 79,
    p: 80,
    q: 81,
    r: 82,
    s: 83,
    t: 84,
    u: 85,
    v: 86,
    w: 87,
    x: 88,
    y: 89,
    z: 90,
    leftwindowkey: 91,
    rightwindowkey: 92,
    meta: isAppleDevice ? [91, 93] : [91, 92],
    // Legacy alias kept for compatibility; standard name is "contextmenu".
    selectkey: 93,
    contextmenu: 93,
    numpad0: 96,
    numpad1: 97,
    numpad2: 98,
    numpad3: 99,
    numpad4: 100,
    numpad5: 101,
    numpad6: 102,
    numpad7: 103,
    numpad8: 104,
    numpad9: 105,
    multiply: 106,
    add: 107,
    subtract: 109,
    decimalpoint: 110,
    divide: 111,
    f1: 112,
    f2: 113,
    f3: 114,
    f4: 115,
    f5: 116,
    f6: 117,
    f7: 118,
    f8: 119,
    f9: 120,
    f10: 121,
    f11: 122,
    f12: 123,
    numlock: 144,
    scrolllock: 145,
    semicolon: 186,
    equalsign: 187,
    comma: 188,
    dash: 189,
    period: 190,
    forwardslash: 191,
    graveaccent: 192,
    openbracket: 219,
    backslash: 220,
    closebracket: 221,
    singlequote: 222,
};
// 修饰键
var modifierKey = {
    ctrl: function (event) { return event.ctrlKey; },
    shift: function (event) { return event.shiftKey; },
    alt: function (event) { return event.altKey; },
    meta: function (event) {
        if (event.type === 'keyup') {
            return aliasKeyCodeMap.meta.includes(event.keyCode);
        }
        return event.metaKey;
    },
};
// 判断合法的按键类型
function isValidKeyType(value) {
    return isString(value) || isNumber(value);
}
// 根据 event 计算激活键数量
function countKeyByEvent(event) {
    var countOfModifier = Object.keys(modifierKey).reduce(function (total, key) {
        if (modifierKey[key](event)) {
            return total + 1;
        }
        return total;
    }, 0);
    // 16 17 18 91 92 是修饰键的 keyCode，如果 keyCode 是修饰键，那么激活数量就是修饰键的数量，如果不是，那么就需要 +1
    return [16, 17, 18, 91, 92].includes(event.keyCode) ? countOfModifier : countOfModifier + 1;
}
/**
 * 判断按键是否激活
 * @param [event: KeyboardEvent]键盘事件
 * @param [keyFilter: any] 当前键
 * @returns string | number | boolean
 */
function genFilterKey(event, keyFilter, exactMatch) {
    var e_1, _a;
    // 浏览器自动补全 input 的时候，会触发 keyDown、keyUp 事件，但此时 event.key 等为空
    if (!event.key) {
        return false;
    }
    // 数字类型直接匹配事件的 keyCode
    if (isNumber(keyFilter)) {
        return event.keyCode === keyFilter ? keyFilter : false;
    }
    // 字符串依次判断是否有组合键
    var genArr = keyFilter.split('.');
    var genLen = 0;
    try {
        for (var genArr_1 = __values(genArr), genArr_1_1 = genArr_1.next(); !genArr_1_1.done; genArr_1_1 = genArr_1.next()) {
            var key = genArr_1_1.value;
            // 组合键
            var genModifier = modifierKey[key];
            // keyCode 别名
            var aliasKeyCode = aliasKeyCodeMap[key.toLowerCase()];
            if ((genModifier && genModifier(event)) || (aliasKeyCode && aliasKeyCode === event.keyCode)) {
                genLen++;
            }
        }
    }
    catch (e_1_1) { e_1 = { error: e_1_1 }; }
    finally {
        try {
            if (genArr_1_1 && !genArr_1_1.done && (_a = genArr_1.return)) _a.call(genArr_1);
        }
        finally { if (e_1) throw e_1.error; }
    }
    /**
     * 需要判断触发的键位和监听的键位完全一致，判断方法就是触发的键位里有且等于监听的键位
     * genLen === genArr.length 能判断出来触发的键位里有监听的键位
     * countKeyByEvent(event) === genArr.length 判断出来触发的键位数量里有且等于监听的键位数量
     * 主要用来防止按组合键其子集也会触发的情况，例如监听 ctrl+a 会触发监听 ctrl 和 a 两个键的事件。
     */
    if (exactMatch) {
        return genLen === genArr.length && countKeyByEvent(event) === genArr.length ? keyFilter : false;
    }
    return genLen === genArr.length ? keyFilter : false;
}
/**
 * 键盘输入预处理方法
 * @param [keyFilter: any] 当前键
 * @returns () => Boolean
 */
function genKeyFormatter(keyFilter, exactMatch) {
    if (isFunction(keyFilter)) {
        return keyFilter;
    }
    if (isValidKeyType(keyFilter)) {
        return function (event) { return genFilterKey(event, keyFilter, exactMatch); };
    }
    if (Array.isArray(keyFilter)) {
        return function (event) {
            return keyFilter.find(function (item) { return genFilterKey(event, item, exactMatch); });
        };
    }
    return function () { return Boolean(keyFilter); };
}
var defaultEvents = ['keydown'];
function useKeyPress(keyFilter, eventHandler, option) {
    var _a = option || {}, _b = _a.events, events = _b === void 0 ? defaultEvents : _b, target = _a.target, _c = _a.exactMatch, exactMatch = _c === void 0 ? false : _c, _d = _a.useCapture, useCapture = _d === void 0 ? false : _d;
    var eventHandlerRef = useLatest(eventHandler);
    var keyFilterRef = useLatest(keyFilter);
    useDeepCompareEffectWithTarget(function () {
        var e_2, _a;
        var _b;
        var el = getTargetElement(target, window);
        if (!el) {
            return;
        }
        var callbackHandler = function (event) {
            var _a;
            var keyEvent = event;
            var genGuard = genKeyFormatter(keyFilterRef.current, exactMatch);
            var keyGuard = genGuard(keyEvent);
            var firedKey = isValidKeyType(keyGuard) ? keyGuard : keyEvent.key;
            if (keyGuard) {
                return (_a = eventHandlerRef.current) === null || _a === void 0 ? void 0 : _a.call(eventHandlerRef, keyEvent, firedKey);
            }
        };
        try {
            for (var events_1 = __values(events), events_1_1 = events_1.next(); !events_1_1.done; events_1_1 = events_1.next()) {
                var eventName = events_1_1.value;
                (_b = el === null || el === void 0 ? void 0 : el.addEventListener) === null || _b === void 0 ? void 0 : _b.call(el, eventName, callbackHandler, useCapture);
            }
        }
        catch (e_2_1) { e_2 = { error: e_2_1 }; }
        finally {
            try {
                if (events_1_1 && !events_1_1.done && (_a = events_1.return)) _a.call(events_1);
            }
            finally { if (e_2) throw e_2.error; }
        }
        return function () {
            var e_3, _a;
            var _b;
            try {
                for (var events_2 = __values(events), events_2_1 = events_2.next(); !events_2_1.done; events_2_1 = events_2.next()) {
                    var eventName = events_2_1.value;
                    (_b = el === null || el === void 0 ? void 0 : el.removeEventListener) === null || _b === void 0 ? void 0 : _b.call(el, eventName, callbackHandler, useCapture);
                }
            }
            catch (e_3_1) { e_3 = { error: e_3_1 }; }
            finally {
                try {
                    if (events_2_1 && !events_2_1.done && (_a = events_2.return)) _a.call(events_2);
                }
                finally { if (e_3) throw e_3.error; }
            }
        };
    }, [events], target);
}
export default useKeyPress;
