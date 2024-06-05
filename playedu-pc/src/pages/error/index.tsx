import { useState } from "react";
import { Button, Result } from "antd";
import { useNavigate, useLocation } from "react-router-dom";
import styles from "./index.module.scss";

const ErrorPage = () => {
  const navigate = useNavigate();
  const result = new URLSearchParams(useLocation().search);
  const [status, setStatus] = useState(String(result.get("status") || "404"));

  return (
    <Result
      status="404"
      title={status}
      subTitle="您访问的页面不存在"
      className={styles["main"]}
      extra={
        <Button
          type="primary"
          onClick={() => {
            navigate("/", { replace: true });
          }}
        >
          返回首页
        </Button>
      }
    />
  );
};

export default ErrorPage;
