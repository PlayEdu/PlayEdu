import moment from "moment";

export function getToken(): string {
  return window.localStorage.getItem("playedu-h5-token") || "";
}

export function setToken(token: string) {
  window.localStorage.setItem("playedu-h5-token", token);
}

export function clearToken() {
  window.localStorage.removeItem("playedu-h5-token");
}

export function dateFormat(dateStr: string) {
  return moment(dateStr).utcOffset(0).format("YYYY-MM-DD HH:mm");
}

export function getHost() {
  return window.location.protocol + "//" + window.location.host + "/";
}
export function getDepKey(): string {
  return window.localStorage.getItem("playedu-h5-depatmentKey") || "";
}

export function setDepKey(token: string) {
  window.localStorage.setItem("playedu-h5-depatmentKey", token);
}

export function clearDepKey() {
  window.localStorage.removeItem("playedu-h5-depatmentKey");
}
export function getDepName(): string {
  return window.localStorage.getItem("playedu-h5-depatmentName") || "";
}

export function setDepName(token: string) {
  window.localStorage.setItem("playedu-h5-depatmentName", token);
}

export function clearDepName() {
  window.localStorage.removeItem("playedu-frontend-depatmentName");
}

export function changeAppUrl(str: string) {
  let key = str.slice(str.length - 1);
  if (key === "/") {
    return str;
  } else {
    return str + "/";
  }
}

export function studyTimeFormat(dateStr: number) {
  var d = moment.duration(dateStr / 1000, "seconds");
  let value = [];
  value.push(d.hours());
  value.push(d.minutes());
  value.push(d.seconds());
  return value;
}

export function durationFormat(dateStr: number) {
  var d = moment.duration(dateStr, "seconds");
  let hour = d.hours() === 0 ? "" : d.hours() + ":";
  let minute = d.minutes() >= 10 ? d.minutes() + ":" : "0" + d.minutes() + ":";
  let second = d.seconds() >= 10 ? d.seconds() : "0" + d.seconds();

  return hour + minute + second;
}

export function isMobile() {
  let flag = window.navigator.userAgent.match(
    /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
  );
  return flag;
}

export function isEmptyObject(obj: Object) {
  return Object.keys(obj).length === 0;
}

export function isWechat() {
  let ua = window.navigator.userAgent.toLowerCase();
  return /micromessenger/.test(ua);
}

export function isIOS() {
  var u = navigator.userAgent;
  return !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
}
