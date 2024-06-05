import { useState, useEffect } from "react";
import { Button, Toast, Input, Image } from "antd-mobile";
import styles from "./index.module.scss";
import { useNavigate } from "react-router-dom";
import { user } from "../../api/index";
import backIcon from "../../assets/images/commen/icon-back.png";

const ChangePasswordPage = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [againPassword, setAgainPassword] = useState("");

  useEffect(() => {
    document.title = "修改密码";
  }, []);

  const onFinish = () => {
    if (!oldPassword) {
      Toast.show({
        content: "请输入原密码",
      });
      return;
    }
    if (!newPassword) {
      Toast.show({
        content: "请输入新密码",
      });
      return;
    }
    if (!againPassword) {
      Toast.show({
        content: "再次输入新密码",
      });
      return;
    }
    if (againPassword !== newPassword) {
      Toast.show({
        content: "再次输入的新密码错误",
      });
      return;
    }
    if (loading) {
      return;
    }
    handleSubmit();
  };

  const handleSubmit = () => {
    if (loading) {
      return;
    }
    setLoading(true);
    user
      .password(oldPassword, newPassword)
      .then((res: any) => {
        Toast.show({
          content: "修改密码成功",
        });
        navigate(-1);
      })
      .catch((e) => {
        setLoading(false);
      });
  };

  return (
    <div className="main-body">
      <div className="main-header">
        <Image
          className="back-icon"
          src={backIcon}
          onClick={() => navigate(-1)}
        />
        <div className="main-title">修改密码</div>
      </div>
      <div className={styles["form-box"]}>
        <div className={styles["input-box"]}>
          <div className={styles["input-box"]}>
            <Input
              type="password"
              className={styles["input-item"]}
              placeholder="请输入原密码"
              value={oldPassword}
              onChange={(val) => {
                setOldPassword(val);
              }}
            />
            <div className={styles["line"]}></div>
            <Input
              type="password"
              className={styles["input-item"]}
              placeholder="请输入新密码"
              value={newPassword}
              onChange={(val) => {
                setNewPassword(val);
              }}
            />
          </div>
        </div>
        <div className={styles["input2-box"]}>
          <Input
            type="password"
            className={styles["input-item"]}
            placeholder="请再次输入新密码"
            value={againPassword}
            onChange={(val) => {
              setAgainPassword(val);
            }}
          />
        </div>
        <div className={styles["button-box"]}>
          <Button
            className={styles["primary-button"]}
            disabled={
              oldPassword === "" || newPassword === "" || againPassword === ""
            }
            color="primary"
            loading={loading}
            onClick={onFinish}
          >
            确认修改
          </Button>
        </div>
      </div>
    </div>
  );
};

export default ChangePasswordPage;
