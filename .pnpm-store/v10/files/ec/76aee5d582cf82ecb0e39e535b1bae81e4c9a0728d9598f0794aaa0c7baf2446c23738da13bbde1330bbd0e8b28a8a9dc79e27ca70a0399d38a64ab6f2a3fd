import type { DebounceOptions } from '../useDebounce/debounceOptions';
type noop = (...args: any[]) => any;
declare function useDebounceFn<T extends noop>(fn: T, options?: DebounceOptions): {
    run: import("lodash").DebouncedFunc<(...args: Parameters<T>) => ReturnType<T>>;
    cancel: () => void;
    flush: () => ReturnType<T> | undefined;
};
export default useDebounceFn;
