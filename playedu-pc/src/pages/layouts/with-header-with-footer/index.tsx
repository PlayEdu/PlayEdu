import { Outlet } from "react-router-dom";
import { Footer, Header } from "../../../compenents";
import { Suspense } from "react";
import LoadingPage from "../../loading";

const WithHeaderWithFooter = () => {
  return (
    <div className="layout-box">
      <Header></Header>
      <Suspense fallback={<LoadingPage height="100vh" />}>
        <Outlet />
      </Suspense>
      <div className="footer-box">
        <Footer></Footer>
      </div>
    </div>
  );
};

export default WithHeaderWithFooter;
