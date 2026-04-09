import type { DependencyList } from 'react';
type Effect<T extends DependencyList> = (changes?: number[], previousDeps?: T, currentDeps?: T) => void | (() => void);
declare const useTrackedEffect: <T extends DependencyList>(effect: Effect<T>, deps?: [...T]) => void;
export default useTrackedEffect;
