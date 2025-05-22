import { useEffect, useState } from "react";
import {
  Modal,
  Table,
  message,
  Space,
  Typography,
  Input,
  Select,
  Button,
} from "antd";
import { resource } from "../../../api";
import { useLocation } from "react-router-dom";
import { ExclamationCircleFilled } from "@ant-design/icons";
import type { ColumnsType } from "antd/es/table";
import { dateFormat } from "../../../utils/index";
import { TreeCategory, UploadCoursewareButton } from "../../../compenents";
import { CoursewareUpdateDialog } from "./compenents/update-dialog";

const { confirm } = Modal;

interface DataType {
  admin_id: number;
  created_at: string;
  disk: string;
  extension: string;
  file_id: string;
  id: React.Key;
  name: string;
  parent_id: number;
  path: string;
  size: number;
  type: string;
  url: string;
}

type AdminUsersModel = {
  [key: number]: string;
};

const ResourceCoursewarePage = () => {
  const result = new URLSearchParams(useLocation().search);
  const [list, setList] = useState<DataType[]>([]);
  const [adminUsers, setAdminUsers] = useState<AdminUsersModel>({});
  const [existingTypes, setExistingTypes] = useState<string[]>([]);
  const [resourceUrl, setResourceUrl] = useState<ResourceUrlModel>({});
  const [refresh, setRefresh] = useState(false);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(10);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(true);
  const [category_ids, setCategoryIds] = useState<number[]>([]);
  const [selectedRowKeys, setSelectedRowKeys] = useState<any>([]);
  const [type, setType] = useState("WORD,EXCEL,PPT,PDF,TXT,RAR,ZIP");
  const [title, setTitle] = useState("");
  const [multiConfig, setMultiConfig] = useState(false);
  const [selLabel, setLabel] = useState<string>(
    result.get("label") ? String(result.get("label")) : "全部课件"
  );
  const [cateId, setCateId] = useState(Number(result.get("cid")));
  const [updateId, setUpdateId] = useState(0);
  const [updateVisible, setUpdateVisible] = useState(false);
  const types = [
    { label: "全部", value: "WORD,EXCEL,PPT,PDF,TXT,RAR,ZIP" },
    { label: "WORD", value: "WORD" },
    { label: "EXCEL", value: "EXCEL" },
    { label: "PPT", value: "PPT" },
    { label: "PDF", value: "PDF" },
    { label: "TXT", value: "TXT" },
    { label: "RAR", value: "RAR" },
    { label: "ZIP", value: "ZIP" },
  ];

  useEffect(() => {
    setCateId(Number(result.get("cid")));
    if (Number(result.get("cid"))) {
      let arr = [];
      arr.push(Number(result.get("cid")));
      setCategoryIds(arr);
    }
  }, [result.get("cid")]);

  // 加载课件列表
  useEffect(() => {
    getList();
  }, [category_ids, refresh, page, size]);

  const getList = () => {
    setLoading(true);
    let categoryIds = category_ids.join(",");
    resource
      .resourceList(page, size, "", "", title, type, categoryIds)
      .then((res: any) => {
        setTotal(res.data.result.total);
        setList(res.data.result.data);
        setExistingTypes(res.data.existing_types);
        setAdminUsers(res.data.admin_users);
        setResourceUrl(res.data.resource_url);
        setLoading(false);
      })
      .catch((err: any) => {
        console.log("错误,", err);
      });
  };

  const columns: ColumnsType<DataType> = [
    {
      title: "课件名称",
      render: (_, record: any) => (
        <div className="d-flex">
          <i
            className="iconfont icon-icon-file"
            style={{
              fontSize: 16,
              color: "rgba(0,0,0,0.3)",
            }}
          />
          <span className="ml-8">
            {record.name}.{record.extension}
          </span>
        </div>
      ),
    },
    {
      title: "课件格式",
      dataIndex: "type",
      render: (type: string) => <span>{type}</span>,
    },
    {
      title: "课件大小",
      dataIndex: "size",
      render: (size: number) => <span>{(size / 1024 / 1024).toFixed(2)}M</span>,
    },
    {
      title: "创建人",
      dataIndex: "admin_id",
      render: (text: number) =>
        JSON.stringify(adminUsers) !== "{}" && <span>{adminUsers[text]}</span>,
    },
    {
      title: "创建时间",
      dataIndex: "created_at",
      render: (text: string) => <span>{dateFormat(text)}</span>,
    },
    {
      title: "操作",
      key: "action",
      fixed: "right",
      width: 180,
      render: (_, record: any) => {
        return (
          <Space size="small">
            <Button
              type="link"
              size="small"
              className="b-n-link c-red"
              onClick={() => {
                downLoadFile(
                  resourceUrl[record.id],
                  record.name,
                  record.extension
                );
              }}
            >
              下载
            </Button>
            <div className="form-column"></div>
            <Button
              type="link"
              className="b-link c-red"
              onClick={() => {
                setUpdateId(record.id);
                setUpdateVisible(true);
              }}
            >
              编辑
            </Button>
            <div className="form-column"></div>
            <Button
              type="link"
              className="b-link c-red"
              onClick={() => removeResource(record.id)}
            >
              删除
            </Button>
          </Space>
        );
      },
    },
  ];

  const paginationProps = {
    current: page, //当前页码
    pageSize: size,
    total: total, // 总条数
    onChange: (page: number, pageSize: number) =>
      handlePageChange(page, pageSize), //改变页码的函数
    showSizeChanger: true,
  };

  const handlePageChange = (page: number, pageSize: number) => {
    setPage(page);
    setSize(pageSize);
  };

  const rowSelection = {
    onChange: (selectedRowKeys: React.Key[], selectedRows: DataType[]) => {
      setSelectedRowKeys(selectedRowKeys);
    },
  };

  // 重置列表
  const resetList = () => {
    setPage(1);
    setSize(10);
    setList([]);
    setTitle("");
    setSelectedRowKeys([]);
    setType("WORD,EXCEL,PPT,PDF,TXT,RAR,ZIP");
    setRefresh(!refresh);
  };

  // 删除课件
  const removeResource = (id: number) => {
    if (id === 0) {
      return;
    }
    confirm({
      title: "操作确认",
      icon: <ExclamationCircleFilled />,
      content: "删除前请检查选中课件文件无关联课程，确认删除？",
      centered: true,
      okText: "确认",
      cancelText: "取消",
      onOk() {
        resource.destroyResource(id).then(() => {
          message.success("删除成功");
          resetList();
        });
      },
      onCancel() {
        console.log("Cancel");
      },
    });
  };

  // 批量删除课件
  const removeResourceMulti = () => {
    if (selectedRowKeys.length === 0) {
      return;
    }
    confirm({
      title: "操作确认",
      icon: <ExclamationCircleFilled />,
      content: "删除前请检查选中课件文件无关联课程，确认删除？",
      centered: true,
      okText: "确认",
      cancelText: "取消",
      onOk() {
        resource.destroyResourceMulti(selectedRowKeys).then(() => {
          message.success("删除成功");
          resetList();
        });
      },
      onCancel() {
        console.log("Cancel");
      },
    });
  };

  const downLoadFile = (url: string, name: string, extension: string) => {
    window.open(url);
    const a = document.createElement("a");
    a.style.display = "none";
    a.href = url;
    a.download = `${name}.${extension}`; // 设置下载的文件名
    document.body.appendChild(a);
    a.click(); // 触发点击事件
    // 释放 URL 对象
    URL.revokeObjectURL(url);
    document.body.removeChild(a);
  };

  return (
    <>
      <div className="tree-main-body">
        <div className="left-box">
          <TreeCategory
            selected={category_ids}
            type="no-cate"
            text={"课件"}
            onUpdate={(keys: any, title: any) => {
              setPage(1);
              setCategoryIds(keys);
              if (typeof title === "string") {
                setLabel(title);
              } else {
                setLabel(title.props.children[0]);
              }
            }}
          />
        </div>
        <div className="right-box">
          <div className="d-flex playedu-main-title float-left mb-24">
            课件 | {selLabel}
          </div>
          <div className="float-left  j-b-flex  mb-24">
            <div>
              <UploadCoursewareButton
                categoryIds={category_ids}
                onUpdate={() => {
                  resetList();
                }}
              ></UploadCoursewareButton>
              <Button
                type="default"
                className="ml-16"
                onClick={() => {
                  setSelectedRowKeys([]);
                  setMultiConfig(!multiConfig);
                }}
              >
                {multiConfig ? "取消操作" : "批量操作"}
              </Button>
              <Button
                type="default"
                className="ml-16"
                onClick={() => removeResourceMulti()}
                disabled={selectedRowKeys.length === 0}
              >
                删除
              </Button>
            </div>
            <div className="d-flex">
              <div className="d-flex">
                <div className="d-flex mr-24">
                  <Typography.Text>名称：</Typography.Text>
                  <Input
                    value={title}
                    onChange={(e) => {
                      setTitle(e.target.value);
                    }}
                    allowClear
                    style={{ width: 160 }}
                    placeholder="请输入名称关键字"
                  />
                </div>
                <div className="d-flex mr-24">
                  <Typography.Text>格式：</Typography.Text>
                  <Select
                    style={{ width: 160 }}
                    placeholder="请选择格式"
                    value={type}
                    onChange={(value: string) => setType(value)}
                    options={types}
                  />
                </div>
                <Button className="mr-16" onClick={resetList}>
                  重 置
                </Button>
                <Button
                  type="primary"
                  onClick={() => {
                    setPage(1);
                    setRefresh(!refresh);
                  }}
                >
                  查 询
                </Button>
              </div>
            </div>
          </div>
          <div className="float-left">
            {multiConfig ? (
              <Table
                rowSelection={{
                  type: "checkbox",
                  ...rowSelection,
                }}
                columns={columns}
                dataSource={list}
                loading={loading}
                pagination={paginationProps}
                rowKey={(record) => record.id}
              />
            ) : (
              <Table
                columns={columns}
                dataSource={list}
                loading={loading}
                pagination={paginationProps}
                rowKey={(record) => record.id}
              />
            )}
          </div>
        </div>
        <CoursewareUpdateDialog
          id={Number(updateId)}
          open={updateVisible}
          onCancel={() => setUpdateVisible(false)}
          onSuccess={() => {
            setUpdateVisible(false);
            setRefresh(!refresh);
          }}
        ></CoursewareUpdateDialog>
      </div>
    </>
  );
};

export default ResourceCoursewarePage;
