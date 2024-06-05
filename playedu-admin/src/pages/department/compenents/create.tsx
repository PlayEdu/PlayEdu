import React, { useState, useRef, useEffect } from "react";
import { Modal, Form, Input, Cascader, message, Spin } from "antd";
import styles from "./create.module.less";
import { department } from "../../../api/index";

interface PropInterface {
  open: boolean;
  onCancel: () => void;
}

interface Option {
  value: string | number;
  label: string;
  children?: Option[];
}

export const DepartmentCreate: React.FC<PropInterface> = ({
  open,
  onCancel,
}) => {
  const [form] = Form.useForm();
  const [init, setInit] = useState(true);
  const [loading, setLoading] = useState(false);
  const [departments, setDepartments] = useState<any>([]);
  const [parent_id, setParentId] = useState<number>(0);

  useEffect(() => {
    setInit(true);
    if (open) {
      form.setFieldsValue({
        name: "",
        parent_id: [0],
      });
      setParentId(0);
      getParams();
    }
  }, [form, open]);

  const getParams = () => {
    department.createDepartment().then((res: any) => {
      const departments = res.data.departments;
      if (JSON.stringify(departments) !== "{}") {
        const new_arr: Option[] = checkArr(departments, 0);
        new_arr.unshift({
          label: "作为一级部门",
          value: 0,
        });
        setDepartments(new_arr);
      } else {
        const new_arr: Option[] = [];
        new_arr.unshift({
          label: "作为一级部门",
          value: 0,
        });
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
    setLoading(true);
    department
      .storeDepartment(values.name, parent_id || 0, 0)
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

  const handleChange = (value: any) => {
    if (value !== undefined) {
      let it = value[value.length - 1];
      setParentId(it);
    } else {
      setParentId(0);
    }
  };

  const displayRender = (label: any, selectedOptions: any) => {
    return label[label.length - 1];
  };

  return (
    <>
      {open ? (
        <Modal
          title="新建部门"
          centered
          forceRender
          open={true}
          width={416}
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
              name="basic"
              labelCol={{ span: 8 }}
              wrapperCol={{ span: 16 }}
              initialValues={{ remember: true }}
              onFinish={onFinish}
              onFinishFailed={onFinishFailed}
              autoComplete="off"
            >
              <Form.Item
                label="所属上级"
                name="parent_id"
                rules={[{ required: true, message: "请选择所属上级!" }]}
              >
                <Cascader
                  style={{ width: 200 }}
                  allowClear
                  placeholder="请选择所属上级"
                  onChange={handleChange}
                  options={departments}
                  changeOnSelect
                  expand-trigger="hover"
                  displayRender={displayRender}
                />
              </Form.Item>
              <Form.Item
                label="部门名称"
                name="name"
                rules={[{ required: true, message: "请输入部门名称!" }]}
              >
                <Input
                  style={{ width: 200 }}
                  allowClear
                  placeholder="请输入部门名称"
                />
              </Form.Item>
            </Form>
          </div>
        </Modal>
      ) : null}
    </>
  );
};
