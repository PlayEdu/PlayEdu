import styles from "./index.module.scss";
import React from "react";
import { Image } from "antd";
import empty from "../../assets/images/commen/empty.png";

export const Empty: React.FC = () => {
  return (
    <div className={styles["img-box"]}>
      <Image src={empty} width={400} height={400} preview={false} />
    </div>
  );
};
