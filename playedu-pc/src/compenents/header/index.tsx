import React, { useState, useEffect } from "react";
import styles from "./index.module.scss";
import { Modal, Button, Dropdown, Image } from "antd";
import type { MenuProps } from "antd";
import { useDispatch, useSelector } from "react-redux";
import { Link, useNavigate, useLocation } from "react-router-dom";
import {
  logoutAction,
  saveCurrentDepId,
} from "../../store/user/loginUserSlice";
import {
  setDepKey,
  setDepName,
  getDepName,
  clearToken,
} from "../../utils/index";
import { ChangePasswordModel } from "../change-password";
import { UserInfoModel } from "../user-info";
import { ExclamationCircleFilled } from "@ant-design/icons";
import logo from "../../assets/logo.png";
const { confirm } = Modal;

export const Header: React.FC = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();
  const user = useSelector((state: any) => state.loginUser.value.user);
  const departments = useSelector(
    (state: any) => state.loginUser.value.departments
  );
  const config = useSelector((state: any) => state.systemConfig.value);
  const [changePasswordVisiale, setChangePasswordVisiale] =
    useState<boolean>(false);
  const [userInfoVisiale, setUserInfoVisiale] = useState<boolean>(false);
  const [departmentsMenu, setDepartmentsMenu] = useState<any>([]);
  const [currentDepartment, setCurrentDepartment] = useState<string>("");
  const [currentNav, serCurrentNav] = useState(location.pathname);

  useEffect(() => {
    if (departments.length > 0) {
      setCurrentDepartment(getDepName() || departments[0].name);
      const arr: any = [
        {
          key: "1",
          type: "group",
          label: "部门",
          children: [],
        },
      ];
      departments.map((item: any) => {
        arr[0].children.push({
          key: item.id,
          label: item.name,
          disabled: item.name === currentDepartment,
        });
      });
      setDepartmentsMenu(arr);
    }
  }, [departments]);

  useEffect(() => {
    serCurrentNav(location.pathname);
  }, [location.pathname]);

  const onClick: MenuProps["onClick"] = ({ key }) => {
    if (key === "login_out") {
      confirm({
        title: "操作确认",
        icon: <ExclamationCircleFilled />,
        content: "确认退出登录？",
        centered: true,
        okText: "确认",
        cancelText: "取消",
        onOk() {
          dispatch(logoutAction());
          window.location.href = "/login";
        },
        onCancel() {
          console.log("Cancel");
        },
      });
    } else if (key === "change_password") {
      setChangePasswordVisiale(true);
    } else if (key === "user_info") {
      setUserInfoVisiale(true);
    }
  };

  const items: MenuProps["items"] = [
    {
      label: "个人信息",
      key: "user_info",
      icon: (
        <i className="iconfont icon-icon-12 c-red" style={{ fontSize: 16 }} />
      ),
    },
    {
      label: "修改密码",
      key: "change_password",
      icon: (
        <i
          className="iconfont icon-icon-password c-red"
          style={{ fontSize: 16 }}
        />
      ),
    },
    {
      label: "退出登录",
      key: "login_out",
      icon: (
        <i
          className="iconfont icon-a-icon-logout c-red"
          style={{ fontSize: 16 }}
        />
      ),
    },
  ];

  const depItems: MenuProps["items"] = departmentsMenu;

  const onDepClick: MenuProps["onClick"] = ({ key }) => {
    let name: string = "";
    departments.map((item: any) => {
      if (Number(key) === item.id) {
        name = item.name;
      }
    });
    confirm({
      title: "操作确认",
      icon: <ExclamationCircleFilled />,
      content: "确认切换部门？",
      centered: true,
      okText: "确认",
      cancelText: "取消",
      onOk() {
        setCurrentDepartment(name);
        dispatch(saveCurrentDepId(Number(key)));
        setDepKey(key);
        setDepName(name);
        const box = [...departments];
        const arr: any = [
          {
            key: "1",
            type: "group",
            label: "部门",
            children: [],
          },
        ];
        box.map((item: any) => {
          arr[0].children.push({
            key: item.id,
            label: item.name,
            disabled: item.name === name,
          });
        });
        setDepartmentsMenu(arr);
      },
      onCancel() {
        console.log("Cancel");
      },
    });
  };

  const navs = [
    {
      key: "/",
      label: "首页",
    },
    {
      key: "/latest-learn",
      label: "最近学习",
    },
  ];

  return (
    <div className={styles["app-header"]}>
      <div className={styles["main-header"]}>
        <div className="d-flex">
          <Link to="/" className={styles["App-logo"]}>
            {/* 此处为版权标识，严禁删改 */}
            <img src={config.systemLogo || logo} />
          </Link>
          <div className={styles["navs"]}>
            {navs.map((item: any) => (
              <div
                key={item.key}
                className={
                  item.key === currentNav
                    ? styles["nav-active-item"]
                    : styles["nav-item"]
                }
                onClick={() => {
                  serCurrentNav(item.key);
                  navigate(item.key);
                }}
              >
                {item.label}
              </div>
            ))}
          </div>
        </div>
        <div className="d-flex">
          {departments.length === 1 && (
            <div className={styles["department-name"]}>{currentDepartment}</div>
          )}
          {departments.length > 1 && (
            <Dropdown menu={{ items: depItems, onClick: onDepClick }}>
              <div
                className={styles["department-name"]}
                style={{ cursor: "pointer" }}
              >
                {currentDepartment}
              </div>
            </Dropdown>
          )}
          <Button.Group className={styles["button-group"]}>
            <Dropdown menu={{ items, onClick }} placement="bottomRight">
              <div className="d-flex" style={{ cursor: "pointer" }}>
                {user && user.name && (
                  <>
                    <Image
                      loading="lazy"
                      style={{ width: 36, height: 36, borderRadius: "50%" }}
                      src={user.avatar}
                      preview={false}
                    />
                    <span className="ml-8 c-admin">{user.name}</span>
                  </>
                )}
              </div>
            </Dropdown>
          </Button.Group>
          <ChangePasswordModel
            open={changePasswordVisiale}
            onCancel={() => {
              setChangePasswordVisiale(false);
            }}
          ></ChangePasswordModel>
          <UserInfoModel
            open={userInfoVisiale}
            onCancel={() => {
              setUserInfoVisiale(false);
            }}
          ></UserInfoModel>
        </div>
      </div>
    </div>
  );
};
