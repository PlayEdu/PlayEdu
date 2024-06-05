import { Input, Button, message } from "antd";
import React, { useState } from "react";
import styles from "./index.module.scss";
import banner from "../../assets/images/login/banner.png";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { loginAction } from "../../store/user/loginUserSlice";
import { login, user } from "../../api/index";
import { setToken } from "../../utils/index";
import { NoFooter } from "../../compenents";

const LoginPage: React.FC = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [loading, setLoading] = useState<boolean>(false);
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const systemConfig = useSelector((state: any) => state.systemConfig.value);

  const loginSubmit = (e: any) => {
    if (!email) {
      message.error("请输入邮箱或UID");
      return;
    }
    if (!password) {
      message.error("请输入密码");
      return;
    }
    if (loading) {
      return;
    }
    handleSubmit();
  };

  const keyUp = (e: any) => {
    if (e.keyCode === 13) {
      loginSubmit(e);
    }
  };

  const handleSubmit = () => {
    if (loading) {
      return;
    }
    setLoading(true);
    if (systemConfig["ldap-enabled"] === "1") {
      login
        .loginLdap(email, password)
        .then((res: any) => {
          const token = res.data.token;
          setToken(token);
          getUser();
        })
        .catch((e) => {
          setLoading(false);
        });
    } else {
      login
        .login(email, password)
        .then((res: any) => {
          const token = res.data.token;
          setToken(token);
          getUser();
        })
        .catch((e) => {
          setLoading(false);
        });
    }
  };

  const getUser = () => {
    user.detail().then((res: any) => {
      const data = res.data;
      dispatch(loginAction(data));
      setLoading(false);
      navigate("/", { replace: true });
    });
  };

  return (
    <div className={styles["login-content"]}>
      <div className={styles["top-content"]}>
        <div className={styles["title"]}>学员登录</div>
        <div className={styles["login-box"]}>
          <div className={styles["left-box"]}>
            <img className={styles["icon"]} src={banner} alt="" />
          </div>
          <div className={styles["right-box"]}>
            <div className="login-box d-flex">
              <Input
                value={email}
                onChange={(e) => {
                  setEmail(e.target.value);
                }}
                style={{ width: 400, height: 54 }}
                placeholder={"请输入邮箱或UID"}
                onKeyUp={(e) => keyUp(e)}
              />
            </div>
            <div className="login-box d-flex mt-50">
              <Input.Password
                value={password}
                onChange={(e) => {
                  setPassword(e.target.value);
                }}
                style={{ width: 400, height: 54 }}
                placeholder="请输入密码"
                onKeyUp={(e) => keyUp(e)}
              />
            </div>
            <div className="login-box d-flex mt-50">
              <Button
                style={{ width: 400, height: 54 }}
                type="primary"
                onClick={loginSubmit}
                loading={loading}
              >
                立即登录
              </Button>
            </div>
          </div>
        </div>
      </div>
      <div className={styles["footer"]}>
        <NoFooter></NoFooter>
      </div>
    </div>
  );
};

export default LoginPage;
