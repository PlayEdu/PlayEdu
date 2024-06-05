import { Outlet } from "react-router-dom";
import { Footer } from "../../../compenents";
import { Suspense } from "react";
import LoadingPage from "../../loading";

const WithoutHeaderWithFooter = () => {
  return (
    <div className="layout-box">
      <Suspense fallback={<LoadingPage height="100vh" />}>
        <Outlet />
      </Suspense>
      <div className="footer-box">
        <Footer></Footer>
      </div>
    </div>
  );
};

export default WithoutHeaderWithFooter;
