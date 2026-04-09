export declare const SYNC_STORAGE_EVENT_NAME = "AHOOKS_SYNC_STORAGE_EVENT_NAME";
export type SetState<S> = S | ((prevState?: S) => S);
export interface Options<T> {
    defaultValue?: T | (() => T);
    listenStorageChange?: boolean;
    serializer?: (value: T) => string;
    deserializer?: (value: string) => T;
    onError?: (error: unknown) => void;
}
export declare const createUseStorageState: (getStorage: () => Storage | undefined) => <T>(key: string, options?: Options<T>) => readonly [T, (this: unknown, value: SetState<T>) => void];
