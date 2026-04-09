type JsOptions = {
    type: 'js';
    js?: Partial<HTMLScriptElement>;
    keepWhenUnused?: boolean;
};
type CssOptions = {
    type: 'css';
    css?: Partial<HTMLStyleElement>;
    keepWhenUnused?: boolean;
};
type DefaultOptions = {
    type?: never;
    js?: Partial<HTMLScriptElement>;
    css?: Partial<HTMLStyleElement>;
    keepWhenUnused?: boolean;
};
export type Options = JsOptions | CssOptions | DefaultOptions;
export type Status = 'unset' | 'loading' | 'ready' | 'error';
declare const useExternal: (path?: string, options?: Options) => Status;
export default useExternal;
