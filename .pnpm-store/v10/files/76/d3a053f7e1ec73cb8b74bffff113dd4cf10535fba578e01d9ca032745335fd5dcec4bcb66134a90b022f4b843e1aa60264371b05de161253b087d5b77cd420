import dayjs from 'dayjs';
export type TDate = dayjs.ConfigType;
export interface Options {
    leftTime?: number;
    targetDate?: TDate;
    interval?: number;
    onEnd?: () => void;
}
export interface FormattedRes {
    days: number;
    hours: number;
    minutes: number;
    seconds: number;
    milliseconds: number;
}
declare const useCountdown: (options?: Options) => readonly [number, FormattedRes];
export default useCountdown;
