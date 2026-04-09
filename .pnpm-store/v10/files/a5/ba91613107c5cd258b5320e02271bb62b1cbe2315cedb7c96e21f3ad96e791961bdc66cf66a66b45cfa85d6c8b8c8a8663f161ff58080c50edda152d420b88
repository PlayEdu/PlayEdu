"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _utils = require("../utils");
var NetworkEventType;
(function (NetworkEventType) {
  NetworkEventType["ONLINE"] = "online";
  NetworkEventType["OFFLINE"] = "offline";
  NetworkEventType["CHANGE"] = "change";
})(NetworkEventType || (NetworkEventType = {}));
function getConnection() {
  var nav = navigator;
  if (!(0, _utils.isObject)(nav)) {
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
    effectiveType: c.effectiveType
  };
}
function useNetwork() {
  var _a = (0, _tslib.__read)((0, _react.useState)(function () {
      return (0, _tslib.__assign)({
        since: undefined,
        online: navigator === null || navigator === void 0 ? void 0 : navigator.onLine
      }, getConnectionProperty());
    }), 2),
    state = _a[0],
    setState = _a[1];
  (0, _react.useEffect)(function () {
    var onOnline = function onOnline() {
      setState(function (prevState) {
        return (0, _tslib.__assign)((0, _tslib.__assign)({}, prevState), {
          online: true,
          since: new Date()
        });
      });
    };
    var onOffline = function onOffline() {
      setState(function (prevState) {
        return (0, _tslib.__assign)((0, _tslib.__assign)({}, prevState), {
          online: false,
          since: new Date()
        });
      });
    };
    var onConnectionChange = function onConnectionChange() {
      setState(function (prevState) {
        return (0, _tslib.__assign)((0, _tslib.__assign)({}, prevState), getConnectionProperty());
      });
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
var _default = exports["default"] = useNetwork;