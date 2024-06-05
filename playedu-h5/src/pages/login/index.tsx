import { useState, useEffect } from "react";
import { Button, Toast, Input, Image } from "antd-mobile";
import styles from "./index.module.scss";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { login, system, user } from "../../api/index";
import { setToken } from "../../utils/index";
import { loginAction } from "../../store/user/loginUserSlice";
import {
  SystemConfigStoreInterface,
  saveConfigAction,
} from "../../store/system/systemConfigSlice";
import banner from "../../assets/images/login/banner.png";

const LoginPage = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [bodyHeight, setBodyHeight] = useState(0);
  const systemConfig = useSelector((state: any) => state.systemConfig.value);

  useEffect(() => {
    document.title = "登录";
    let value = document.documentElement.clientHeight;
    setBodyHeight(value);
  }, []);

  const loginSubmit = async (e: any) => {
    if (!email) {
      Toast.show({
        content: "请输入邮箱或UID",
      });
      return;
    }
    if (!password) {
      Toast.show({
        content: "请输入密码",
      });
      return;
    }
    await handleSubmit();
  };

  const handleSubmit = async () => {
    if (loading) {
      return;
    }
    setLoading(true);
    if (systemConfig["ldap-enabled"] === "1") {
      try {
        let res: any = await login.loginLdap(email, password);
        setToken(res.data.token); //将token写入本地
        await getSystemConfig(); //获取系统配置并写入store
        await getUser(); //获取登录用户的信息并写入store
        setLoading(false);
        navigate("/member", { replace: true });
      } catch (e) {
        console.error("错误信息", e);
        setLoading(false);
      }
    }else{
      try {
        let res: any = await login.login(email, password);
        setToken(res.data.token); //将token写入本地
        await getSystemConfig(); //获取系统配置并写入store
        await getUser(); //获取登录用户的信息并写入store
        setLoading(false);
        navigate("/member", { replace: true });
      } catch (e) {
        console.error("错误信息", e);
        setLoading(false);
      }
    }
    
  };

  const getUser = async () => {
    let res: any = await user.detail();
    dispatch(loginAction(res.data));
  };

  const getSystemConfig = async () => {
    let configRes: any = await system.config();
    if (configRes.data) {
      let config: SystemConfigStoreInterface = {
        //系统配置
        "ldap-enabled": configRes.data["ldap-enabled"],
        systemApiUrl: configRes.data["system-api-url"],
        systemH5Url: configRes.data["system-h5-url"],
        systemLogo: configRes.data["system-logo"],
        systemName: configRes.data["system-name"],
        systemPcUrl: configRes.data["system-pc-url"],
        pcIndexFooterMsg: configRes.data["system-pc-index-footer-msg"],
        //播放器配置
        playerPoster: configRes.data["player-poster"],
        playerIsEnabledBulletSecret:
          configRes.data["player-is-enabled-bullet-secret"] &&
          configRes.data["player-is-enabled-bullet-secret"] === "1"
            ? true
            : false,
        playerIsDisabledDrag:
          configRes.data["player-disabled-drag"] &&
          configRes.data["player-disabled-drag"] === "1"
            ? true
            : false,
        playerBulletSecretText: configRes.data["player-bullet-secret-text"],
        playerBulletSecretColor: configRes.data["player-bullet-secret-color"],
        playerBulletSecretOpacity:
          configRes.data["player-bullet-secret-opacity"],
      };
      dispatch(saveConfigAction(config));
    }
  };

  return (
    <div
      className={styles["login-content"]}
      style={{ height: bodyHeight + "px" }}
    >
      <div className={styles["top-content"]}>
        <div className={styles["title"]}>学员登录</div>
        <Image src={banner} width={150} height={150} />
      </div>
      <div className={styles["form-box"]}>
        <div className={styles["input-box"]}>
          <Input
            className={styles["input-item"]}
            placeholder="请输入邮箱或UID"
            value={email}
            onChange={(val) => {
              setEmail(val);
            }}
          />
          <div className={styles["line"]}></div>
          <Input
            type="password"
            className={styles["input-item"]}
            placeholder="请输入密码"
            value={password}
            onChange={(val) => {
              setPassword(val);
            }}
          />
        </div>
        <div className={styles["button-box"]}>
          <Button
            className={styles["primary-button"]}
            disabled={email === "" || password === ""}
            color="primary"
            loading={loading}
            onClick={loginSubmit}
          >
            登 录
          </Button>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
