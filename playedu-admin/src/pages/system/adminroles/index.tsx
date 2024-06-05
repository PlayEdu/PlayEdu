import React, { useState, useEffect } from "react";
import { Button, Space, Table, Modal, message } from "antd";
import type { ColumnsType } from "antd/es/table";
// import styles from "./index.module.less";
import { PlusOutlined, ExclamationCircleFilled } from "@ant-design/icons";
import { adminRole } from "../../../api/index";
import { dateFormat } from "../../../utils/index";
import { SystemAdminrolesCreate } from "./compenents/create";
import { SystemAdminrolesUpdate } from "./compenents/update";
import { PerButton } from "../../../compenents";

const { confirm } = Modal;

interface DataType {
  id: React.Key;
  name: string;
  created_at: string;
}

const SystemAdminrolesPage = () => {
  const [loading, setLoading] = useState<boolean>(true);
  const [list, setList] = useState<any>([]);
  const [refresh, setRefresh] = useState(false);
  const [createVisible, setCreateVisible] = useState<boolean>(false);
  const [updateVisible, setUpdateVisible] = useState<boolean>(false);
  const [cid, setCid] = useState<number>(0);

  const columns: ColumnsType<DataType> = [
    {
      title: "角色名",
      dataIndex: "name",
      render: (text: string) => <span>{text}</span>,
    },
    {
      title: "时间",
      dataIndex: "created_at",
      render: (text: string) => <span>{text && dateFormat(text)}</span>,
    },
    {
      title: "操作",
      key: "action",
      fixed: "right",
      width: 160,
      render: (_, record) => (
        <Space size="small">
          <PerButton
            type="link"
            text="编辑"
            class="b-link c-red"
            icon={null}
            p="admin-role"
            onClick={() => {
              setCid(Number(record.id));
              setUpdateVisible(true);
            }}
            disabled={null}
          />
          <div className="form-column"></div>
          <PerButton
            type="link"
            text="删除"
            class="b-link c-red"
            icon={null}
            p="admin-role"
            onClick={() => delUser(record.id)}
            disabled={null}
          />
        </Space>
      ),
    },
  ];

  useEffect(() => {
    getData();
  }, [refresh]);

  const getData = () => {
    setLoading(true);
    adminRole.adminRoleList().then((res: any) => {
      setList(res.data);
      setLoading(false);
    });
  };

  const resetData = () => {
    setList([]);
    setRefresh(!refresh);
  };

  const delUser = (id: any) => {
    if (id === 0) {
      return;
    }
    confirm({
      title: "操作确认",
      icon: <ExclamationCircleFilled />,
      content: "确认删除此角色？",
      centered: true,
      okText: "确认",
      cancelText: "取消",
      onOk() {
        adminRole.destroyAdminRole(id).then((res: any) => {
          message.success("操作成功");
          resetData();
        });
      },
      onCancel() {
        console.log("Cancel");
      },
    });
  };

  return (
    <>
      <div className="playedu-main-body">
        <div className="float-left j-b-flex mb-24">
          <div className="d-flex">
            <Button
              icon={<PlusOutlined />}
              className="mr-16"
              type="primary"
              onClick={() => setCreateVisible(true)}
            >
              新建角色
            </Button>
          </div>
          <div className="d-flex"></div>
        </div>
        <div className="float-left">
          <Table
            columns={columns}
            dataSource={list}
            loading={loading}
            rowKey={(record) => record.id}
          />
          <SystemAdminrolesCreate
            open={createVisible}
            onCancel={() => {
              setCreateVisible(false);
              setRefresh(!refresh);
            }}
          />
          <SystemAdminrolesUpdate
            id={cid}
            open={updateVisible}
            onCancel={() => {
              setUpdateVisible(false);
              setRefresh(!refresh);
            }}
          />
        </div>
      </div>
    </>
  );
};

export default SystemAdminrolesPage;
