import React, { useState, useEffect } from "react";
import { Modal, Image, Form, message, Upload, Button } from "antd";
import styles from "./index.module.less";
import { user } from "../../api/index";
import { useDispatch } from "react-redux";
import { loginAction } from "../../store/user/loginUserSlice";
import type { UploadProps } from "antd";
import config from "../../js/config";
import { getToken, changeAppUrl } from "../../utils/index";

interface PropInterface {
  open: boolean;
  onCancel: () => void;
}

export const UserInfoModel: React.FC<PropInterface> = ({ open, onCancel }) => {
  const dispatch = useDispatch();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState<boolean>(true);
  const [avatar, setAvatar] = useState<string>("");
  const [name, setName] = useState<string>("");
  const [idCard, setIdCard] = useState<string>("");

  useEffect(() => {
    if (open) {
      getUser();
    }
  }, [form, open]);

  const getUser = () => {
    user.detail().then((res: any) => {
      setAvatar(res.data.user.avatar);
      setName(res.data.user.name);
      setIdCard(res.data.user.id_card);
      dispatch(loginAction(res.data));
    });
  };

  const props: UploadProps = {
    name: "file",
    multiple: false,
    method: "PUT",
    action: changeAppUrl(config.app_url) + "api/v1/user/avatar",
    headers: {
      Accept: "application/json",
      authorization: "Bearer " + getToken(),
    },
    beforeUpload: (file) => {
      const isPNG =
        file.type === "image/png" ||
        file.type === "image/jpg" ||
        file.type === "image/jpeg";
      if (!isPNG) {
        message.error(`${file.name}不是图片文件`);
      }
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        message.error("超过2M限制，不允许上传");
      }
      return (isPNG && isLt2M) || Upload.LIST_IGNORE;
    },
    onChange(info: any) {
      const { status, response } = info.file;
      if (status === "done") {
        if (response.code === 0) {
          getUser();
        } else {
          message.error(response.msg);
        }
      } else if (status === "error") {
        message.error(`${info.file.name} 上传失败`);
      }
    },
  };

  return (
    <>
      {open ? (
        <Modal
          title="个人信息"
          centered
          forceRender
          open={true}
          width={416}
          onCancel={() => onCancel()}
          maskClosable={false}
          footer={null}
        >
          <div className="mt-24">
            <Form
              form={form}
              name="user-info"
              labelCol={{ span: 8 }}
              wrapperCol={{ span: 16 }}
              initialValues={{ remember: true }}
              autoComplete="off"
            >
              <Form.Item
                label="学员头像"
                labelCol={{ style: { marginTop: 15, marginLeft: 52 } }}
              >
                <div className="d-flex">
                  {avatar && (
                    <Image
                      loading="lazy"
                      width={60}
                      height={60}
                      style={{ borderRadius: "50%" }}
                      src={avatar}
                      preview={false}
                    />
                  )}
                  <div className="d-flex ml-16">
                    <Upload {...props} showUploadList={false}>
                      <Button>更换头像</Button>
                    </Upload>
                  </div>
                </div>
              </Form.Item>
              {name && (
                <Form.Item label="学员姓名">
                  <div>{name}</div>
                </Form.Item>
              )}
              {idCard && (
                <Form.Item label="身份证号" style={{ marginBottom: 16 }}>
                  <div>{idCard}</div>
                </Form.Item>
              )}
            </Form>
          </div>
        </Modal>
      ) : null}
    </>
  );
};
