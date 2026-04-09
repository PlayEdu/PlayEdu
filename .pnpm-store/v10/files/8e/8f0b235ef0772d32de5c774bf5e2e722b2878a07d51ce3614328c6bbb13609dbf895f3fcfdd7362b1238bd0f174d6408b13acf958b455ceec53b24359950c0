"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _react = require("react");
function useLockFn(fn) {
  var _this = this;
  var lockRef = (0, _react.useRef)(false);
  return (0, _react.useCallback)(function () {
    var args = [];
    for (var _i = 0; _i < arguments.length; _i++) {
      args[_i] = arguments[_i];
    }
    return (0, _tslib.__awaiter)(_this, void 0, void 0, function () {
      var ret, e_1;
      return (0, _tslib.__generator)(this, function (_a) {
        switch (_a.label) {
          case 0:
            if (lockRef.current) {
              return [2 /*return*/];
            }
            lockRef.current = true;
            _a.label = 1;
          case 1:
            _a.trys.push([1, 3, 4, 5]);
            return [4 /*yield*/, fn.apply(void 0, (0, _tslib.__spreadArray)([], (0, _tslib.__read)(args), false))];
          case 2:
            ret = _a.sent();
            return [2 /*return*/, ret];
          case 3:
            e_1 = _a.sent();
            throw e_1;
          case 4:
            lockRef.current = false;
            return [7 /*endfinally*/];
          case 5:
            return [2 /*return*/];
        }
      });
    });
  }, [fn]);
}
var _default = exports["default"] = useLockFn;