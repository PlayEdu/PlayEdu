"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
var _useMemoizedFn = _interopRequireDefault(require("../useMemoizedFn"));
function useMap(initialValue) {
  var getInitValue = function getInitValue() {
    return new Map(initialValue);
  };
  var _a = (0, _tslib.__read)((0, _react.useState)(getInitValue), 2),
    map = _a[0],
    setMap = _a[1];
  var set = function set(key, entry) {
    setMap(function (prev) {
      var temp = new Map(prev);
      temp.set(key, entry);
      return temp;
    });
  };
  var setAll = function setAll(newMap) {
    setMap(new Map(newMap));
  };
  var remove = function remove(key) {
    setMap(function (prev) {
      var temp = new Map(prev);
      temp["delete"](key);
      return temp;
    });
  };
  var reset = function reset() {
    return setMap(getInitValue());
  };
  var get = function get(key) {
    return map.get(key);
  };
  return [map, {
    set: (0, _useMemoizedFn["default"])(set),
    setAll: (0, _useMemoizedFn["default"])(setAll),
    remove: (0, _useMemoizedFn["default"])(remove),
    reset: (0, _useMemoizedFn["default"])(reset),
    get: (0, _useMemoizedFn["default"])(get)
  }];
}
var _default = exports["default"] = useMap;