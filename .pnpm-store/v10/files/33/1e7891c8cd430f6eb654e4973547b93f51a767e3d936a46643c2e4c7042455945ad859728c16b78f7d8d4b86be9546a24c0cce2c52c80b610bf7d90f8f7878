type Subscription<T> = (val: T) => void;
export declare class EventEmitter<T> {
    private subscriptions;
    emit: (val: T) => void;
    useSubscription: (callback: Subscription<T>) => void;
}
declare function useEventEmitter<T = void>(): EventEmitter<T>;
export default useEventEmitter;
