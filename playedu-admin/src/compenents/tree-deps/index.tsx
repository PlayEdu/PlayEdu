import { Spin, Tree, Checkbox } from "antd";
import { useState, useEffect } from "react";
import { department } from "../../api/index";
import type { TreeProps } from "antd/es/tree";
import type { CheckboxChangeEvent } from "antd/es/checkbox";

interface Option {
  key: string | number;
  title: string;
  children?: Option[];
}

interface PropInterface {
  type: string;
  refresh: boolean;
  showNum: boolean;
  selected: any;
  onUpdate: (keys: any, nodes: any) => void;
}

export const TreeDeps = (props: PropInterface) => {
  const [treeData, setTreeData] = useState<any>([]);
  const [loading, setLoading] = useState(true);
  const [selectKeys, setSelectKeys] = useState<number[]>([]);
  const [checkKeys, setCheckKeys] = useState<number[]>([]);
  const [userTotal, setUserTotal] = useState(0);
  const [showLeafIcon, setShowLeafIcon] = useState<boolean | React.ReactNode>(
    true
  );
  const [expandKeys, setExpandKeys] = useState<number[]>([]);
  const [totalKeys, setTotalKeys] = useState<number[]>([]);

  useEffect(() => {
    setSelectKeys(props.selected);
    setCheckKeys(props.selected);
  }, [props.selected]);

  useEffect(() => {
    setLoading(true);
    department.departmentList({}).then((res: any) => {
      const departments: DepartmentsBoxModel = res.data.departments;
      const departCount: DepIdsModel = res.data.dep_user_count;
      setUserTotal(res.data.user_total);
      if (JSON.stringify(departments) !== "{}") {
        if (props.showNum) {
          const new_arr: any[] = checkNewArr(departments, 0, departCount);
          setTreeData(new_arr);
          const topLevelParentKeyList = new_arr.map((node: any) => node.key);
          setTotalKeys(topLevelParentKeyList);
        } else {
          const new_arr: any[] = checkArr(departments, 0);
          setTreeData(new_arr);
          const topLevelParentKeyList = new_arr.map((node: any) => node.key);
          setTotalKeys(topLevelParentKeyList);
        }
      } else {
        const new_arr: Option[] = [];
        setTreeData(new_arr);
        setTotalKeys([]);
      }
      setLoading(false);
    });
  }, [props.refresh]);

  const checkNewArr = (
    departments: DepartmentsBoxModel,
    id: number,
    counts: any
  ) => {
    const arr = [];
    for (let i = 0; i < departments[id].length; i++) {
      if (!departments[departments[id][i].id]) {
        arr.push({
          title: getNewTitle(
            departments[id][i].name,
            departments[id][i].id,
            counts
          ),
          key: departments[id][i].id,
        });
      } else {
        const new_arr: any = checkNewArr(
          departments,
          departments[id][i].id,
          counts
        );
        arr.push({
          title: getNewTitle(
            departments[id][i].name,
            departments[id][i].id,
            counts
          ),
          key: departments[id][i].id,
          children: new_arr,
        });
      }
    }
    return arr;
  };

  const checkArr = (departments: DepartmentsBoxModel, id: number) => {
    const arr = [];
    for (let i = 0; i < departments[id].length; i++) {
      if (!departments[departments[id][i].id]) {
        arr.push({
          title: (
            <span className="tree-title-elli">{departments[id][i].name}</span>
          ),
          key: departments[id][i].id,
        });
      } else {
        const new_arr: any[] = checkArr(departments, departments[id][i].id);
        arr.push({
          title: (
            <span className="tree-title-elli">{departments[id][i].name}</span>
          ),
          key: departments[id][i].id,
          children: new_arr,
        });
      }
    }
    return arr;
  };

  const getNewTitle = (title: any, id: number, counts: any) => {
    if (counts) {
      let value = counts[id] || 0;
      return (
        <span className="tree-title-elli">{title + "(" + value + ")"}</span>
      );
    } else {
      return <span className="tree-title-elli">{title}</span>;
    }
  };

  const onSelect: TreeProps["onSelect"] = (selectedKeys: any, info: any) => {
    console.log("onCheck", selectedKeys, info);
    let nodes = [];
    if (info) {
      nodes = info.selectedNodes;
    }
    props.onUpdate(selectedKeys, nodes);
    setSelectKeys(selectedKeys);
  };

  const onExpand = (expandedKeys: any, info: any) => {
    console.log("onExpand", expandedKeys, info);
    if (checkKeys.includes(info.node.key)) {
      // 关闭该节点的展开
      return;
    }
    setExpandKeys(expandedKeys);
  };

  const onCheck: TreeProps["onCheck"] = (checkedKeys: any, info: any) => {
    console.log("onCheck", checkedKeys, info);
    let nodes = [];
    if (info) {
      nodes = info.checkedNodes;
    }
    props.onUpdate(checkedKeys.checked, nodes);
    setCheckKeys(checkedKeys.checked);
    if (info.checked && info.node.children) {
      setExpandKeys(expandKeys.filter((key) => key !== info.node.key)); // 关闭父节点的展开
    }
  };

  const onChange = (e: CheckboxChangeEvent) => {
    console.log(`checked = ${e.target.checked}`);
    if (e.target.checked) {
      const topLevelParentKeyList = treeData.map((node: any) => node.key);
      const topLevelParentNodes = treeData.map((node: any) => node);
      // 设置最外层父级节点为勾选状态
      if (props.type === "single") {
        console.log("全选");
        setSelectKeys([]);
        setExpandKeys([]);
      } else {
        setCheckKeys(topLevelParentKeyList);
        setExpandKeys([]);
      }
      props.onUpdate(topLevelParentKeyList, topLevelParentNodes);
    } else {
      setSelectKeys([]);
      setExpandKeys([]);
      setCheckKeys([]);
      props.onUpdate([], []);
    }
  };

  return (
    <>
      {loading && (
        <div className="text-center mt-30">
          <Spin></Spin>
        </div>
      )}
      <div style={{ display: loading ? "none" : "block" }}>
        {treeData.length > 0 && (
          <>
            {props.type === "single" ? (
              <div className="playedu-old-tree">
                <div style={{ height: 40 }} className="d-flex">
                  <Checkbox onChange={onChange}>全选</Checkbox>
                </div>
                <Tree
                  onSelect={onSelect}
                  treeData={treeData}
                  onExpand={(expandedKeys: any, info: any) => {
                    setExpandKeys(expandedKeys);
                  }}
                  selectedKeys={selectKeys}
                  expandedKeys={expandKeys}
                  showLine={{ showLeafIcon }}
                />
              </div>
            ) : (
              <div className="playedu-old-tree">
                <div style={{ height: 40 }} className="d-flex">
                  <Checkbox
                    checked={
                      checkKeys.toString() === totalKeys.toString()
                        ? true
                        : false
                    }
                    onChange={onChange}
                  >
                    全选
                  </Checkbox>
                </div>
                <Tree
                  selectable={false}
                  checkable
                  checkStrictly={true}
                  checkedKeys={checkKeys}
                  multiple={true}
                  onExpand={onExpand}
                  expandedKeys={expandKeys}
                  showLine={{ showLeafIcon }}
                  onCheck={onCheck}
                  treeData={treeData}
                />
              </div>
            )}
          </>
        )}
      </div>
    </>
  );
};
