import React, { useState, useEffect } from "react";
import { Modal, Form, Input, message } from "antd";
import styles from "./index.module.less";
import { user } from "../../api/index";

interface PropInterface {
  open: boolean;
  onCancel: () => void;
}

export const ChangePasswordModel: React.FC<PropInterface> = ({
  open,
  onCancel,
}) => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    form.setFieldsValue({
      old_password: "",
      new_password: "",
      again_new_password: "",
    });
  }, [form, open]);

  const onFinish = (values: any) => {
    if (values.again_new_password !== values.new_password) {
      message.error("再次输入的新密码错误");
      return;
    }
    user.password(values.old_password, values.new_password).then((res: any) => {
      message.success("保存成功！");
      onCancel();
    });
  };

  const onFinishFailed = (errorInfo: any) => {
    console.log("Failed:", errorInfo);
  };

  return (
    <>
      {open ? (
        <Modal
          title="修改密码"
          centered
          forceRender
          open={true}
          width={416}
          onOk={() => form.submit()}
          onCancel={() => onCancel()}
          maskClosable={false}
        >
          <div className="float-left mt-24">
            <Form
              form={form}
              name="change-password"
              labelCol={{ span: 8 }}
              wrapperCol={{ span: 16 }}
              initialValues={{ remember: true }}
              onFinish={onFinish}
              onFinishFailed={onFinishFailed}
              autoComplete="off"
            >
              <Form.Item
                label="请输入原密码"
                name="old_password"
                rules={[{ required: true, message: "请输入原密码!" }]}
              >
                <Input.Password
                  style={{ width: 200 }}
                  autoComplete="off"
                  placeholder="请输入原密码"
                />
              </Form.Item>
              <Form.Item
                label="输入新密码"
                name="new_password"
                rules={[{ required: true, message: "请输入新密码!" }]}
              >
                <Input.Password
                  style={{ width: 200 }}
                  autoComplete="off"
                  placeholder="请输入新密码"
                />
              </Form.Item>
              <Form.Item
                label="再次输入新密码"
                name="again_new_password"
                rules={[{ required: true, message: "再次输入新密码!" }]}
              >
                <Input.Password
                  style={{ width: 200 }}
                  autoComplete="off"
                  placeholder="再次输入新密码"
                />
              </Form.Item>
            </Form>
          </div>
        </Modal>
      ) : null}
    </>
  );
};
