import moment from "moment";

export function getToken(): string {
  return window.localStorage.getItem("playedu-backend-token") || "";
}

export function setToken(token: string) {
  window.localStorage.setItem("playedu-backend-token", token);
}

export function clearToken() {
  window.localStorage.removeItem("playedu-backend-token");
}

export function dateFormat(dateStr: string) {
  if (!dateStr) {
    return "-";
  }
  return moment(dateStr).format("YYYY-MM-DD HH:mm");
}

export function timeFormat(dateStr: number) {
  if (!dateStr) {
    return "-";
  }
  var d = moment.duration(dateStr, "seconds");
  let value = d.hours() + "时" + d.minutes() + "分" + d.seconds() + "秒";

  if (d.hours() === 0) {
    value = d.minutes() + "分" + d.seconds() + "秒";
  } else {
    value = d.hours() + "时" + d.minutes() + "分" + d.seconds() + "秒";
  }

  return value;
}

export function generateUUID(): string {
  let guid = "";
  for (let i = 1; i <= 32; i++) {
    let n = Math.floor(Math.random() * 16.0).toString(16);
    guid += n;
    if (i === 8 || i === 12 || i === 16 || i === 20) guid += "-";
  }
  return guid;
}

export function transformBase64ToBlob(
  base64: string,
  mime: string,
  filename: string
): File {
  const arr = base64.split(",");
  const bstr = atob(arr[1]);
  let n = bstr.length;
  const u8arr = new Uint8Array(n);
  while (n--) {
    u8arr[n] = bstr.charCodeAt(n);
  }
  return new File([u8arr], filename, { type: mime });
}

export function parseVideo(file: File): Promise<VideoParseInfo> {
  return new Promise((resolve, reject) => {
    let video = document.createElement("video");
    video.muted = true;
    video.setAttribute("src", URL.createObjectURL(file));
    video.setAttribute("autoplay", "autoplay");
    video.setAttribute("crossOrigin", "anonymous"); //设置跨域 否则toDataURL导出图片失败
    video.setAttribute("width", "400"); //设置大小，如果不设置，下面的canvas就要按需设置
    video.setAttribute("height", "300");
    video.currentTime = 7; //视频时长，一定要设置，不然大概率白屏
    video.addEventListener("loadeddata", function () {
      let canvas = document.createElement("canvas"),
        width = video.width, //canvas的尺寸和图片一样
        height = video.height;
      canvas.width = width; //画布大小，默认为视频宽高
      canvas.height = height;
      let ctx = canvas.getContext("2d");
      if (!ctx) {
        return reject("无法捕获视频帧");
      }
      ctx.drawImage(video, 0, 0, width, height); //绘制canvas
      let dataURL = canvas.toDataURL("image/png"); //转换为base64
      video.remove();
      let info: VideoParseInfo = {
        poster: dataURL,
        duration: parseInt(video.duration + ""),
      };
      return resolve(info);
    });
  });
}

export function getHost() {
  return window.location.protocol + "//" + window.location.host + "/";
}

export function inStrArray(array: string[], value: string): boolean {
  for (let i = 0; i < array.length; i++) {
    if (array[i] === value) {
      return true;
    }
  }
  return false;
}

export function ValidataCredentials(value: any) {
  let regIdCard =
    /^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;
  if (regIdCard.test(value)) {
    if (value.length === 18) {
      return true;
    }
  }
}

export function checkUrl(value: any) {
  let url = value;
  let str = url.substr(url.length - 1, 1);
  if (str !== "/") {
    url = url + "/";
  }
  return url;
}

export function dateWholeFormat(dateStr: string) {
  if (!dateStr) {
    return "";
  }
  return moment(dateStr).format("YYYY-MM-DD HH:mm:ss");
}

export function transUtcTime(value: string) {
  const specifiedTime = value;
  // 创建一个新的Date对象，传入指定时间
  const specifiedDate = new Date(specifiedTime);
  //将指定时间转换为UTC+0时间
  const utcTime = specifiedDate.toISOString();

  return utcTime;
}
