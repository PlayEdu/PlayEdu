import React, { useState, useEffect } from "react";
import { Modal, Form, TreeSelect, Input, message, Spin } from "antd";
import styles from "./update.module.less";
import { user, department } from "../../../api/index";
import { UploadImageButton } from "../../../compenents";
import { ValidataCredentials } from "../../../utils/index";
import memberDefaultAvatar from "../../../assets/thumb/avatar.png";

interface PropInterface {
  id: number;
  open: boolean;
  onCancel: () => void;
}

interface Option {
  value: string | number;
  label: string;
  children?: Option[];
}

export const MemberUpdate: React.FC<PropInterface> = ({
  id,
  open,
  onCancel,
}) => {
  const [form] = Form.useForm();
  const [init, setInit] = useState(true);
  const [loading, setLoading] = useState(false);
  const [departments, setDepartments] = useState<any>([]);
  const [resourceUrl, setResourceUrl] = useState<ResourceUrlModel>({});
  const [avatar, setAvatar] = useState<string>(memberDefaultAvatar);

  useEffect(() => {
    setInit(true);
    if (id == 0) {
      return;
    }
    if (open) {
      getParams();
      form.setFieldsValue({
        password: "",
      });
      getDetail();
    }
  }, [form, id, open]);

  const getParams = () => {
    if (id === 0) {
      return;
    }
    department.departmentList({}).then((res: any) => {
      const departments = res.data.departments;
      if (JSON.stringify(departments) !== "{}") {
        const new_arr: Option[] = checkArr(departments, 0);
        setDepartments(new_arr);
      }
    });
  };

  const getDetail = () => {
    user.user(id).then((res: any) => {
      let user = res.data.user;
      setResourceUrl(res.data.resource_url);
      form.setFieldsValue({
        email: user.email,
        name: user.name,
        avatar: user.avatar,
        idCard: user.id_card,
        dep_ids: res.data.dep_ids,
      });
      if (user.avatar === -1) {
        setAvatar(memberDefaultAvatar);
      } else {
        setAvatar(res.data.resource_url[user.avatar]);
      }
      setInit(false);
    });
  };

  const checkChild = (departments: any[], id: number) => {
    for (let key in departments) {
      for (let i = 0; i < departments[key].length; i++) {
        if (departments[key][i].id === id) {
          return departments[key][i];
        }
      }
    }
  };

  const checkArr = (departments: any[], id: number) => {
    const arr = [];
    for (let i = 0; i < departments[id].length; i++) {
      if (!departments[departments[id][i].id]) {
        arr.push({
          label: departments[id][i].name,
          value: departments[id][i].id,
        });
      } else {
        const new_arr: Option[] = checkArr(departments, departments[id][i].id);
        arr.push({
          label: departments[id][i].name,
          value: departments[id][i].id,
          children: new_arr,
        });
      }
    }
    return arr;
  };

  const onFinish = (values: any) => {
    if (loading) {
      return;
    }
    // if (values.idCard !== "" && !ValidataCredentials(values.idCard)) {
    //   message.error("请输入正确的身份证号！");
    //   return;
    // }
    setLoading(true);
    user
      .updateUser(
        id,
        values.email,
        values.name,
        values.avatar,
        values.password || "",
        values.idCard,
        values.dep_ids
      )
      .then((res: any) => {
        setLoading(false);
        message.success("保存成功！");
        onCancel();
      })
      .catch((e) => {
        setLoading(false);
      });
  };

  const onFinishFailed = (errorInfo: any) => {
    console.log("Failed:", errorInfo);
  };

  const onChange = (value: any) => {};

  return (
    <>
      {open ? (
        <Modal
          title="编辑学员"
          centered
          forceRender
          open={true}
          width={484}
          onOk={() => form.submit()}
          onCancel={() => onCancel()}
          maskClosable={false}
          okButtonProps={{ loading: loading }}
        >
          {init && (
            <div className="float-left text-center mt-30">
              <Spin></Spin>
            </div>
          )}
          <div
            className="float-left mt-24"
            style={{ display: init ? "none" : "block" }}
          >
            <Form
              form={form}
              name="update-basic"
              labelCol={{ span: 7 }}
              wrapperCol={{ span: 17 }}
              initialValues={{ remember: true }}
              onFinish={onFinish}
              onFinishFailed={onFinishFailed}
              autoComplete="off"
            >
              <Form.Item
                label="学员头像"
                labelCol={{ style: { marginTop: 15, marginLeft: 46 } }}
                name="avatar"
                rules={[{ required: true, message: "请上传学员头像!" }]}
              >
                <div className="d-flex">
                  {avatar && (
                    <img className="form-avatar mr-16" src={avatar} alt="" />
                  )}
                  <div className="d-flex">
                    <UploadImageButton
                      text="更换头像"
                      onSelected={(url, id) => {
                        setAvatar(url);
                        form.setFieldsValue({ avatar: id });
                      }}
                    ></UploadImageButton>
                  </div>
                </div>
              </Form.Item>
              <Form.Item
                label="学员姓名"
                name="name"
                rules={[{ required: true, message: "请输入学员姓名!" }]}
              >
                <Input
                  allowClear
                  style={{ width: 274 }}
                  placeholder="请填写学员姓名"
                />
              </Form.Item>
              <Form.Item
                label="登录邮箱"
                name="email"
                rules={[{ required: true, message: "请输入登录邮箱!" }]}
              >
                <Input
                  autoComplete="off"
                  style={{ width: 274 }}
                  allowClear
                  placeholder="请输入学员登录邮箱"
                />
              </Form.Item>
              <Form.Item label="登录密码" name="password">
                <Input.Password
                  autoComplete="off"
                  style={{ width: 274 }}
                  allowClear
                  placeholder="请输入登录密码"
                />
              </Form.Item>
              <Form.Item
                label="所属部门"
                name="dep_ids"
                rules={[{ required: true, message: "请选择学员所属部门!" }]}
              >
                <TreeSelect
                  showCheckedStrategy={TreeSelect.SHOW_ALL}
                  style={{ width: 274 }}
                  treeData={departments}
                  multiple
                  allowClear
                  treeDefaultExpandAll
                  placeholder="请选择学员所属部门"
                />
              </Form.Item>
            </Form>
          </div>
        </Modal>
      ) : null}
    </>
  );
};
