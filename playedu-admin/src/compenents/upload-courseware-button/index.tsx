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
import { generateUUID } from "../../utils";
import { minioMergeVideo, minioUploadId } from "../../api/upload";
import { UploadChunk } from "../../js/minio-upload-chunk";

interface PropsInterface {
  categoryIds: number[];
  onUpdate: () => void;
}

export const UploadCoursewareButton = (props: PropsInterface) => {
  const [showModal, setShowModal] = useState(false);
  const localFileList = useRef<FileItem[]>([]);
  const intervalId = useRef<number>();
  const [fileList, setFileList] = useState<FileItem[]>([]);

  const getMinioUploadId = async (extension: string) => {
    let resp: any = await minioUploadId(extension);
    return resp.data;
  };

  useEffect(() => {
    if (showModal) {
      intervalId.current = setInterval(() => {
        if (localFileList.current.length === 0) {
          return;
        }
        for (let i = 0; i < localFileList.current.length; i++) {
          if (localFileList.current[i].upload.status === 0) {
            localFileList.current[i].upload.handler.start();
            break;
          }
          if (localFileList.current[i].upload.status === 3) {
            break;
          }
        }
      }, 1000);
      console.log("定时器已创建", intervalId.current);
    } else {
      window.clearInterval(intervalId.current);
      console.log("定时器已销毁");
    }
  }, [showModal]);

  const uploadProps = {
    multiple: true,
    beforeUpload: async (file: File) => {
      if (file.size === 0) {
        message.error(`文件 ${file.name} 为空文件`);
        return Upload.LIST_IGNORE;
      }
      let extension: any = file.name.split(".");
      extension = extension[extension.length - 1];
      if (
        extension === "rar" ||
        file.type ===
          "application/vnd.openxmlformats-officedocument.wordprocessingml.document" ||
        file.type === "application/msword" ||
        file.type === "application/vnd.ms-word.document.macroEnabled.12" ||
        file.type === "application/vnd.ms-word.template.macroEnabled.12" ||
        file.type === "text/plain" ||
        file.type === "application/pdf" ||
        file.type === "application/x-zip-compressed" ||
        file.type === "application/octet-stream" ||
        file.type === "application/zip" ||
        file.type === "application/x-rar" ||
        file.type ===
          "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" ||
        file.type === "application/vnd.ms-excel" ||
        file.type ===
          "application/vnd.openxmlformats-officedocument.spreadsheetml.template" ||
        file.type === "application/vnd.ms-excel.sheet.macroEnabled.12" ||
        file.type === "application/vnd.ms-excel.template.macroEnabled.12" ||
        file.type === "application/vnd.ms-excel.addin.macroEnabled.12" ||
        file.type === "application/vnd.ms-excel.sheet.binary.macroEnabled.12" ||
        file.type === "application/vnd.ms-powerpoint" ||
        file.type ===
          "application/vnd.openxmlformats-officedocument.presentationml.presentation" ||
        file.type ===
          "application/vnd.openxmlformats-officedocument.presentationml.template" ||
        file.type ===
          "application/vnd.openxmlformats-officedocument.presentationml.slideshow" ||
        file.type === "application/vnd.ms-powerpoint.addin.macroEnabled.12" ||
        file.type ===
          "application/vnd.ms-powerpoint.presentation.macroEnabled.12" ||
        file.type === "application/vnd.ms-powerpoint.slideshow.macroEnabled.12"
      ) {
        // 添加到本地待上传
        let data = await getMinioUploadId(extension);
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
        };
        item.upload.handler.on("success", () => {
          minioMergeVideo(
            data["filename"],
            data["upload_id"],
            props.categoryIds.join(","),
            item.file.name,
            extension,
            item.file.size,
            0,
            ""
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
        // 先插入到ref
        localFileList.current.push(item);
        // 再更新list
        setFileList([...localFileList.current]);
      } else {
        message.error(`${file.name} 并不是可上传文件`);
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
    props.onUpdate();
    localFileList.current = [];
    setFileList([]);
    setShowModal(false);
  };

  return (
    <>
      <Button
        type="primary"
        onClick={() => {
          setShowModal(true);
        }}
      >
        上传课件
      </Button>

      {showModal ? (
        <Modal
          width={800}
          title="上传课件"
          open={true}
          onCancel={() => {
            closeWin();
          }}
          maskClosable={false}
          closable={false}
          onOk={() => {
            closeWin();
          }}
          okText="完成"
        >
          <Row gutter={[0, 10]}>
            <Col span={24}>
              <Dragger {...uploadProps}>
                <p className="ant-upload-drag-icon">
                  <InboxOutlined />
                </p>
                <p className="ant-upload-text">请将文件拖拽到此处上传</p>
                <p className="ant-upload-hint">
                  支持一次上传多个 /
                  支持word、excel、ppt、pdf、zip、rar、txt格式文件
                </p>
              </Dragger>
            </Col>
            <Col span={24}>
              <Table
                pagination={false}
                rowKey="id"
                columns={[
                  {
                    title: "课件",
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
                          <Tag color="red">{record.upload.remark}</Tag>
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
          </Row>
        </Modal>
      ) : null}
    </>
  );
};
