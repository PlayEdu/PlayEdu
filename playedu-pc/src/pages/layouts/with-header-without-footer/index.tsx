import { Outlet } from "react-router-dom";
import { NoHeader } from "../../../compenents";
import { Suspense } from "react";
import LoadingPage from "../../loading";

const WithHeaderWithoutFooter = () => {
  return (
    <>
      <NoHeader></NoHeader>
      <Suspense fallback={<LoadingPage height="100vh" />}>
        <Outlet />
      </Suspense>
    </>
  );
};

export default WithHeaderWithoutFooter;
