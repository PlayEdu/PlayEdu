import React from "react";
import { TabBar } from "antd-mobile";
import styles from "./index.module.scss";
import { useNavigate, useLocation } from "react-router-dom";

export const TabBarFooter: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { pathname } = location;

  const tabs = [
    {
      key: "/",
      title: "首页",
      icon: (active: boolean) =>
        active ? (
          <i
            style={{ fontSize: 28, color: "#FF4D4F" }}
            className="iconfont icon-icon-shouye"
          ></i>
        ) : (
          <i
            style={{ fontSize: 28, color: "#cccccc" }}
            className="iconfont icon-icon-shouye"
          ></i>
        ),
    },
    {
      key: "/study",
      title: "学习",
      icon: (active: boolean) =>
        active ? (
          <i
            style={{ fontSize: 28, color: "#FF4D4F" }}
            className="iconfont icon-icon-xuexi"
          ></i>
        ) : (
          <i
            style={{ fontSize: 28, color: "#cccccc" }}
            className="iconfont icon-icon-xuexi"
          ></i>
        ),
    },
    {
      key: "/member",
      title: "我的",
      icon: (active: boolean) =>
        active ? (
          <i
            style={{ fontSize: 28, color: "#FF4D4F" }}
            className="iconfont icon-icon-wode"
          ></i>
        ) : (
          <i
            style={{ fontSize: 28, color: "#cccccc" }}
            className="iconfont icon-icon-wode"
          ></i>
        ),
    },
  ];

  return (
    <div className={styles["footer"]}>
      <TabBar
        activeKey={pathname}
        onChange={(value) => navigate(value, { replace: true })}
      >
        {tabs.map((item) => (
          <TabBar.Item key={item.key} icon={item.icon} title={item.title} />
        ))}
      </TabBar>
    </div>
  );
};
