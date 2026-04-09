"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = exports.ReadyState = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _useLatest = _interopRequireDefault(require("../useLatest"));
var _useMemoizedFn = _interopRequireDefault(require("../useMemoizedFn"));
var _useUnmount = _interopRequireDefault(require("../useUnmount"));
var ReadyState;
(function (ReadyState) {
  ReadyState[ReadyState["Connecting"] = 0] = "Connecting";
  ReadyState[ReadyState["Open"] = 1] = "Open";
  ReadyState[ReadyState["Closing"] = 2] = "Closing";
  ReadyState[ReadyState["Closed"] = 3] = "Closed";
})(ReadyState || (exports.ReadyState = ReadyState = {}));
function useWebSocket(socketUrl, options) {
  if (options === void 0) {
    options = {};
  }
  var _a = options.reconnectLimit,
    reconnectLimit = _a === void 0 ? 3 : _a,
    _b = options.reconnectInterval,
    reconnectInterval = _b === void 0 ? 3 * 1000 : _b,
    _c = options.manual,
    manual = _c === void 0 ? false : _c,
    onOpen = options.onOpen,
    onClose = options.onClose,
    onMessage = options.onMessage,
    onError = options.onError,
    protocols = options.protocols;
  var _d = (0, _tslib.__read)((0, _react.useState)(), 2),
    latestMessage = _d[0],
    setLatestMessage = _d[1];
  var _e = (0, _tslib.__read)((0, _react.useState)(ReadyState.Closed), 2),
    readyState = _e[0],
    setReadyState = _e[1];
  var onOpenRef = (0, _useLatest["default"])(onOpen);
  var onCloseRef = (0, _useLatest["default"])(onClose);
  var onMessageRef = (0, _useLatest["default"])(onMessage);
  var onErrorRef = (0, _useLatest["default"])(onError);
  var readyStateRef = (0, _useLatest["default"])(readyState);
  var reconnectTimesRef = (0, _react.useRef)(0);
  var reconnectTimerRef = (0, _react.useRef)(undefined);
  var websocketRef = (0, _react.useRef)(undefined);
  var reconnect = function reconnect() {
    var _a;
    if (reconnectTimesRef.current < reconnectLimit && ((_a = websocketRef.current) === null || _a === void 0 ? void 0 : _a.readyState) !== ReadyState.Open) {
      if (reconnectTimerRef.current) {
        clearTimeout(reconnectTimerRef.current);
      }
      reconnectTimerRef.current = setTimeout(function () {
        // eslint-disable-next-line @typescript-eslint/no-use-before-define
        connectWs();
        reconnectTimesRef.current++;
      }, reconnectInterval);
    }
  };
  var connectWs = function connectWs() {
    if (reconnectTimerRef.current) {
      clearTimeout(reconnectTimerRef.current);
    }
    if (websocketRef.current) {
      websocketRef.current.close();
    }
    var ws = new WebSocket(socketUrl, protocols);
    setReadyState(ReadyState.Connecting);
    ws.onerror = function (event) {
      var _a;
      if (websocketRef.current !== ws) {
        return;
      }
      reconnect();
      (_a = onErrorRef.current) === null || _a === void 0 ? void 0 : _a.call(onErrorRef, event, ws);
      setReadyState(ws.readyState || ReadyState.Closed);
    };
    ws.onopen = function (event) {
      var _a;
      if (websocketRef.current !== ws) {
        return;
      }
      (_a = onOpenRef.current) === null || _a === void 0 ? void 0 : _a.call(onOpenRef, event, ws);
      reconnectTimesRef.current = 0;
      setReadyState(ws.readyState || ReadyState.Open);
    };
    ws.onmessage = function (message) {
      var _a;
      if (websocketRef.current !== ws) {
        return;
      }
      (_a = onMessageRef.current) === null || _a === void 0 ? void 0 : _a.call(onMessageRef, message, ws);
      setLatestMessage(message);
    };
    ws.onclose = function (event) {
      var _a;
      (_a = onCloseRef.current) === null || _a === void 0 ? void 0 : _a.call(onCloseRef, event, ws);
      // closed by server
      if (websocketRef.current === ws) {
        reconnect();
      }
      // closed by disconnect or closed by server
      if (!websocketRef.current || websocketRef.current === ws) {
        setReadyState(ws.readyState || ReadyState.Closed);
      }
    };
    websocketRef.current = ws;
  };
  var sendMessage = function sendMessage(message) {
    var _a;
    if (readyStateRef.current === ReadyState.Open) {
      (_a = websocketRef.current) === null || _a === void 0 ? void 0 : _a.send(message);
    } else {
      throw new Error('WebSocket disconnected');
    }
  };
  var connect = function connect() {
    reconnectTimesRef.current = 0;
    connectWs();
  };
  var disconnect = function disconnect() {
    var _a;
    if (reconnectTimerRef.current) {
      clearTimeout(reconnectTimerRef.current);
    }
    reconnectTimesRef.current = reconnectLimit;
    (_a = websocketRef.current) === null || _a === void 0 ? void 0 : _a.close();
    websocketRef.current = undefined;
  };
  (0, _react.useEffect)(function () {
    if (!manual && socketUrl) {
      connect();
    }
  }, [socketUrl, manual]);
  (0, _useUnmount["default"])(function () {
    disconnect();
  });
  return {
    latestMessage: latestMessage,
    sendMessage: (0, _useMemoizedFn["default"])(sendMessage),
    connect: (0, _useMemoizedFn["default"])(connect),
    disconnect: (0, _useMemoizedFn["default"])(disconnect),
    readyState: readyState,
    webSocketIns: websocketRef.current
  };
}
var _default = exports["default"] = useWebSocket;