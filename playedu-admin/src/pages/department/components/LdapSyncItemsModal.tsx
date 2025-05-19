import React, { useState, useEffect } from "react";
import { Modal, Table, Tag } from "antd";
import { ldap } from "../../../api";

interface LdapSyncItemsModalProps {
  recordId: number;
  type: "department" | "user";
  action: number;
  open: boolean;
  onCancel: () => void;
}

export const LdapSyncItemsModal: React.FC<LdapSyncItemsModalProps> = ({
  recordId,
  type,
  action,
  open,
  onCancel
}) => {
  const [loading, setLoading] = useState(false);
  const [list, setList] = useState<any[]>([]);
  const [total, setTotal] = useState(0);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(10);

  useEffect(() => {
    if (open) {
      loadData();
    }
  }, [open, page, size, type, action]);

  const loadData = () => {
    setLoading(true);
    ldap.getSyncRecordDetails(recordId, {
      type,
      action,
      page,
      size,
    }).then((res: any) => {
      setList(res.data.records);
      setTotal(res.data.total);
      setLoading(false);
    }).catch(() => {
      setLoading(false);
    });
  };

  const getActionText = (action: number) => {
    const actions: any = {
      department: {
        1: "新增",
        2: "更新",
        3: "删除",
        4: "无变化"
      },
      user: {
        1: "新增",
        2: "更新",
        3: "删除",
        4: "无变化",
        5: "禁止"
      }
    };
    
    return actions[type][action] || "未知";
  };

  const getActionColor = (action: number) => {
    const colors: {[key: number]: string} = {
      1: "green",  // 新增
      2: "blue",   // 更新
      3: "red",    // 删除
      4: "gray",   // 无变化
      5: "orange"  // 禁止
    };
    
    return colors[action] || "default";
  };

  const departmentColumns = [
    { title: "ID", dataIndex: "id", key: "id", width: 60 },
    { title: "部门名称", dataIndex: "name", key: "name" },
    { title: "DN", dataIndex: "dn", key: "dn", ellipsis: true },
    { title: "UUID", dataIndex: "uuid", key: "uuid", width: 280 },
    { 
      title: "操作类型", 
      dataIndex: "action", 
      key: "action",
      width: 100,
      render: (actionType: number) => (
        <Tag color={getActionColor(actionType)}>
          {getActionText(actionType)}
        </Tag>
      )
    },
  ];

  const userColumns = [
    { title: "ID", dataIndex: "id", key: "id", width: 60 },
    { title: "用户名", dataIndex: "cn", key: "cn" },
    { title: "登录名", dataIndex: "uid", key: "uid" },
    { title: "邮箱", dataIndex: "email", key: "email" },
    { title: "部门", dataIndex: "ou", key: "ou", ellipsis: true },
    { 
      title: "操作类型", 
      dataIndex: "action", 
      key: "action",
      width: 100,
      render: (actionType: number) => (
        <Tag color={getActionColor(actionType)}>
          {getActionText(actionType)}
        </Tag>
      )
    },
  ];

  const columns = type === "department" ? departmentColumns : userColumns;
  const title = type === "department" ? "部门同步详情" : "用户同步详情";
  const actionText = action > 0 ? ` - ${getActionText(action)}` : "";

  return (
    <Modal
      title={`${title}${actionText}`}
      open={open}
      onCancel={onCancel}
      width={900}
      footer={null}
    >
      <Table
        columns={columns}
        dataSource={list}
        rowKey="id"
        loading={loading}
        pagination={{
          total,
          current: page,
          pageSize: size,
          onChange: (page, pageSize) => {
            setPage(page);
            setSize(pageSize || 10);
          },
          showSizeChanger: true,
        }}
      />
    </Modal>
  );
}; 