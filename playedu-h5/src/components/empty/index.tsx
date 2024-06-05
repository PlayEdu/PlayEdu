import styles from "./index.module.scss";
import React from "react";
import { Image } from "antd-mobile";
import empty from "../../assets/images/commen/empty.png";

export const Empty: React.FC = () => {
  return (
    <div className={styles["img-box"]}>
      <Image src={empty} width={250} height={250} style={{ marginTop: 100 }} />
    </div>
  );
};
