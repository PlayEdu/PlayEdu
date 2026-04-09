import { useRef } from 'react';
import useEffectWithTarget from './useEffectWithTarget';
import { depsEqual } from './depsEqual';
var useDeepCompareEffectWithTarget = function (effect, deps, target) {
    var ref = useRef(undefined);
    var signalRef = useRef(0);
    if (!depsEqual(deps, ref.current)) {
        signalRef.current += 1;
    }
    ref.current = deps;
    useEffectWithTarget(effect, [signalRef.current], target);
};
export default useDeepCompareEffectWithTarget;
