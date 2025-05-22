import React, { useState, useEffect } from "react";
import {
  Typography,
  Input,
  Modal,
  Button,
  Space,
  Table,
  message,
  Image,
  Dropdown,
} from "antd";
import type { MenuProps } from "antd";
import type { ColumnsType } from "antd/es/table";
// import styles from "./index.module.less";
import {
  PlusOutlined,
  DownOutlined,
  ExclamationCircleFilled,
} from "@ant-design/icons";
import { user } from "../../api/index";
import { dateFormat } from "../../utils/index";
import { Link, useLocation, useSearchParams } from "react-router-dom";
import { useSelector } from "react-redux";
import { TreeDepartment, PerButton } from "../../compenents";
import { MemberCreate } from "./compenents/create";
import { MemberUpdate } from "./compenents/update";
import memberDefaultAvatar from "../../assets/thumb/avatar.png";
const { confirm } = Modal;

interface DataType {
  id: React.Key;
  avatar: string;
  create_city?: string;
  create_ip?: string;
  created_at?: string;
  credit1?: number;
  email: string;
  id_card?: string;
  is_active?: number;
  is_lock?: number;
  is_set_password?: number;
  is_verify?: number;
  login_at?: string;
  name: string;
  updated_at?: string;
  verify_at?: string;
}

interface LocalSearchParamsInterface {
  page?: number;
  size?: number;
  nickname?: string;
  email?: string;
}

const MemberPage = () => {
  const result = new URLSearchParams(useLocation().search);

  const [searchParams, setSearchParams] = useSearchParams({
    page: "1",
    size: "10",
    nickname: "",
    email: "",
  });
  const page = parseInt(searchParams.get("page") || "1");
  const size = parseInt(searchParams.get("size") || "10");
  const nickname = searchParams.get("nickname");
  const email = searchParams.get("email");

  const [loading, setLoading] = useState(false);
  const [list, setList] = useState<DataType[]>([]);
  const [resourceUrl, setResourceUrl] = useState<ResourceUrlModel>({});
  const [total, setTotal] = useState(0);
  const [refresh, setRefresh] = useState(false);
  const [dep_ids, setDepIds] = useState<number[]>([]);
  const [selLabel, setLabel] = useState<string>(
    result.get("label") ? String(result.get("label")) : "全部部门"
  );
  const [createVisible, setCreateVisible] = useState(false);
  const [updateVisible, setUpdateVisible] = useState(false);
  const [mid, setMid] = useState(0);
  const [user_dep_ids, setUserDepIds] = useState<DepIdsModel>({});
  const [departments, setDepartments] = useState<DepartmentsModel>({});
  const [did, setDid] = useState(Number(result.get("did")));
  const ldapEnabled = useSelector(
    (state: any) => state.systemConfig.value["ldap-enabled"]
  );

  useEffect(() => {
    if (result.get("refresh")) {
      resetLocalSearchParams({
        page: 1,
      });
      setRefresh(!refresh);
    }
  }, [result.get("refresh")]);

  const columns: ColumnsType<DataType> = [
    {
      title: "学员",
      dataIndex: "name",
      width: 300,
      render: (_, record: any) => (
        <>
          <Image
            style={{ borderRadius: "50%" }}
            src={
              record.avatar == -1
                ? memberDefaultAvatar
                : resourceUrl[record.avatar]
            }
            preview={false}
            width={40}
            height={40}
          />
          <span className="ml-8">{record.name}</span>
        </>
      ),
    },
    {
      title: "所属部门",
      dataIndex: "id",
      render: (id: number) => (
        <div className="float-left">
          {user_dep_ids[id] &&
            user_dep_ids[id].map((item: any, index: number) => {
              return (
                <span key={index}>
                  {index === user_dep_ids[id].length - 1
                    ? departments[item]
                    : departments[item] + "、"}
                </span>
              );
            })}
        </div>
      ),
    },
    {
      title: "登录邮箱",
      width: 200,
      dataIndex: "email",
      render: (email: string) => <span>{email}</span>,
    },
    {
      title: "加入时间",
      width: 200,
      dataIndex: "created_at",
      render: (text: string) => <span>{dateFormat(text)}</span>,
    },
    {
      title: "操作",
      key: "action",
      fixed: "right",
      width: 160,
      render: (_, record: any) => {
        const items: MenuProps["items"] = [
          {
            key: "1",
            label: (
              <PerButton
                type="link"
                text="编辑"
                class="b-link c-red"
                icon={null}
                p="user-update"
                onClick={() => {
                  setMid(Number(record.id));
                  setUpdateVisible(true);
                }}
                disabled={null}
              />
            ),
          },
          {
            key: "2",
            label: (
              <PerButton
                type="link"
                text="删除"
                class="b-link c-red"
                icon={null}
                p="user-destroy"
                onClick={() => delUser(record.id)}
                disabled={null}
              />
            ),
          },
        ];

        return (
          <Space size="small">
            <Link
              style={{ textDecoration: "none" }}
              to={`/member/learn?id=${record.id}&name=${record.name}`}
            >
              <PerButton
                type="link"
                text="学习"
                class="b-link c-red"
                icon={null}
                p="user-learn"
                onClick={() => null}
                disabled={null}
              />
            </Link>
            {!ldapEnabled && (
              <>
                <div className="form-column"></div>
                <Dropdown menu={{ items }}>
                  <Button
                    type="link"
                    className="b-link c-red"
                    onClick={(e) => e.preventDefault()}
                  >
                    <Space size="small" align="center">
                      更多
                      <DownOutlined />
                    </Space>
                  </Button>
                </Dropdown>
              </>
            )}
          </Space>
        );
      },
    },
  ];

  useEffect(() => {
    getData();
  }, [refresh, page, size, dep_ids]);

  useEffect(() => {
    const handlePageBack = () => {
      getData();
    };
    window.addEventListener("popstate", handlePageBack);
    return () => {
      window.removeEventListener("popstate", handlePageBack);
    };
  }, []);

  const getData = () => {
    if (loading) {
      return;
    }
    setLoading(true);
    user
      .userList(page, size, {
        name: nickname,
        email: email,
        dep_ids: dep_ids.join(","),
      })
      .then((res: any) => {
        setList(res.data.data);
        setDepartments(res.data.departments);
        setUserDepIds(res.data.user_dep_ids);
        setResourceUrl(res.data.resource_url);
        setTotal(res.data.total);
        setLoading(false);
      });
  };

  const resetData = () => {
    resetLocalSearchParams({
      page: 1,
      size: 10,
      nickname: "",
      email: "",
    });
    setList([]);
    setRefresh(!refresh);
  };

  const paginationProps = {
    current: page, //当前页码
    pageSize: size,
    total: total, // 总条数
    onChange: (page: number, pageSize: number) =>
      handlePageChange(page, pageSize), //改变页码的函数
    showSizeChanger: true,
  };

  const handlePageChange = (page: number, pageSize: number) => {
    resetLocalSearchParams({
      page: page,
      size: pageSize,
    });
  };

  const resetLocalSearchParams = (params: LocalSearchParamsInterface) => {
    setSearchParams(
      (prev) => {
        if (typeof params.nickname !== "undefined") {
          prev.set("nickname", params.nickname);
        }
        if (typeof params.email !== "undefined") {
          prev.set("email", params.email);
        }
        if (typeof params.page !== "undefined") {
          prev.set("page", params.page + "");
        }
        if (typeof params.size !== "undefined") {
          prev.set("size", params.size + "");
        }
        return prev;
      },
      { replace: true }
    );
  };

  const delUser = (id: number) => {
    if (id === 0) {
      return;
    }
    confirm({
      title: "操作确认",
      icon: <ExclamationCircleFilled />,
      content: "确认删除此学员？",
      centered: true,
      okText: "确认",
      cancelText: "取消",
      onOk() {
        user.destroyUser(id).then((res: any) => {
          message.success("操作成功");
          setRefresh(!refresh);
        });
      },
      onCancel() {
        console.log("Cancel");
      },
    });
  };

  return (
    <>
      <div className="tree-main-body">
        <div className="left-box">
          <TreeDepartment
            selected={dep_ids}
            refresh={refresh}
            showNum={true}
            text={"部门"}
            onUpdate={(keys: any, title: any) => {
              resetLocalSearchParams({
                page: 1,
              });
              setDepIds(keys);
              var index = title.indexOf("(");
              if (index !== -1) {
                var resolve = title.substring(0, index);
                setLabel(resolve);
              } else {
                setLabel(title);
              }
            }}
          />
        </div>
        <div className="right-box">
          <div className="playedu-main-title float-left mb-24">
            学员 | {selLabel}
          </div>
          <div className="float-left j-b-flex mb-24">
            <div className="d-flex">
              {!ldapEnabled && (
                <PerButton
                  type="primary"
                  text="添加学员"
                  class="mr-16"
                  icon={<PlusOutlined />}
                  p="user-store"
                  onClick={() => setCreateVisible(true)}
                  disabled={null}
                />
              )}
              {!ldapEnabled && dep_ids.length === 0 && (
                <Link style={{ textDecoration: "none" }} to={`/member/import`}>
                  <PerButton
                    type="default"
                    text="批量导入学员"
                    class="mr-16"
                    icon={null}
                    p="user-store"
                    disabled={null}
                  />
                </Link>
              )}
              {/* {dep_ids.length > 0 && (
                <Link
                  style={{ textDecoration: "none" }}
                  to={`/member/departmentUser?id=${dep_ids.join(
                    ","
                  )}&title=${selLabel}`}
                >
                  <PerButton
                    type="default"
                    text="部门学员进度"
                    class="mr-16"
                    p="department-user-learn"
                    disabled={null}
                  />
                </Link>
              )} */}
            </div>
            <div className="d-flex">
              <div className="d-flex mr-24">
                <Typography.Text>姓名：</Typography.Text>
                <Input
                  value={nickname || ""}
                  onChange={(e) => {
                    resetLocalSearchParams({
                      nickname: e.target.value,
                    });
                  }}
                  style={{ width: 160 }}
                  placeholder="请输入姓名关键字"
                  allowClear
                />
              </div>
              <div className="d-flex mr-24">
                <Typography.Text>邮箱：</Typography.Text>
                <Input
                  value={email || ""}
                  onChange={(e) => {
                    resetLocalSearchParams({
                      email: e.target.value,
                    });
                  }}
                  style={{ width: 160 }}
                  placeholder="请输入邮箱账号"
                  allowClear
                />
              </div>
              <div className="d-flex">
                <Button className="mr-16" onClick={resetData}>
                  重 置
                </Button>
                <Button
                  type="primary"
                  onClick={() => {
                    resetLocalSearchParams({
                      page: 1,
                    });
                    setRefresh(!refresh);
                  }}
                >
                  查 询
                </Button>
              </div>
            </div>
          </div>
          <div className="float-left">
            <Table
              columns={columns}
              dataSource={list}
              loading={loading}
              pagination={paginationProps}
              rowKey={(record) => record.id}
            />
            <MemberCreate
              open={createVisible}
              depIds={dep_ids}
              onCancel={() => {
                setCreateVisible(false);
                setRefresh(!refresh);
              }}
            />
            <MemberUpdate
              id={mid}
              open={updateVisible}
              onCancel={() => {
                setUpdateVisible(false);
                setRefresh(!refresh);
              }}
            />
          </div>
        </div>
      </div>
    </>
  );
};

export default MemberPage;
