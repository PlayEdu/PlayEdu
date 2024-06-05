import React, { useState, useEffect } from "react";
import { Modal, Form } from "antd";
import { adminLog } from "../../../../api";

interface PropInterface {
  id: number;
  open: boolean;
  onCancel: () => void;
}

export const AdminLogDetailDialog: React.FC<PropInterface> = ({
  id,
  open,
  onCancel,
}) => {
  const [form] = Form.useForm();
  const [param, setParam] = useState("");
  const [result, setResult] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (open && id > 0) {
      getDetail();
    }
  }, [open, id]);

  const getDetail = () => {
    adminLog.adminLogDetail(id).then((res: any) => {
      setParam(res.data.param);
      setResult(res.data.result);
    });
  };

  const onFinish = (values: any) => {};

  const onFinishFailed = (errorInfo: any) => {
    console.log("Failed:", errorInfo);
  };

  return (
    <>
      {open ? (
        <Modal
          title="日志详情"
          centered
          forceRender
          open={true}
          width={600}
          onOk={() => onCancel()}
          onCancel={() => onCancel()}
          footer={null}
          maskClosable={false}
        >
          <div
            className="mt-24"
            style={{ maxHeight: 600, overflowY: "auto", overflowX: "hidden" }}
          >
            <Form
              form={form}
              name="adminlog-detail"
              labelCol={{ span: 3 }}
              wrapperCol={{ span: 21 }}
              initialValues={{ remember: true }}
              onFinish={onFinish}
              onFinishFailed={onFinishFailed}
              autoComplete="off"
            >
              <Form.Item label="Param">{param}</Form.Item>
              <Form.Item label="Result">{result}</Form.Item>
            </Form>
          </div>
        </Modal>
      ) : null}
    </>
  );
};
