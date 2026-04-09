"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.getScrollTop = exports.getScrollHeight = exports.getClientHeight = void 0;
var getScrollTop = exports.getScrollTop = function getScrollTop(el) {
  if (el === document || el === document.documentElement || el === document.body) {
    return Math.max(window.pageYOffset, document.documentElement.scrollTop, document.body.scrollTop);
  }
  return el.scrollTop;
};
var getScrollHeight = exports.getScrollHeight = function getScrollHeight(el) {
  return el.scrollHeight || Math.max(document.documentElement.scrollHeight, document.body.scrollHeight);
};
var getClientHeight = exports.getClientHeight = function getClientHeight(el) {
  return el.clientHeight || Math.max(document.documentElement.clientHeight, document.body.clientHeight);
};