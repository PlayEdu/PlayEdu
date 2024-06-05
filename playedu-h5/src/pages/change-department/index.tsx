import { useState, useEffect } from "react";
import { Radio, Image, Toast } from "antd-mobile";
import styles from "./index.module.scss";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { saveCurrentDepId } from "../../store/user/loginUserSlice";
import { setDepKey, setDepName } from "../../utils/index";
import backIcon from "../../assets/images/commen/icon-back.png";

const ChangeDepartmentPage = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const departments = useSelector(
    (state: any) => state.loginUser.value.departments
  );
  const currentDepId = useSelector(
    (state: any) => state.loginUser.value.currentDepId
  );

  useEffect(() => {
    document.title = "切换部门";
  }, []);

  const onDepClick = (value: any) => {
    let it = departments.find((o: any) => o.id === value);
    if (it) {
      dispatch(saveCurrentDepId(Number(value)));
      setDepKey(value);
      setDepName(it.name);
      Toast.show({
        content: "部门切换成功",
      });
      navigate("/member", { replace: true });
    }
  };

  return (
    <div className="main-body">
      <div className="main-header">
        <Image
          className="back-icon"
          src={backIcon}
          onClick={() => navigate(-1)}
        />
        <div className="main-title">切换部门</div>
      </div>
      <div className={styles["info"]}>点击部门名称切换部门</div>
      <div className={styles["radio-box"]}>
        <Radio.Group onChange={onDepClick} defaultValue={currentDepId}>
          {departments.map((item: any) => (
            <Radio
              key={item.id}
              value={item.id}
              block
              className={styles["radio-item"]}
            >
              {item.name}
            </Radio>
          ))}
        </Radio.Group>
      </div>
    </div>
  );
};

export default ChangeDepartmentPage;
