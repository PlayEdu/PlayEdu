import { __read } from "tslib";
import { useRef, useState } from 'react';
import useEventListener from '../useEventListener';
import useMemoizedFn from '../useMemoizedFn';
import useUpdateEffect from '../useUpdateEffect';
import { isFunction, isUndef } from '../utils';
export var SYNC_STORAGE_EVENT_NAME = 'AHOOKS_SYNC_STORAGE_EVENT_NAME';
export var createUseStorageState = function (getStorage) {
    var useStorageState = function (key, options) {
        if (options === void 0) { options = {}; }
        var storage;
        var _a = options.listenStorageChange, listenStorageChange = _a === void 0 ? false : _a;
        var serializer = isFunction(options.serializer) ? options.serializer : JSON.stringify;
        var deserializer = isFunction(options.deserializer) ? options.deserializer : JSON.parse;
        var onError = isFunction(options.onError) ? options.onError : console.error;
        // https://github.com/alibaba/hooks/issues/800
        try {
            storage = getStorage();
        }
        catch (err) {
            onError(err);
        }
        var getStoredValue = function () {
            try {
                var raw = storage === null || storage === void 0 ? void 0 : storage.getItem(key);
                if (raw) {
                    return deserializer(raw);
                }
            }
            catch (e) {
                onError(e);
            }
            if (isFunction(options.defaultValue)) {
                return options.defaultValue();
            }
            return options.defaultValue;
        };
        var _b = __read(useState(getStoredValue), 2), state = _b[0], setState = _b[1];
        var stateRef = useRef(state);
        stateRef.current = state;
        useUpdateEffect(function () {
            var nextState = getStoredValue();
            if (Object.is(nextState, stateRef.current)) {
                return; // 新旧状态相同，不更新 state，避免 setState 带来不必要的 re-render
            }
            stateRef.current = nextState;
            setState(nextState);
        }, [key]);
        var updateState = function (value) {
            var previousState = stateRef.current;
            var currentState = isFunction(value) ? value(previousState) : value;
            if (Object.is(currentState, previousState)) {
                return; // 新旧状态相同，不更新 state，避免 setState 带来不必要的 re-render
            }
            if (!listenStorageChange) {
                stateRef.current = currentState;
                setState(currentState);
            }
            try {
                var newValue = void 0;
                var oldValue = storage === null || storage === void 0 ? void 0 : storage.getItem(key);
                if (isUndef(currentState)) {
                    newValue = null;
                    storage === null || storage === void 0 ? void 0 : storage.removeItem(key);
                }
                else {
                    newValue = serializer(currentState);
                    storage === null || storage === void 0 ? void 0 : storage.setItem(key, newValue);
                }
                dispatchEvent(
                // send custom event to communicate within same page
                // importantly this should not be a StorageEvent since those cannot
                // be constructed with a non-built-in storage area
                new CustomEvent(SYNC_STORAGE_EVENT_NAME, {
                    detail: {
                        key: key,
                        newValue: newValue,
                        oldValue: oldValue,
                        storageArea: storage,
                    },
                }));
            }
            catch (e) {
                onError(e);
            }
        };
        var syncState = function (event) {
            if (event.key !== key || event.storageArea !== storage) {
                return;
            }
            var nextState = getStoredValue();
            if (Object.is(nextState, stateRef.current)) {
                return; // 新旧状态相同，不更新 state，避免 setState 带来不必要的 re-render
            }
            stateRef.current = nextState;
            setState(nextState);
        };
        var syncStateFromCustomEvent = function (event) {
            syncState(event.detail);
        };
        // from another document
        useEventListener('storage', syncState, {
            enable: listenStorageChange,
        });
        // from the same document but different hooks
        useEventListener(SYNC_STORAGE_EVENT_NAME, syncStateFromCustomEvent, {
            enable: listenStorageChange,
        });
        return [state, useMemoizedFn(updateState)];
    };
    return useStorageState;
};
