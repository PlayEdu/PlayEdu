import React, { useState, useEffect } from "react";
import { Modal, Form, TreeSelect, Input, message, Spin } from "antd";
import styles from "./create.module.less";
import { useSelector } from "react-redux";
import { user, department } from "../../../api/index";
import { UploadImageButton } from "../../../compenents";
import { ValidataCredentials } from "../../../utils/index";

interface PropInterface {
  open: boolean;
  depIds: any;
  onCancel: () => void;
}

interface Option {
  value: string | number;
  label: string;
  children?: Option[];
}

export const MemberCreate: React.FC<PropInterface> = ({
  open,
  depIds,
  onCancel,
}) => {
  const [form] = Form.useForm();
  const [init, setInit] = useState(true);
  const [loading, setLoading] = useState(false);
  const [departments, setDepartments] = useState<any>([]);
  const memberDefaultAvatar = useSelector(
    (state: any) => state.systemConfig.value.memberDefaultAvatar
  );
  const [avatar, setAvatar] = useState<string>(memberDefaultAvatar);

  useEffect(() => {
    setInit(true);
    if (open) {
      getParams();
    }
  }, [open]);

  useEffect(() => {
    form.setFieldsValue({
      email: "",
      name: "",
      password: "",
      avatar: memberDefaultAvatar,
      idCard: "",
      dep_ids: depIds,
    });
    setAvatar(memberDefaultAvatar);
  }, [form, open, depIds]);

  const getParams = () => {
    department.departmentList({}).then((res: any) => {
      const departments = res.data.departments;
      if (JSON.stringify(departments) !== "{}") {
        const new_arr: Option[] = checkArr(departments, 0);
        setDepartments(new_arr);
      }
      setInit(false);
    });
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
      .storeUser(
        values.email,
        values.name,
        values.avatar,
        values.password,
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

  return (
    <>
      {open ? (
        <Modal
          title="添加学员"
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
              name="create-basic"
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
                      onSelected={(url) => {
                        setAvatar(url);
                        form.setFieldsValue({ avatar: url });
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
                <Input style={{ width: 274 }} placeholder="请填写学员姓名" />
              </Form.Item>
              <Form.Item
                label="登录邮箱"
                name="email"
                rules={[{ required: true, message: "请输入登录邮箱!" }]}
              >
                <Input
                  autoComplete="off"
                  allowClear
                  style={{ width: 274 }}
                  placeholder="请输入学员登录邮箱"
                />
              </Form.Item>
              <Form.Item
                label="登录密码"
                name="password"
                rules={[{ required: true, message: "请输入登录密码!" }]}
              >
                <Input.Password
                  autoComplete="off"
                  allowClear
                  style={{ width: 274 }}
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
