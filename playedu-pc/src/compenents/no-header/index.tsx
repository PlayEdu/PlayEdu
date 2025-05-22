import React from "react";
import styles from "./index.module.scss";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { Image } from "antd";
import logo from "../../assets/logo.png";

export const NoHeader: React.FC = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const config = useSelector((state: any) => state.systemConfig.value);

  return (
    <div className={styles["app-header"]}>
      <div className={styles["main-header"]}>
        <img
          src={config.resourceUrl[config.systemLogo] || logo}
          className={styles["App-logo"]}
        />
      </div>
    </div>
  );
};
