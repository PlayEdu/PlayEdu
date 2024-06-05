import { Button, Input, message, Modal } from "antd";
import { useState } from "react";
import { resourceCategory } from "../../api";
import { PlusOutlined } from "@ant-design/icons";

interface PropInterface {
  type: string;
  onUpdate: () => void;
}

export const CreateResourceCategory = (props: PropInterface) => {
  const [showModal, setShowModal] = useState(false);
  const [name, setName] = useState<string>("");

  const confirm = () => {
    if (name.length == 0) {
      message.error("请输入分类名");
      return;
    }
    resourceCategory
      .storeResourceCategory(name, 0, 0)
      .then(() => {
        setName("");
        message.success("分类添加成功");
        setShowModal(false);
        props.onUpdate();
      })
      .catch((err) => {
        console.log("错误", err);
      });
  };

  return (
    <>
      <Button
        type="primary"
        onClick={() => {
          setShowModal(true);
        }}
        shape="circle"
        icon={<PlusOutlined />}
      />
      {showModal ? (
        <Modal
          onCancel={() => {
            setShowModal(false);
          }}
          onOk={confirm}
          open={true}
          title="创建分类"
        >
          <Input
            placeholder="请输入分类名"
            value={name}
            onChange={(e) => {
              setName(e.target.value);
            }}
            allowClear
          />
        </Modal>
      ) : null}
    </>
  );
};
