"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.trigger = exports.subscribe = void 0;
var listeners = {};
var trigger = exports.trigger = function trigger(key, data) {
  if (listeners[key]) {
    listeners[key].forEach(function (item) {
      return item(data);
    });
  }
};
var subscribe = exports.subscribe = function subscribe(key, listener) {
  if (!listeners[key]) {
    listeners[key] = [];
  }
  listeners[key].push(listener);
  return function unsubscribe() {
    var index = listeners[key].indexOf(listener);
    listeners[key].splice(index, 1);
  };
};