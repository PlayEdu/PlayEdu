import React, { useState, useEffect } from "react";
import { Button, Drawer, Form, Modal, message, Spin } from "antd";
import styles from "./hour-update.module.less";
import { course, courseAttachment } from "../../../api/index";
import { SelectAttachment } from "../../../compenents";
import { ExclamationCircleFilled } from "@ant-design/icons";
import { TreeAttachments } from "./attachments";

const { confirm } = Modal;

interface PropInterface {
  id: number;
  open: boolean;
  onCancel: () => void;
}

export const CourseAttachmentUpdate: React.FC<PropInterface> = ({
  id,
  open,
  onCancel,
}) => {
  const [form] = Form.useForm();
  const [init, setInit] = useState(true);
  const [attachmentVisible, setAttachmentVisible] = useState<boolean>(false);
  const [attachmentData, setAttachmentData] = useState<AttachmentDataModel[]>(
    []
  );
  const [attachments, setAttachments] = useState<number[]>([]);

  useEffect(() => {
    setInit(true);
    if (id === 0) {
      return;
    }
    setAttachmentVisible(false);
    setAttachmentData([]);
    setAttachments([]);
    getDetail();
  }, [id, open]);

  const getDetail = () => {
    course.course(id).then((res: any) => {
      let treeData = res.data.attachments;
      if (treeData.length > 0) {
        const arr: any = resetAttachments(treeData).arr;
        const keys: any = resetAttachments(treeData).keys;
        setAttachmentData(arr);
        setAttachments(keys);
      }
      setInit(false);
    });
  };

  const resetAttachments = (data: any) => {
    const arr: any = [];
    const keys: any = [];
    if (data) {
      for (let i = 0; i < data.length; i++) {
        arr.push({
          type: data[i].type,
          name: data[i].title,
          rid: data[i].rid,
          id: data[i].id,
        });
        keys.push(data[i].rid);
      }
    }
    return { arr, keys };
  };

  const onFinish = (values: any) => {};

  const onFinishFailed = (errorInfo: any) => {
    console.log("Failed:", errorInfo);
  };

  const selectAttachmentData = (arr: any, videos: any) => {
    const hours: any = [];
    for (let i = 0; i < videos.length; i++) {
      hours.push({
        sort: attachmentData.length + i,
        title: videos[i].name,
        type: videos[i].type,
        rid: videos[i].rid,
      });
    }
    if (hours.length === 0) {
      message.error("请选择课件");
      return;
    }

    courseAttachment.storeCourseAttachmentMulti(id, hours).then((res: any) => {
      console.log("ok");
      setAttachmentVisible(false);
      getDetail();
    });
  };

  const delAttachments = (hid: number) => {
    const data = [...attachmentData];
    confirm({
      title: "操作确认",
      icon: <ExclamationCircleFilled />,
      content: "确认删除此课件？",
      centered: true,
      okText: "确认",
      cancelText: "取消",
      onOk() {
        const index = data.findIndex((i: any) => i.rid === hid);
        let delId = data[index].id;
        if (index >= 0) {
          data.splice(index, 1);
        }
        if (data.length > 0) {
          setAttachmentData(data);
          const keys = data.map((item: any) => item.rid);
          setAttachments(keys);
        } else {
          setAttachmentData([]);
          setAttachments([]);
        }
        if (delId) {
          courseAttachment.destroyAttachment(id, delId).then((res: any) => {
            console.log("ok");
          });
        }
      },
      onCancel() {
        console.log("Cancel");
      },
    });
  };

  const transAttachments = (arr: any) => {
    setAttachments(arr);
    const data = [...attachmentData];
    const newArr: any = [];
    const hourIds: any = [];
    for (let i = 0; i < arr.length; i++) {
      data.map((item: any) => {
        if (item.rid === arr[i]) {
          newArr.push(item);
          hourIds.push(item.id);
        }
      });
    }
    setAttachmentData(newArr);
    courseAttachment.transCourseAttachment(id, hourIds).then((res: any) => {
      console.log("ok");
    });
  };

  return (
    <>
      {open ? (
        <Drawer
          title="课件管理"
          onClose={onCancel}
          maskClosable={false}
          open={true}
          width={634}
        >
          <div className={styles["top-content"]}>
            <p>1.线上课课件调整及时生效，操作不可逆，请谨慎操作。</p>
          </div>
          <div className="float-left mt-24">
            <SelectAttachment
              defaultKeys={attachments}
              open={attachmentVisible}
              onCancel={() => {
                setAttachmentVisible(false);
              }}
              onSelected={(arr: any, videos: any) => {
                selectAttachmentData(arr, videos);
              }}
            ></SelectAttachment>
            {init && (
              <div className="float-left text-center mt-30">
                <Spin></Spin>
              </div>
            )}
            <div
              className="float-left"
              style={{ display: init ? "none" : "block" }}
            >
              <Form
                form={form}
                name="attachment-update-basic"
                labelCol={{ span: 5 }}
                wrapperCol={{ span: 19 }}
                initialValues={{ remember: true }}
                onFinish={onFinish}
                onFinishFailed={onFinishFailed}
                autoComplete="off"
              >
                <div className="c-flex">
                  <Form.Item>
                    <div className="ml-42">
                      <Button
                        onClick={() => setAttachmentVisible(true)}
                        type="primary"
                      >
                        添加课件
                      </Button>
                    </div>
                  </Form.Item>
                  <div className={styles["hous-box"]}>
                    {attachmentData.length === 0 && (
                      <span className={styles["no-hours"]}>
                        请点击上方按钮添加课件
                      </span>
                    )}
                    {attachmentData.length > 0 && (
                      <TreeAttachments
                        data={attachmentData}
                        onRemoveItem={(id: number) => {
                          delAttachments(id);
                        }}
                        onUpdate={(arr: any[]) => {
                          transAttachments(arr);
                        }}
                      />
                    )}
                  </div>
                </div>
              </Form>
            </div>
          </div>
        </Drawer>
      ) : null}
    </>
  );
};
