import type { BasicTarget } from '../utils/domTarget';
export type KeyType = number | string;
export type KeyPredicate = (event: KeyboardEvent) => KeyType | boolean | undefined;
export type KeyFilter = KeyType | KeyType[] | ((event: KeyboardEvent) => boolean);
export type KeyEvent = 'keydown' | 'keyup';
export type Target = BasicTarget<HTMLElement | Document | Window>;
export type Options = {
    events?: KeyEvent[];
    target?: Target;
    exactMatch?: boolean;
    useCapture?: boolean;
};
declare function useKeyPress(keyFilter: KeyFilter, eventHandler: (event: KeyboardEvent, key: KeyType) => void, option?: Options): void;
export default useKeyPress;
