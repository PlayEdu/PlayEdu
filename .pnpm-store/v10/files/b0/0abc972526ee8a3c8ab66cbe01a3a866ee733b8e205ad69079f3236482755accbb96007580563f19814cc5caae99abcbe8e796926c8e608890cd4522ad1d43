"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.createUseStorageState = exports.SYNC_STORAGE_EVENT_NAME = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _useEventListener = _interopRequireDefault(require("../useEventListener"));
var _useMemoizedFn = _interopRequireDefault(require("../useMemoizedFn"));
var _useUpdateEffect = _interopRequireDefault(require("../useUpdateEffect"));
var _utils = require("../utils");
var SYNC_STORAGE_EVENT_NAME = exports.SYNC_STORAGE_EVENT_NAME = 'AHOOKS_SYNC_STORAGE_EVENT_NAME';
var createUseStorageState = exports.createUseStorageState = function createUseStorageState(getStorage) {
  var useStorageState = function useStorageState(key, options) {
    if (options === void 0) {
      options = {};
    }
    var storage;
    var _a = options.listenStorageChange,
      listenStorageChange = _a === void 0 ? false : _a;
    var serializer = (0, _utils.isFunction)(options.serializer) ? options.serializer : JSON.stringify;
    var deserializer = (0, _utils.isFunction)(options.deserializer) ? options.deserializer : JSON.parse;
    var onError = (0, _utils.isFunction)(options.onError) ? options.onError : console.error;
    // https://github.com/alibaba/hooks/issues/800
    try {
      storage = getStorage();
    } catch (err) {
      onError(err);
    }
    var getStoredValue = function getStoredValue() {
      try {
        var raw = storage === null || storage === void 0 ? void 0 : storage.getItem(key);
        if (raw) {
          return deserializer(raw);
        }
      } catch (e) {
        onError(e);
      }
      if ((0, _utils.isFunction)(options.defaultValue)) {
        return options.defaultValue();
      }
      return options.defaultValue;
    };
    var _b = (0, _tslib.__read)((0, _react.useState)(getStoredValue), 2),
      state = _b[0],
      setState = _b[1];
    var stateRef = (0, _react.useRef)(state);
    stateRef.current = state;
    (0, _useUpdateEffect["default"])(function () {
      var nextState = getStoredValue();
      if (Object.is(nextState, stateRef.current)) {
        return; // 新旧状态相同，不更新 state，避免 setState 带来不必要的 re-render
      }
      stateRef.current = nextState;
      setState(nextState);
    }, [key]);
    var updateState = function updateState(value) {
      var previousState = stateRef.current;
      var currentState = (0, _utils.isFunction)(value) ? value(previousState) : value;
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
        if ((0, _utils.isUndef)(currentState)) {
          newValue = null;
          storage === null || storage === void 0 ? void 0 : storage.removeItem(key);
        } else {
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
            storageArea: storage
          }
        }));
      } catch (e) {
        onError(e);
      }
    };
    var syncState = function syncState(event) {
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
    var syncStateFromCustomEvent = function syncStateFromCustomEvent(event) {
      syncState(event.detail);
    };
    // from another document
    (0, _useEventListener["default"])('storage', syncState, {
      enable: listenStorageChange
    });
    // from the same document but different hooks
    (0, _useEventListener["default"])(SYNC_STORAGE_EVENT_NAME, syncStateFromCustomEvent, {
      enable: listenStorageChange
    });
    return [state, (0, _useMemoizedFn["default"])(updateState)];
  };
  return useStorageState;
};