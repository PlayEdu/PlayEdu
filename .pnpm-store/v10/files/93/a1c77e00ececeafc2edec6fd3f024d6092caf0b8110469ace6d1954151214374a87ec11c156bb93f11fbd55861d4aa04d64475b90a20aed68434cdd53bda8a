import type { ThrottleOptions } from '../useThrottle/throttleOptions';
type noop = (...args: any[]) => any;
declare function useThrottleFn<T extends noop>(fn: T, options?: ThrottleOptions): {
    run: import("lodash").DebouncedFuncLeading<(...args: Parameters<T>) => ReturnType<T>>;
    cancel: () => void;
    flush: () => ReturnType<T>;
};
export default useThrottleFn;
