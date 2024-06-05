import React from "react";
import { getToken } from "../../utils/index";
import { Navigate } from "react-router-dom";

interface PropInterface {
  Component: any;
}

const PrivateRoute: React.FC<PropInterface> = ({ Component }) => {
  return getToken() ? Component : <Navigate to="/login" replace={true} />;
};
export default PrivateRoute;
