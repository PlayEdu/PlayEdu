import { __read, __spreadArray } from "tslib";
import { useRef, useState } from 'react';
import useMemoizedFn from '../useMemoizedFn';
import { isNumber } from '../utils';
var dumpIndex = function (step, arr) {
    var index = step > 0
        ? step - 1 // move forward
        : arr.length + step; // move backward
    if (index >= arr.length - 1) {
        index = arr.length - 1;
    }
    if (index < 0) {
        index = 0;
    }
    return index;
};
var split = function (step, targetArr) {
    var index = dumpIndex(step, targetArr);
    return {
        _current: targetArr[index],
        _before: targetArr.slice(0, index),
        _after: targetArr.slice(index + 1),
    };
};
export default function useHistoryTravel(initialValue, maxLength) {
    if (maxLength === void 0) { maxLength = 0; }
    var _a = __read(useState({
        present: initialValue,
        past: [],
        future: [],
    }), 2), history = _a[0], setHistory = _a[1];
    var present = history.present, past = history.past, future = history.future;
    var initialValueRef = useRef(initialValue);
    var reset = function () {
        var params = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            params[_i] = arguments[_i];
        }
        var _initial = params.length > 0 ? params[0] : initialValueRef.current;
        initialValueRef.current = _initial;
        setHistory({
            present: _initial,
            future: [],
            past: [],
        });
    };
    var updateValue = function (val) {
        var _past = __spreadArray(__spreadArray([], __read(past), false), [present], false);
        var maxLengthNum = isNumber(maxLength) ? maxLength : Number(maxLength);
        // maximum number of records exceeded
        if (maxLengthNum > 0 && _past.length > maxLengthNum) {
            //delete first
            _past.splice(0, 1);
        }
        setHistory({
            present: val,
            future: [],
            past: _past,
        });
    };
    var _forward = function (step) {
        if (step === void 0) { step = 1; }
        if (future.length === 0) {
            return;
        }
        var _a = split(step, future), _before = _a._before, _current = _a._current, _after = _a._after;
        setHistory({
            past: __spreadArray(__spreadArray(__spreadArray([], __read(past), false), [present], false), __read(_before), false),
            present: _current,
            future: _after,
        });
    };
    var _backward = function (step) {
        if (step === void 0) { step = -1; }
        if (past.length === 0) {
            return;
        }
        var _a = split(step, past), _before = _a._before, _current = _a._current, _after = _a._after;
        setHistory({
            past: _before,
            present: _current,
            future: __spreadArray(__spreadArray(__spreadArray([], __read(_after), false), [present], false), __read(future), false),
        });
    };
    var go = function (step) {
        var stepNum = isNumber(step) ? step : Number(step);
        if (stepNum === 0) {
            return;
        }
        if (stepNum > 0) {
            return _forward(stepNum);
        }
        _backward(stepNum);
    };
    return {
        value: present,
        backLength: past.length,
        forwardLength: future.length,
        setValue: useMemoizedFn(updateValue),
        go: useMemoizedFn(go),
        back: useMemoizedFn(function () {
            go(-1);
        }),
        forward: useMemoizedFn(function () {
            go(1);
        }),
        reset: useMemoizedFn(reset),
    };
}
