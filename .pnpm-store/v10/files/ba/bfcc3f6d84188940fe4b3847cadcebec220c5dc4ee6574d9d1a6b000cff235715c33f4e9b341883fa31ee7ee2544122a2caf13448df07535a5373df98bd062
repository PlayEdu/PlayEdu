import type { BasicTarget } from '../utils/domTarget';
type ItemHeight<T> = (index: number, data: T) => number;
export interface Options<T> {
    containerTarget: BasicTarget;
    wrapperTarget: BasicTarget;
    itemHeight: number | ItemHeight<T>;
    overscan?: number;
}
declare const useVirtualList: <T = any>(list: T[], options: Options<T>) => readonly [{
    index: number;
    data: T;
}[], (this: unknown, index: number) => void];
export default useVirtualList;
