import type { BasicTarget } from '../utils/domTarget';
type Position = {
    left: number;
    top: number;
};
export type Target = BasicTarget<Element | Document>;
export type ScrollListenController = (val: Position) => boolean;
declare function useScroll(target?: Target, shouldUpdate?: ScrollListenController): Position | undefined;
export default useScroll;
