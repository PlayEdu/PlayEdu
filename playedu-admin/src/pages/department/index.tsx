import React, { useState, useEffect } from "react";
import { Spin, Button, Tree, Modal, message, Tooltip } from "antd";
// import styles from "./index.module.less";
import { PlusOutlined, ExclamationCircleFilled } from "@ant-design/icons";
import { department } from "../../api/index";
import { PerButton } from "../../compenents";
import type { DataNode, TreeProps } from "antd/es/tree";
import { DepartmentCreate } from "./compenents/create";
import { DepartmentUpdate } from "./compenents/update";
import { useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { saveDepartmentsAction } from "../../store/system/systemConfigSlice";

const { confirm } = Modal;

interface Option {
  key: string | number;
  title: any;
  children?: Option[];
}

const DepartmentPage = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const permissions = useSelector(
    (state: any) => state.loginUser.value.permissions
  );
  const [loading, setLoading] = useState(true);
  const [refresh, setRefresh] = useState(false);
  const [treeData, setTreeData] = useState<Option[]>([]);
  const [selectKey, setSelectKey] = useState<number[]>([]);

  const [createVisible, setCreateVisible] = useState(false);
  const [updateVisible, setUpdateVisible] = useState(false);
  const [did, setDid] = useState<number>(0);
  const [modal, contextHolder] = Modal.useModal();

  // 是否启用LDAP
  const ldapEnabled = useSelector(
    (state: any) => state.systemConfig.value["ldap-enabled"]
  );

  const onSelect = (selectedKeys: any, info: any) => {
    setSelectKey(selectedKeys);
  };

  const through = (p: string) => {
    if (!permissions) {
      return false;
    }
    return typeof permissions[p] !== "undefined";
  };

  useEffect(() => {
    setLoading(true);
    getData();
  }, [refresh, permissions]);

  const getData = () => {
    department.departmentList({}).then((res: any) => {
      const departments: DepartmentsBoxModel = res.data.departments;
      dispatch(saveDepartmentsAction(res.data.departments));
      if (JSON.stringify(departments) !== "{}") {
        const new_arr: Option[] = checkArr(departments, 0);
        setTreeData(new_arr);
      }

      setLoading(false);
    });
  };

  const checkArr = (departments: DepartmentsBoxModel, id: number) => {
    const arr = [];
    for (let i = 0; i < departments[id].length; i++) {
      if (!departments[departments[id][i].id]) {
        if (ldapEnabled) {
          arr.push({
            title: (
              <>
                <div className="tree-title-elli">{departments[id][i].name}</div>
              </>
            ),
            key: departments[id][i].id,
          });
        } else {
          arr.push({
            title: (
              <>
                <div className="tree-title-elli">{departments[id][i].name}</div>
                <div className="d-flex">
                  <Tooltip placement="top" title="可拖拽排序">
                    <i
                      className="iconfont icon-icon-drag mr-16"
                      style={{ fontSize: 24 }}
                    />
                  </Tooltip>
                  {through("department-cud") && (
                    <>
                      <i
                        className="iconfont icon-icon-edit mr-16"
                        style={{ fontSize: 24 }}
                        onClick={() => {
                          setDid(departments[id][i].id);
                          setUpdateVisible(true);
                        }}
                      />
                      <i
                        className="iconfont icon-icon-delete"
                        style={{ fontSize: 24 }}
                        onClick={() =>
                          removeItem(
                            departments[id][i].id,
                            departments[id][i].name
                          )
                        }
                      />
                    </>
                  )}
                </div>
              </>
            ),
            key: departments[id][i].id,
          });
        }
      } else {
        const new_arr: Option[] = checkArr(departments, departments[id][i].id);
        if (ldapEnabled) {
          arr.push({
            title: (
              <>
                <div className="tree-title-elli">{departments[id][i].name}</div>
              </>
            ),
            key: departments[id][i].id,
            children: new_arr,
          });
        } else {
          arr.push({
            title: (
              <>
                <div className="tree-title-elli">{departments[id][i].name}</div>
                <div className="d-flex">
                  <Tooltip placement="top" title="可拖拽排序">
                    <i
                      className="iconfont icon-icon-drag mr-16"
                      style={{ fontSize: 24 }}
                    />
                  </Tooltip>
                  {through("department-cud") && (
                    <>
                      <i
                        className="iconfont icon-icon-edit mr-16"
                        style={{ fontSize: 24 }}
                        onClick={() => {
                          setDid(departments[id][i].id);
                          setUpdateVisible(true);
                        }}
                      />
                      <i
                        className="iconfont icon-icon-delete"
                        style={{ fontSize: 24 }}
                        onClick={() =>
                          removeItem(
                            departments[id][i].id,
                            departments[id][i].name
                          )
                        }
                      />
                    </>
                  )}
                </div>
              </>
            ),
            key: departments[id][i].id,
            children: new_arr,
          });
        }
      }
    }
    return arr;
  };

  const resetData = () => {
    setTreeData([]);
    setRefresh(!refresh);
  };

  const removeItem = (id: number, label: string) => {
    if (id === 0) {
      return;
    }
    department.checkDestroy(id).then((res: any) => {
      if (
        res.data.children &&
        res.data.children.length === 0 &&
        res.data.courses &&
        res.data.courses.length === 0 &&
        res.data.users &&
        res.data.users.length === 0
      ) {
        delUser(id);
      } else {
        if (res.data.children && res.data.children.length > 0) {
          modal.warning({
            title: "操作确认",
            centered: true,
            okText: "好的",
            content: (
              <p>
                此部门下包含
                <span className="c-red">
                  （{res.data.children.length}个子部门）
                </span>
                ，请先解除关联再删除！
              </p>
            ),
          });
        } else {
          modal.warning({
            title: "操作确认",
            centered: true,
            okText: "好的",
            content: (
              <p>
                此部门已关联
                {res.data.courses && res.data.courses.length > 0 && (
                  <Button
                    style={{ paddingLeft: 4, paddingRight: 4 }}
                    type="link"
                    danger
                    onClick={() =>
                      navigate("/course?did=" + id + "&label=" + label)
                    }
                  >
                    （{res.data.courses.length}个线上课程），
                  </Button>
                )}
                {res.data.users && res.data.users.length > 0 && (
                  <Button
                    type="link"
                    style={{ paddingLeft: 4, paddingRight: 4 }}
                    danger
                    onClick={() =>
                      navigate("/member/index?did=" + id + "&label=" + label)
                    }
                  >
                    （{res.data.users.length}个学员），
                  </Button>
                )}
                请先解除关联再删除！
              </p>
            ),
          });
        }
      }
    });
  };

  const delUser = (id: any) => {
    confirm({
      title: "操作确认",
      icon: <ExclamationCircleFilled />,
      content: "确认删除此部门？",
      centered: true,
      okText: "确认",
      cancelText: "取消",
      onOk() {
        department.destroyDepartment(id).then((res: any) => {
          message.success("操作成功");
          resetData();
        });
      },
      onCancel() {
        console.log("Cancel");
      },
    });
  };

  const onDragEnter: TreeProps["onDragEnter"] = (info) => {
    console.log(info);
    // expandedKeys 需要受控时设置
    // setExpandedKeys(info.expandedKeys)
  };

  const onDrop: TreeProps["onDrop"] = (info) => {
    const dropKey = info.node.key;
    const dragKey = info.dragNode.key;
    const dropPos = info.node.pos.split("-");
    const dropPosition =
      info.dropPosition - Number(dropPos[dropPos.length - 1]);

    const loop = (
      data: DataNode[],
      key: React.Key,
      callback: (node: DataNode, i: number, data: DataNode[]) => void
    ) => {
      for (let i = 0; i < data.length; i++) {
        if (data[i].key === key) {
          return callback(data[i], i, data);
        }
        if (data[i].children) {
          loop(data[i].children!, key, callback);
        }
      }
    };
    const data = [...treeData];
    let isTop = false;

    for (let i = 0; i < data.length; i++) {
      if (data[i].key === dragKey) {
        isTop = true;
      }
    }

    // Find dragObject
    let dragObj: DataNode;
    loop(data, dragKey, (item, index, arr) => {
      arr.splice(index, 1);
      dragObj = item;
    });

    if (!info.dropToGap) {
      // Drop on the content
      loop(data, dropKey, (item) => {
        item.children = item.children || [];
        // where to insert 示例添加到头部，可以是随意位置
        item.children.unshift(dragObj);
      });
    } else if (
      ((info.node as any).props.children || []).length > 0 && // Has children
      (info.node as any).props.expanded && // Is expanded
      dropPosition === 1 // On the bottom gap
    ) {
      loop(data, dropKey, (item) => {
        item.children = item.children || [];
        // where to insert 示例添加到头部，可以是随意位置
        item.children.unshift(dragObj);
        // in previous version, we use item.children.push(dragObj) to insert the
        // item to the tail of the children
      });
    } else {
      let ar: DataNode[] = [];
      let i: number;
      loop(data, dropKey, (_item, index, arr) => {
        ar = arr;
        i = index;
      });
      if (dropPosition === -1) {
        ar.splice(i!, 0, dragObj!);
      } else {
        ar.splice(i! + 1, 0, dragObj!);
      }
    }
    setTreeData(data);
    submitDrop(isTop, data, dragKey);
  };

  const submitDrop = (isTop: boolean, data: any, key: any) => {
    let result = checkDropArr(data, key);
    if (result) {
      if (isTop) {
        department.dropSameClass(result.ids).then((res: any) => {
          console.log("ok");
        });
      } else {
        submitChildDrop(key, 0, result);
      }
    }
  };

  const submitChildDrop = (key: any, pid: any, ids: any) => {
    department.dropDiffClass(key, pid, ids.ids).then((res: any) => {
      console.log("ok");
    });
  };

  const checkDropArr = (data: any, key: any) => {
    let ids = [];
    let isSame = false;
    for (let i = 0; i < data.length; i++) {
      ids.push(data[i].key);
      if (data[i].key === key) {
        isSame = true;
      }
      if (data[i].children) {
        let res: any = checkDropArr(data[i].children, key);
        if (res) {
          submitChildDrop(key, data[i].key, res);
        }
      }
    }
    if (isSame) {
      return { key, ids };
    }
  };

  const ldapSync = () => {
    if (loading) {
      message.warning("正在同步，请稍后...");
      return;
    }
    setLoading(true);
    department.ldapSync().then(() => {
      message.success("操作成功");
      setLoading(false);
      resetData();
    });
  };

  return (
    <>
      {
        <div className="playedu-main-top mb-24">
          {contextHolder}
          <div className="d-flex">
            {ldapEnabled ? (
              <PerButton
                type="primary"
                text="一键同步LDAP部门架构"
                class="mr-16"
                icon={null}
                p="department-cud"
                onClick={() => ldapSync()}
                disabled={null}
              />
            ) : (
              <PerButton
                type="primary"
                text="新建部门"
                class="mr-16"
                icon={<PlusOutlined />}
                p="department-cud"
                onClick={() => setCreateVisible(true)}
                disabled={null}
              />
            )}
          </div>
        </div>
      }
      <div className="playedu-main-body">
        {loading && (
          <div className="float-left text-center mt-30">
            <Spin></Spin>
          </div>
        )}
        <div
          className="playedu-tree"
          style={{ display: loading ? "none" : "block", width: 366 }}
        >
          {treeData.length > 0 && (
            <Tree
              onSelect={onSelect}
              treeData={treeData}
              draggable
              blockNode
              onDragEnter={onDragEnter}
              onDrop={onDrop}
              switcherIcon={<i className="iconfont icon-icon-fold c-gray" />}
            />
          )}
        </div>
        <DepartmentCreate
          open={createVisible}
          onCancel={() => {
            setCreateVisible(false);
            setRefresh(!refresh);
          }}
        />
        <DepartmentUpdate
          id={did}
          open={updateVisible}
          onCancel={() => {
            setUpdateVisible(false);
            setRefresh(!refresh);
          }}
        />
      </div>
    </>
  );
};

export default DepartmentPage;
