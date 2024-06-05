import React from "react";
import styles from "./index.module.scss";
import { useSelector } from "react-redux";

export const Footer: React.FC = () => {
  const systemConfig = useSelector((state: any) => state.systemConfig.value);

  return (
    <div className={styles["footer-box"]}>{systemConfig.pcIndexFooterMsg}</div>
  );
};
