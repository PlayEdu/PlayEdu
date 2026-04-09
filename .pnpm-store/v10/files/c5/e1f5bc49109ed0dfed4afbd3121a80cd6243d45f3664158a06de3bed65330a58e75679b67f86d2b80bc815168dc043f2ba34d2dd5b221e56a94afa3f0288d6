import 'intersection-observer';
import type { BasicTarget } from '../utils/domTarget';
type CallbackType = (entry: IntersectionObserverEntry) => void;
export interface Options {
    rootMargin?: string;
    threshold?: number | number[];
    root?: BasicTarget<Element>;
    callback?: CallbackType;
}
declare function useInViewport(target: BasicTarget | BasicTarget[], options?: Options): readonly [boolean | undefined, number | undefined];
export default useInViewport;
