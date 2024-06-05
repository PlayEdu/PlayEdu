import React from "react";
import styles from "./index.module.less";
import { Button, Dropdown, MenuProps } from "antd";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import avatar from "../../assets/images/commen/avatar.png";
import { logoutAction } from "../../store/user/loginUserSlice";
import { clearToken } from "../../utils/index";

export const Header: React.FC = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector((state: any) => state.loginUser.value.user);

  const onClick: MenuProps["onClick"] = ({ key }) => {
    if (key === "login_out") {
      clearToken();
      dispatch(logoutAction());
      navigate("/login");
    } else if (key === "change_password") {
      navigate("/change-password");
    }
  };

  const items: MenuProps["items"] = [
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
  return (
    <div className={styles["app-header"]}>
      <div className={styles["main-header"]}>
        <div></div>
        <Button.Group className={styles["button-group"]}>
          <Dropdown menu={{ items, onClick }} placement="bottomRight">
            <div className="d-flex">
              {user.name && (
                <img style={{ width: 30, height: 30 }} src={avatar} />
              )}
              <span className="ml-8 c-admin">{user.name}</span>
            </div>
          </Dropdown>
        </Button.Group>
      </div>
    </div>
  );
};
