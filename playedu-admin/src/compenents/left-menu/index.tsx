import React, { useEffect, useState } from "react";
import { Menu } from "antd";
import { useSelector } from "react-redux";
import { useNavigate, useLocation } from "react-router-dom";
import styles from "./index.module.less";
import logo from "../../assets/logo.png";

function getItem(
  label: any,
  key: any,
  icon: any,
  children: any,
  type: any,
  permission: any
) {
  return {
    key,
    icon,
    children,
    label,
    type,
    permission,
  };
}
const items = [
  getItem(
    "首页概览",
    "/",
    <i className={`iconfont icon-icon-home`} />,
    null,
    null,
    null
  ),
  getItem(
    "分类管理",
    "/resource-category",
    <i className="iconfont icon-icon-category" />,
    null,
    null,
    "resource-category-menu"
  ),
  getItem(
    "资源管理",
    "resource",
    <i className="iconfont icon-icon-file" />,
    [
      getItem("视频", "/videos", null, null, null, "resource-menu"),
      getItem("图片", "/images", null, null, null, "resource-menu"),
      getItem("课件", "/courseware", null, null, null, "resource-menu"),
    ],
    null,
    null
  ),
  getItem(
    "课程中心",
    "courses",
    <i className="iconfont icon-icon-study" />,
    [getItem("线上课", "/course", null, null, null, "course")],
    null,
    null
  ),
  getItem(
    "学员管理",
    "user",
    <i className="iconfont icon-icon-user" />,
    [
      getItem("学员", "/member/index", null, null, null, "user-index"),
      getItem("部门", "/department", null, null, null, "department-cud"),
    ],
    null,
    null
  ),
  getItem(
    "系统设置",
    "system",
    <i className="iconfont icon-icon-setting" />,
    [
      getItem(
        "系统配置",
        "/system/config/index",
        null,
        null,
        null,
        "system-config"
      ),
      getItem(
        "管理人员",
        "/system/administrator",
        null,
        null,
        null,
        "admin-user-index"
      ),
      getItem("管理日志", "/system/adminlog", null, null, null, "admin-log"),
    ],
    null,
    null
  ),
  getItem(
    "使用许可",
    "/licensing",
    <i className="iconfont icon-xuke" />,
    null,
    null,
    null,
  ),
];

export const LeftMenu: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const children2Parent: any = {
    "^/video": ["resource"],
    "^/image": ["resource"],
    "^/courseware": ["resource"],
    "^/member": ["user"],
    "^/department": ["user"],
    "^/course": ["courses"],
    "^/system": ["system"],
  };

  const hit = (pathname: string): string[] => {
    for (let p in children2Parent) {
      if (pathname.search(p) >= 0) {
        return children2Parent[p];
      }
    }
    return [];
  };

  const openKeyMerge = (pathname: string): string[] => {
    let newOpenKeys = hit(pathname);
    for (let i = 0; i < openKeys.length; i++) {
      let isIn = false;
      for (let j = 0; j < newOpenKeys.length; j++) {
        if (newOpenKeys[j] === openKeys[i]) {
          isIn = true;
          break;
        }
      }
      if (isIn) {
        continue;
      }
      newOpenKeys.push(openKeys[i]);
    }
    return newOpenKeys;
  };

  // 选中的菜单
  const [selectedKeys, setSelectedKeys] = useState<string[]>([
    location.pathname,
  ]);
  // 展开菜单
  const [openKeys, setOpenKeys] = useState<string[]>(hit(location.pathname));
  const permissions = useSelector(
    (state: any) => state.loginUser.value.permissions
  );
  const [activeMenus, setActiveMenus] = useState<any>([]);

  const onClick = (e: any) => {
    navigate(e.key);
  };

  useEffect(() => {
    checkMenuPermissions(items, permissions);
  }, [items, permissions]);

  const checkMenuPermissions = (items: any, permissions: any) => {
    let menus: any = [];
    if (permissions.length === 0) {
      setActiveMenus(menus);
      return;
    }

    for (let i in items) {
      let menuItem = items[i];
      // 一级菜单=>没有子菜单&配置了权限
      if (menuItem.children === null) {
        if (
          menuItem.permission !== null &&
          typeof permissions[menuItem.permission] === "undefined"
        ) {
          continue;
        }
        menus.push(menuItem);
        continue;
      }
      let children = [];

      for (let j in menuItem.children) {
        let childrenItem = menuItem.children[j];

        if (
          typeof permissions[childrenItem.permission] !== "undefined" ||
          !childrenItem.permission
        ) {
          // 存在权限
          children.push(childrenItem);
        }
      }

      if (children.length > 0) {
        menus.push(Object.assign({}, menuItem, { children: children }));
      }
    }
    setActiveMenus(menus);
  };

  useEffect(() => {
    if (location.pathname.indexOf("/course/user") !== -1) {
      setSelectedKeys(["/course"]);
      setOpenKeys(openKeyMerge("/course"));
    } else if (location.pathname.indexOf("/member/learn") !== -1) {
      setSelectedKeys(["/member/index"]);
      setOpenKeys(openKeyMerge("/member/index"));
    } else {
      setSelectedKeys([location.pathname]);
      setOpenKeys(openKeyMerge(location.pathname));
    }
  }, [location.pathname]);

  return (
    <div className={styles["left-menu"]}>
      <div
        style={{
          textDecoration: "none",
          cursor: "pointer",
          position: "sticky",
          top: 0,
          zIndex: 10,
          background: "#fff",
        }}
        onClick={() => {
          window.location.href = "/";
        }}
      >
        {/* 此处为版权标识，严禁删改 */}
        <img src={logo} className={styles["App-logo"]} />
      </div>
      <div className={styles["menu-box"]}>
        <Menu
          onClick={onClick}
          style={{
            width: 200,
            background: "#ffffff",
          }}
          selectedKeys={selectedKeys}
          openKeys={openKeys}
          mode="inline"
          items={activeMenus}
          onSelect={(data: any) => {
            setSelectedKeys(data.selectedKeys);
          }}
          onOpenChange={(keys: any) => {
            setOpenKeys(keys);
          }}
        />
      </div>
    </div>
  );
};
