"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _tslib = require("tslib");
var _utils = require("../../utils");
var Fetch = /** @class */function () {
  function Fetch(serviceRef, options, subscribe, initState) {
    if (initState === void 0) {
      initState = {};
    }
    this.serviceRef = serviceRef;
    this.options = options;
    this.subscribe = subscribe;
    this.initState = initState;
    this.pluginImpls = [];
    this.count = 0;
    this.state = {
      loading: false,
      params: undefined,
      data: undefined,
      error: undefined
    };
    this.state = (0, _tslib.__assign)((0, _tslib.__assign)((0, _tslib.__assign)({}, this.state), {
      loading: !options.manual
    }), initState);
  }
  Fetch.prototype.setState = function (s) {
    if (s === void 0) {
      s = {};
    }
    this.state = (0, _tslib.__assign)((0, _tslib.__assign)({}, this.state), s);
    this.subscribe();
  };
  Fetch.prototype.runPluginHandler = function (event) {
    var rest = [];
    for (var _i = 1; _i < arguments.length; _i++) {
      rest[_i - 1] = arguments[_i];
    }
    // @ts-ignore
    var r = this.pluginImpls.map(function (i) {
      var _a;
      return (_a = i[event]) === null || _a === void 0 ? void 0 : _a.call.apply(_a, (0, _tslib.__spreadArray)([i], (0, _tslib.__read)(rest), false));
    }).filter(Boolean);
    return Object.assign.apply(Object, (0, _tslib.__spreadArray)([{}], (0, _tslib.__read)(r), false));
  };
  Fetch.prototype.runAsync = function () {
    var params = [];
    for (var _i = 0; _i < arguments.length; _i++) {
      params[_i] = arguments[_i];
    }
    return (0, _tslib.__awaiter)(this, void 0, void 0, function () {
      var currentCount, _a, _b, stopNow, _c, returnNow, state, servicePromise, res, error_1;
      var _d;
      var _e, _f, _g, _h, _j, _k, _l, _m, _o, _p;
      return (0, _tslib.__generator)(this, function (_q) {
        switch (_q.label) {
          case 0:
            this.count += 1;
            currentCount = this.count;
            _a = this.runPluginHandler('onBefore', params), _b = _a.stopNow, stopNow = _b === void 0 ? false : _b, _c = _a.returnNow, returnNow = _c === void 0 ? false : _c, state = (0, _tslib.__rest)(_a, ["stopNow", "returnNow"]);
            // stop request
            if (stopNow) {
              return [2 /*return*/, Promise.resolve(state.data)];
            }
            this.setState((0, _tslib.__assign)({
              loading: true,
              params: params
            }, state));
            // return now
            if (returnNow) {
              return [2 /*return*/, Promise.resolve(state.data)];
            }
            (_f = (_e = this.options).onBefore) === null || _f === void 0 ? void 0 : _f.call(_e, params);
            _q.label = 1;
          case 1:
            _q.trys.push([1, 3,, 4]);
            servicePromise = this.runPluginHandler('onRequest', this.serviceRef.current, params).servicePromise;
            if (!servicePromise) {
              servicePromise = (_d = this.serviceRef).current.apply(_d, (0, _tslib.__spreadArray)([], (0, _tslib.__read)(params), false));
            }
            return [4 /*yield*/, servicePromise];
          case 2:
            res = _q.sent();
            if (currentCount !== this.count) {
              // prevent run.then when request is canceled
              return [2 /*return*/, new Promise(function () {})];
            }
            // const formattedResult = this.options.formatResultRef.current ? this.options.formatResultRef.current(res) : res;
            this.setState({
              data: res,
              error: undefined,
              loading: false
            });
            (_h = (_g = this.options).onSuccess) === null || _h === void 0 ? void 0 : _h.call(_g, res, params);
            this.runPluginHandler('onSuccess', res, params);
            (_k = (_j = this.options).onFinally) === null || _k === void 0 ? void 0 : _k.call(_j, params, res, undefined);
            if (currentCount === this.count) {
              this.runPluginHandler('onFinally', params, res, undefined);
            }
            return [2 /*return*/, res];
          case 3:
            error_1 = _q.sent();
            if (currentCount !== this.count) {
              // prevent run.then when request is canceled
              return [2 /*return*/, new Promise(function () {})];
            }
            this.setState({
              error: error_1,
              loading: false
            });
            (_m = (_l = this.options).onError) === null || _m === void 0 ? void 0 : _m.call(_l, error_1, params);
            this.runPluginHandler('onError', error_1, params);
            (_p = (_o = this.options).onFinally) === null || _p === void 0 ? void 0 : _p.call(_o, params, undefined, error_1);
            if (currentCount === this.count) {
              this.runPluginHandler('onFinally', params, undefined, error_1);
            }
            throw error_1;
          case 4:
            return [2 /*return*/];
        }
      });
    });
  };
  Fetch.prototype.run = function () {
    var _this = this;
    var params = [];
    for (var _i = 0; _i < arguments.length; _i++) {
      params[_i] = arguments[_i];
    }
    this.runAsync.apply(this, (0, _tslib.__spreadArray)([], (0, _tslib.__read)(params), false))["catch"](function (error) {
      if (!_this.options.onError) {
        console.error(error);
      }
    });
  };
  Fetch.prototype.cancel = function () {
    this.count += 1;
    this.setState({
      loading: false
    });
    this.runPluginHandler('onCancel');
  };
  Fetch.prototype.refresh = function () {
    // @ts-ignore
    this.run.apply(this, (0, _tslib.__spreadArray)([], (0, _tslib.__read)(this.state.params || []), false));
  };
  Fetch.prototype.refreshAsync = function () {
    // @ts-ignore
    return this.runAsync.apply(this, (0, _tslib.__spreadArray)([], (0, _tslib.__read)(this.state.params || []), false));
  };
  Fetch.prototype.mutate = function (data) {
    var targetData = (0, _utils.isFunction)(data) ? data(this.state.data) : data;
    this.runPluginHandler('onMutate', targetData);
    this.setState({
      data: targetData
    });
  };
  return Fetch;
}();
var _default = exports["default"] = Fetch;