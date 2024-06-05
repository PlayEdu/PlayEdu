import { Spin } from "antd";
import styles from "./index.module.less";

interface PropsInterface {
  height?: string;
}

const LoadingPage = (props: PropsInterface) => {
  return (
    <>
      <div
        className={styles["loading-box"]}
        style={{ height: props.height || "100vh" }}
      >
        <Spin size="large" />
      </div>
    </>
  );
};

export default LoadingPage;
