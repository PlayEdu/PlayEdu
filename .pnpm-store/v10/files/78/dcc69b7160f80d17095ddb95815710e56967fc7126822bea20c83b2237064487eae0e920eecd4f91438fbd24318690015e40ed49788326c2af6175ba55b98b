import type { BasicTarget } from '../utils/domTarget';
export interface PageFullscreenOptions {
    className?: string;
    zIndex?: number;
}
export interface Options {
    onExit?: () => void;
    onEnter?: () => void;
    pageFullscreen?: boolean | PageFullscreenOptions;
}
declare const useFullscreen: (target: BasicTarget, options?: Options) => readonly [boolean, {
    readonly enterFullscreen: (this: unknown) => void;
    readonly exitFullscreen: (this: unknown) => void;
    readonly toggleFullscreen: (this: unknown) => void;
    readonly isEnabled: true;
}];
export default useFullscreen;
