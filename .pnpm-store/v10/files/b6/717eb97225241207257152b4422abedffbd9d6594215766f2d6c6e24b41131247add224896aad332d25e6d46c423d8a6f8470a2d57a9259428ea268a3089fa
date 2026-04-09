"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _dayjs = _interopRequireDefault(require("dayjs"));
var _react = require("react");
var _useLatest = _interopRequireDefault(require("../useLatest"));
var _index = require("../utils/index");
var calcLeft = function calcLeft(target) {
  if (!target) {
    return 0;
  }
  // https://stackoverflow.com/questions/4310953/invalid-date-in-safari
  var left = (0, _dayjs["default"])(target).valueOf() - Date.now();
  return left < 0 ? 0 : left;
};
var parseMs = function parseMs(milliseconds) {
  return {
    days: Math.floor(milliseconds / 86400000),
    hours: Math.floor(milliseconds / 3600000) % 24,
    minutes: Math.floor(milliseconds / 60000) % 60,
    seconds: Math.floor(milliseconds / 1000) % 60,
    milliseconds: Math.floor(milliseconds) % 1000
  };
};
var useCountdown = function useCountdown(options) {
  if (options === void 0) {
    options = {};
  }
  var _a = options || {},
    leftTime = _a.leftTime,
    targetDate = _a.targetDate,
    _b = _a.interval,
    interval = _b === void 0 ? 1000 : _b,
    onEnd = _a.onEnd;
  var memoLeftTime = (0, _react.useMemo)(function () {
    return (0, _index.isNumber)(leftTime) && leftTime > 0 ? Date.now() + leftTime : undefined;
  }, [leftTime]);
  var target = 'leftTime' in options ? memoLeftTime : targetDate;
  var _c = (0, _tslib.__read)((0, _react.useState)(function () {
      return calcLeft(target);
    }), 2),
    timeLeft = _c[0],
    setTimeLeft = _c[1];
  var onEndRef = (0, _useLatest["default"])(onEnd);
  (0, _react.useEffect)(function () {
    if (!target) {
      // for stop
      setTimeLeft(0);
      return;
    }
    // 立即执行一次
    setTimeLeft(calcLeft(target));
    var timer = setInterval(function () {
      var _a;
      var targetLeft = calcLeft(target);
      setTimeLeft(targetLeft);
      if (targetLeft === 0) {
        clearInterval(timer);
        (_a = onEndRef.current) === null || _a === void 0 ? void 0 : _a.call(onEndRef);
      }
    }, interval);
    return function () {
      return clearInterval(timer);
    };
  }, [target, interval]);
  var formattedRes = (0, _react.useMemo)(function () {
    return parseMs(timeLeft);
  }, [timeLeft]);
  return [timeLeft, formattedRes];
};
var _default = exports["default"] = useCountdown;