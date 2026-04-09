import type { SetStateAction } from 'react';
export interface Options<T> {
    defaultValue?: T;
    defaultValuePropName?: string;
    valuePropName?: string;
    trigger?: string;
}
export type Props = Record<string, any>;
export interface StandardProps<T> {
    value: T;
    defaultValue?: T;
    onChange: (val: T) => void;
}
declare function useControllableValue<T = any>(props: StandardProps<T>): [T, (v: SetStateAction<T>) => void];
declare function useControllableValue<T = any>(props?: Props, options?: Options<T>): [T, (v: SetStateAction<T>, ...args: any[]) => void];
export default useControllableValue;
