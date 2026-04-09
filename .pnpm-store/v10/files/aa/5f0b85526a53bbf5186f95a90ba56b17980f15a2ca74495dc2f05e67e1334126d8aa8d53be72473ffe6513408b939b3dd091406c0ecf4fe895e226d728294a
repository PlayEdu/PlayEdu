import type { BasicTarget } from '../utils/domTarget';
type EventType = MouseEvent | TouchEvent;
export interface Options {
    delay?: number;
    moveThreshold?: {
        x?: number;
        y?: number;
    };
    onClick?: (event: EventType) => void;
    onLongPressEnd?: (event: EventType) => void;
}
declare function useLongPress(onLongPress: (event: EventType) => void, target: BasicTarget, { delay, moveThreshold, onClick, onLongPressEnd }?: Options): void;
export default useLongPress;
