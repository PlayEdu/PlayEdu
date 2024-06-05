import axios, { Axios } from "axios";
import { minioPreSignUrl } from "../api/upload";

export class UploadChunk {
  client: Axios;
  file: File;
  progress: number;
  chunkNumber: number;
  isStop: boolean;
  chunkSize: number;
  chunkIndex: number;
  uploadId: string;
  filename: string;
  // 上传状态[0:等待上传,3:上传中,5:上传失败,7:上传成功]
  uploadStatus: number;
  uploadRemark: string;

  onError?: (err: string) => void | undefined;
  onSuccess?: () => void | undefined;
  onRetry?: () => void | undefined;
  onProgress?: (progress: number) => void;

  constructor(file: File, uploadId: string, filename: string) {
    this.client = axios.create({
      timeout: 60000,
      withCredentials: false,
    });
    this.file = file;
    this.progress = 0;
    this.isStop = false;
    this.chunkIndex = 1;
    this.chunkSize = 5 * 1024 * 1024; //分块大小-5mb
    this.chunkNumber = Math.ceil(file.size / this.chunkSize);

    this.uploadId = uploadId;
    this.filename = filename;

    this.uploadStatus = 0;
    this.uploadRemark = "";
  }

  on(event: string, handle: any) {
    if (event === "error") {
      this.onError = handle;
    } else if (event === "success") {
      this.onSuccess = handle;
    } else if (event === "progress") {
      this.onProgress = handle;
    } else if (event === "retry") {
      this.onRetry = handle;
    }
  }

  start() {
    if (this.isStop) {
      return;
    }

    // 检测是否上传完成
    if (this.chunkIndex > this.chunkNumber) {
      this.uploadCompleted();
      return;
    }

    // 进度更新
    this.uploadProgressUpdated();

    let start = (this.chunkIndex - 1) * this.chunkSize;
    const chunkData = this.file.slice(start, start + this.chunkSize);
    const boolname = this.file.name + "-" + this.chunkIndex;
    const tmpFile = new File([chunkData], boolname);

    // 首先获取上传minio的签名
    minioPreSignUrl(this.uploadId, this.filename, this.chunkIndex)
      .then((res: any) => {
        // 拿到签名之后将分块内容上传到minio
        return this.client.put(res.data.url, tmpFile, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        });
      })
      .then(() => {
        this.chunkIndex += 1;
        this.start();
      })
      .catch((e: any) => {
        this.uploadedFail(e);
      });
  }

  isOver() {
    return this.uploadStatus === 5 || this.uploadStatus === 7;
  }

  cancel() {
    this.isStop = true;
    this.onError && this.onError("已取消");
  }

  retry() {
    this.isStop = false;
    this.uploadStatus = 0;
    this.start();
    this.onRetry && this.onRetry();
  }

  uploadProgressUpdated() {
    if (this.uploadStatus === 0) {
      this.uploadStatus = 3;
    }
    this.onProgress &&
      this.onProgress(
        parseInt((this.chunkIndex / this.chunkNumber) * 100 + "")
      );
  }

  uploadCompleted() {
    this.uploadStatus = 7;
    this.onSuccess && this.onSuccess();
  }

  uploadedFail(e: any) {
    console.log("上传失败,错误信息:", e);
    this.uploadStatus = 5;
    this.onError && this.onError("失败.2");
  }

  getUploadStatus(): number {
    return this.uploadStatus;
  }

  getUploadProgress(): number {
    if (this.chunkNumber === 0) {
      return 0;
    }
    return (this.chunkIndex / this.chunkNumber) * 100;
  }

  getUploadRemark(): string {
    return this.uploadRemark;
  }
}
