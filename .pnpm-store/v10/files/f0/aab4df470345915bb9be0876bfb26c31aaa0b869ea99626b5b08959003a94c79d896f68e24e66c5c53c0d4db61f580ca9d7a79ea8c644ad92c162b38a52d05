type noop = (this: any, ...args: any[]) => any;
type PickFunction<T extends noop> = (this: ThisParameterType<T>, ...args: Parameters<T>) => ReturnType<T>;
declare const useMemoizedFn: <T extends noop>(fn: T) => PickFunction<T>;
export default useMemoizedFn;
