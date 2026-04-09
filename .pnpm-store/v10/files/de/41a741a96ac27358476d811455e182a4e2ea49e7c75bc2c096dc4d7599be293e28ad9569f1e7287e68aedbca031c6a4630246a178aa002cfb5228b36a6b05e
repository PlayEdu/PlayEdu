import Cookies from 'js-cookie';
export type State = string | undefined;
export interface Options extends Cookies.CookieAttributes {
    defaultValue?: State | (() => State);
}
declare function useCookieState(cookieKey: string, options?: Options): readonly [State, (this: unknown, newValue: State | ((prevState: State) => State), newOptions?: Cookies.CookieAttributes | undefined) => void];
export default useCookieState;
