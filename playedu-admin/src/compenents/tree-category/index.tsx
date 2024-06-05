import { Tree } from "antd";
import { useState, useEffect } from "react";
import { useSelector } from "react-redux";

interface Option {
  key: string | number;
  title: any;
  children?: Option[];
}

interface PropInterface {
  type: string;
  text: string;
  selected: any;
  onUpdate: (keys: any, title: any) => void;
}

export const TreeCategory = (props: PropInterface) => {
  const [treeData, setTreeData] = useState<any>([]);
  const [selectKey, setSelectKey] = useState<number[]>([]);
  const resourceCategories = useSelector(
    (state: any) => state.systemConfig.value.resourceCategories
  );

  useEffect(() => {
    if (props.selected && props.selected.length > 0) {
      setSelectKey(props.selected);
    }
  }, [props.selected]);

  useEffect(() => {
    if (JSON.stringify(resourceCategories) !== "{}") {
      const new_arr: Option[] = checkArr(resourceCategories, 0);
      if (props.type === "no-cate") {
        new_arr.unshift({
          key: 0,
          title: <span className="tree-title-elli">未分类</span>,
        });
      }
      setTreeData(new_arr);
    }
  }, [resourceCategories]);

  const checkArr = (categories: CategoriesBoxModel, id: number) => {
    const arr = [];
    for (let i = 0; i < categories[id].length; i++) {
      if (!categories[categories[id][i].id]) {
        let name = (
          <span className="tree-title-elli">{categories[id][i].name}</span>
        );
        arr.push({
          title: name,
          key: categories[id][i].id,
        });
      } else {
        let name = (
          <span className="tree-title-elli">{categories[id][i].name}</span>
        );
        const new_arr: Option[] = checkArr(categories, categories[id][i].id);
        arr.push({
          title: name,
          key: categories[id][i].id,
          children: new_arr,
        });
      }
    }
    return arr;
  };

  const onSelect = (selectedKeys: any, info: any) => {
    let label = "全部" + props.text;
    if (info) {
      label = info.node.title.props.children;
    }
    props.onUpdate(selectedKeys, label);
    setSelectKey(selectedKeys);
  };

  const onExpand = (selectedKeys: any, info: any) => {};

  return (
    <div className="playedu-tree">
      <div
        className={
          selectKey.length === 0
            ? "mb-8 category-label active"
            : "mb-8 category-label"
        }
        onClick={() => {
          onSelect([], "");
        }}
      >
        <div className="j-b-flex">
          <span>全部{props.text}</span>
        </div>
      </div>
      {treeData.length > 0 && (
        <Tree
          onSelect={onSelect}
          selectedKeys={selectKey}
          onExpand={onExpand}
          treeData={treeData}
          // defaultExpandAll={true}
          switcherIcon={<i className="iconfont icon-icon-fold c-gray" />}
        />
      )}
    </div>
  );
};
