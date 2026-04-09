import type { RefObject } from 'react';
type TargetValue<T> = T | undefined | null;
type TargetType = HTMLElement | Element | Window | Document;
export type BasicTarget<T extends TargetType = Element> = (() => TargetValue<T>) | TargetValue<T> | RefObject<TargetValue<T>>;
export declare function getTargetElement<T extends TargetType>(target: BasicTarget<T>, defaultElement?: T): TargetValue<T>;
export {};
