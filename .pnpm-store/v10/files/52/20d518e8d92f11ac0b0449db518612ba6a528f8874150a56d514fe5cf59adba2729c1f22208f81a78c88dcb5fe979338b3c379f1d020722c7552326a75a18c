"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
function useWhyDidYouUpdate(componentName, props) {
  var prevProps = (0, _react.useRef)({});
  (0, _react.useEffect)(function () {
    if (prevProps.current) {
      var allKeys = Object.keys((0, _tslib.__assign)((0, _tslib.__assign)({}, prevProps.current), props));
      var changedProps_1 = {};
      allKeys.forEach(function (key) {
        if (!Object.is(prevProps.current[key], props[key])) {
          changedProps_1[key] = {
            from: prevProps.current[key],
            to: props[key]
          };
        }
      });
      if (Object.keys(changedProps_1).length) {
        console.log('[why-did-you-update]', componentName, changedProps_1);
      }
    }
    prevProps.current = props;
  });
}
var _default = exports["default"] = useWhyDidYouUpdate;