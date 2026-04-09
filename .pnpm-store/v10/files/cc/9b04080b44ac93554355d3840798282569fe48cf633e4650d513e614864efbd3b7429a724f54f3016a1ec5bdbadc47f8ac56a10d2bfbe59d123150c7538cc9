import { useRef } from 'react';
import { depsEqual } from '../utils/depsEqual';
export var createDeepCompareEffect = function (hook) { return function (effect, deps) {
    var ref = useRef(undefined);
    var signalRef = useRef(0);
    if (deps === undefined || !depsEqual(deps, ref.current)) {
        signalRef.current += 1;
    }
    ref.current = deps;
    hook(effect, [signalRef.current]);
}; };
