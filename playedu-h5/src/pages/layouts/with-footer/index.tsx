import { Suspense } from "react";
import { Outlet } from "react-router-dom";
import LoadingPage from "../../loading";
import { TabBarFooter } from "../../../components";
import styles from "./index.module.scss";

const WithoutHeaderWithoutFooter = () => {
  return (
    <div className={styles["playedu-app"]}>
      <Suspense fallback={<LoadingPage />}>
        <Outlet />
      </Suspense>
      <TabBarFooter></TabBarFooter>
    </div>
  );
};

export default WithoutHeaderWithoutFooter;
