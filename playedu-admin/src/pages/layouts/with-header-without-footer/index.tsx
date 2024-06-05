import { useEffect } from "react";
import styles from "./index.module.less";
import { Outlet } from "react-router-dom";
import { Header, LeftMenu } from "../../../compenents";
import { Suspense } from "react";
import LoadingPage from "../../loading";

const HomePage = () => {
  useEffect(() => {}, []);

  return (
    <>
      <div className={styles["layout-wrap"]}>
        <div className={styles["left-menu"]}>
          <LeftMenu />
        </div>
        <div className={styles["right-cont"]}>
          <div className={styles["right-top"]}>
            <Header></Header>
          </div>
          <div className={styles["right-main"]}>
            <Suspense fallback={<LoadingPage height="100vh" />}>
              {/* 二级路由出口 */}
              <Outlet />{" "}
            </Suspense>
          </div>
        </div>
      </div>
    </>
  );
};

export default HomePage;
