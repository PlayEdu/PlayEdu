import { __assign, __read } from "tslib";
import { useEffect, useState } from 'react';
import { isObject } from '../utils';
var NetworkEventType;
(function (NetworkEventType) {
    NetworkEventType["ONLINE"] = "online";
    NetworkEventType["OFFLINE"] = "offline";
    NetworkEventType["CHANGE"] = "change";
})(NetworkEventType || (NetworkEventType = {}));
function getConnection() {
    var nav = navigator;
    if (!isObject(nav)) {
        return null;
    }
    return nav.connection || nav.mozConnection || nav.webkitConnection;
}
function getConnectionProperty() {
    var c = getConnection();
    if (!c) {
        return {};
    }
    return {
        rtt: c.rtt,
        type: c.type,
        saveData: c.saveData,
        downlink: c.downlink,
        downlinkMax: c.downlinkMax,
        effectiveType: c.effectiveType,
    };
}
function useNetwork() {
    var _a = __read(useState(function () {
        return __assign({ since: undefined, online: navigator === null || navigator === void 0 ? void 0 : navigator.onLine }, getConnectionProperty());
    }), 2), state = _a[0], setState = _a[1];
    useEffect(function () {
        var onOnline = function () {
            setState(function (prevState) { return (__assign(__assign({}, prevState), { online: true, since: new Date() })); });
        };
        var onOffline = function () {
            setState(function (prevState) { return (__assign(__assign({}, prevState), { online: false, since: new Date() })); });
        };
        var onConnectionChange = function () {
            setState(function (prevState) { return (__assign(__assign({}, prevState), getConnectionProperty())); });
        };
        window.addEventListener(NetworkEventType.ONLINE, onOnline);
        window.addEventListener(NetworkEventType.OFFLINE, onOffline);
        var connection = getConnection();
        connection === null || connection === void 0 ? void 0 : connection.addEventListener(NetworkEventType.CHANGE, onConnectionChange);
        return function () {
            window.removeEventListener(NetworkEventType.ONLINE, onOnline);
            window.removeEventListener(NetworkEventType.OFFLINE, onOffline);
            connection === null || connection === void 0 ? void 0 : connection.removeEventListener(NetworkEventType.CHANGE, onConnectionChange);
        };
    }, []);
    return state;
}
export default useNetwork;
