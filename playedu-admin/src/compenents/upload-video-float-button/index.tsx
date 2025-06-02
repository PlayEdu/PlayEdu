import { InboxOutlined } from "@ant-design/icons";
import {
  Button,
  Col,
  message,
  Modal,
  Progress,
  Row,
  Table,
  Tag,
  Upload,
} from "antd";
import Dragger from "antd/es/upload/Dragger";
import { useEffect, useRef, useState } from "react";
import { generateUUID, parseVideo } from "../../utils";
import styles from "./index.module.less";
import { minioMergeVideo, minioUploadId } from "../../api/upload";
import { UploadChunk } from "../../js/minio-upload-chunk";
import { useDispatch } from "react-redux";
import { useSelector } from "react-redux";
import { uploadAction } from "../../store/user/loginUserSlice";
import upIcon from "../../assets/images/commen/upload.png";

export const UploadVideoFloatButton = () => {
  const dispatch = useDispatch();
  const [showModal, setShowModal] = useState(false);
  const localFileList = useRef<FileItem[]>([]);
  const intervalId = useRef<number>();
  const intervalId2 = useRef<number>();
  const [successNum, setSuccessNum] = useState(0);
  const [fileList, setFileList] = useState<FileItem[]>([]);
  const uploadStatus = useSelector(
    (state: any) => state.loginUser.value.uploadStatus
  );
  const categoryIds = useSelector(
    (state: any) => state.loginUser.value.uploadCateIds
  );

  const getMinioUploadId = async () => {
    let resp: any = await minioUploadId("mp4");
    return resp.data;
  };

  useEffect(() => {
    if (uploadStatus) {
      setShowModal(true);
      intervalId.current = setInterval(() => {
        let num = localFileList.current.filter(
          (it) => it.upload.status === 7
        ).length;
        setSuccessNum(num);
      }, 5000);
      let timeDiv = document.createElement("div");
      document.body.appendChild(timeDiv);
      intervalId2.current = setInterval(() => {
        timeDiv && timeDiv.click();
      }, 10000);
    } else {
      window.clearInterval(intervalId.current);
      window.clearInterval(intervalId2.current);
      console.log("定时器已销毁");
    }
  }, [uploadStatus]);

  const uploadProps = {
    multiple: true,
    beforeUpload: async (file: File, fileList: any) => {
      if (file.size > 10 * 1024 * 1024 * 1024) {
        message.error(`${file.name} 大小超过10G`);
        return Upload.LIST_IGNORE;
      }
      if (fileList.length > 10) {
        message.config({ maxCount: 1 });
        message.error("单次最多上传10个视频");
        return Upload.LIST_IGNORE;
      } else {
        message.config({ maxCount: 10 });
      }
      if (file.type === "video/mp4") {
        // 视频封面解析 || 视频时长解析
        let videoInfo = await parseVideo(file);
        // 添加到本地待上传
        let data = await getMinioUploadId();
        let run = new UploadChunk(file, data["upload_id"], data["filename"]);
        let item: FileItem = {
          id: generateUUID(),
          file: file,
          upload: {
            handler: run,
            progress: 0,
            status: 0,
            remark: "",
          },
          video: {
            duration: videoInfo.duration,
            poster: videoInfo.poster,
          },
        };
        item.upload.handler.on("success", () => {
          minioMergeVideo(
            data["filename"],
            data["upload_id"],
            categoryIds.join(","),
            item.file.name,
            "mp4",
            item.file.size,
            item.video?.duration || 0,
            item.video?.poster || ""
          ).then(() => {
            item.upload.progress = 100;
            item.upload.status = item.upload.handler.getUploadStatus();
            setFileList([...localFileList.current]);
          });
        });
        item.upload.handler.on("progress", (p: number) => {
          item.upload.status = item.upload.handler.getUploadStatus();
          item.upload.progress = p >= 100 ? 99 : p;
          setFileList([...localFileList.current]);
        });
        item.upload.handler.on("error", (msg: string) => {
          item.upload.status = item.upload.handler.getUploadStatus();
          item.upload.remark = msg;
          setFileList([...localFileList.current]);
        });
        item.upload.handler.start();
        // 先插入到ref
        localFileList.current.push(item);
        // 再更新list
        setFileList([...localFileList.current]);
      } else {
        message.error(`${file.name} 并不是 mp4 视频文件`);
      }
      return Upload.LIST_IGNORE;
    },
  };

  const closeWin = () => {
    if (fileList.length > 0) {
      fileList.forEach((item) => {
        if (item.upload.status !== 5 && item.upload.status !== 7) {
          item.upload.handler.cancel();
        }
      });
    }
    localFileList.current = [];
    setFileList([]);
    setSuccessNum(0);
    setShowModal(false);
    dispatch(
      uploadAction({
        uploadStatus: false,
        uploadCateIds: [],
      })
    );
  };

  return (
    <>
      {uploadStatus ? (
        <>
          <div
            style={{ display: showModal ? "none" : "flex" }}
            className={styles["float-button"]}
            onClick={() => setShowModal(true)}
          >
            <img src={upIcon} />
            <span>
              视频上传成功 ({successNum}/{fileList.length})
            </span>
          </div>
          <Modal
            width={800}
            title="上传视频"
            open={showModal}
            maskClosable={false}
            footer={null}
            onCancel={() => {
              closeWin();
            }}
          >
            <Row gutter={[0, 10]}>
              <Col span={24}>
                <Dragger {...uploadProps}>
                  <p className="ant-upload-drag-icon">
                    <InboxOutlined />
                  </p>
                  <p className="ant-upload-text">请将视频拖拽到此处上传</p>
                  <p className="ant-upload-hint">
                    支持一次上传多个 / 支持10G以内的mp4文件
                  </p>
                </Dragger>
              </Col>
              <Col span={24}>
                <Table
                  pagination={false}
                  rowKey="id"
                  columns={[
                    {
                      title: "视频",
                      dataIndex: "name",
                      key: "name",
                      render: (_, record) => <span>{record.file.name}</span>,
                    },
                    {
                      title: "大小",
                      dataIndex: "size",
                      key: "size",
                      render: (_, record) => (
                        <span>
                          {(record.file.size / 1024 / 1024).toFixed(2)}M
                        </span>
                      ),
                    },
                    {
                      title: "进度",
                      dataIndex: "progress",
                      key: "progress",
                      render: (_, record: FileItem) => (
                        <>
                          {record.upload.status === 0 ? (
                            "等待上传"
                          ) : (
                            <Progress
                              size="small"
                              steps={20}
                              percent={record.upload.progress}
                            />
                          )}
                        </>
                      ),
                    },
                    {
                      title: "操作",
                      key: "action",
                      render: (_, record) => (
                        <>
                          {record.upload.status === 5 ? (
                            <>
                              <Button
                                type="link"
                                size="small"
                                className="b-n-link c-red"
                                onClick={() => {
                                  record.upload.handler.retry();
                                }}
                              >
                                失败.重试
                              </Button>
                            </>
                          ) : null}

                          {record.upload.status === 7 ? (
                            <Tag color="success">上传成功</Tag>
                          ) : null}
                        </>
                      ),
                    },
                  ]}
                  dataSource={fileList}
                />
              </Col>
              <Col span={24}>
                <div className="r-r-flex">
                  <Button
                    type="primary"
                    onClick={() => {
                      closeWin();
                    }}
                  >
                    完成
                  </Button>
                  <Button
                    type="default"
                    className="mr-16"
                    onClick={() => {
                      setShowModal(false);
                    }}
                  >
                    最小化
                  </Button>
                </div>
              </Col>
            </Row>
          </Modal>
        </>
      ) : null}
    </>
  );
};
