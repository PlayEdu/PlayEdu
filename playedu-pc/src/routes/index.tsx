import { lazy } from "react";
import { RouteObject } from "react-router-dom";
import { system, user } from "../api";
import { getToken } from "../utils";
// 页面加载
import { InitPage } from "../pages/init";
import LoginPage from "../pages/login";
import WithHeaderWithFooter from "../pages/layouts/with-header-with-footer";
import WithHeaderWithoutFooter from "../pages/layouts/with-header-without-footer";
import WithoutHeaderWithFooter from "../pages/layouts/without-header-with-footer";
import WithoutHeaderWithoutFooter from "../pages/layouts/without-header-without-footer";

//主页
const IndexPage = lazy(() => import("../pages/index"));
//课程相关
const CoursePage = lazy(() => import("../pages/course/index"));
const CoursePlayPage = lazy(() => import("../pages/course/video"));
//最近学习
const LatestLearnPage = lazy(() => import("../pages/latest-learn"));
//错误页面
const ErrorPage = lazy(() => import("../pages/error"));

import PrivateRoute from "../compenents/private-route";

let RootPage: any = null;
if (getToken()) {
  RootPage = lazy(async () => {
    return new Promise<any>(async (resolve) => {
      try {
        let configRes: any = await system.config();
        let userRes: any = await user.detail();
        resolve({
          default: (
            <InitPage configData={configRes.data} loginData={userRes.data} />
          ),
        });
      } catch (e) {
        console.error("系统初始化失败", e);
      }
    });
  });
} else {
  RootPage = lazy(async () => {
    return new Promise<any>(async (resolve) => {
      try {
        let configRes: any = await system.config();

        resolve({
          default: <InitPage configData={configRes.data} />,
        });
      } catch (e) {
        console.error("系统初始化失败", e);
      }
    });
  });
}

const routes: RouteObject[] = [
  {
    path: "/",
    element: RootPage,
    children: [
      {
        path: "/",
        element: <WithHeaderWithFooter />,
        children: [
          {
            path: "/",
            element: <PrivateRoute Component={<IndexPage />} />,
          },

          {
            path: "/course/:courseId",
            element: <PrivateRoute Component={<CoursePage />} />,
          },

          {
            path: "/latest-learn",
            element: <PrivateRoute Component={<LatestLearnPage />} />,
          },
        ],
      },
      {
        path: "/",
        element: <WithHeaderWithoutFooter />,
        children: [
          {
            path: "/login",
            element: <LoginPage />,
          },
        ],
      },
      {
        path: "/",
        element: <WithoutHeaderWithoutFooter />,
        children: [
          {
            path: "/course/:courseId/hour/:hourId",
            element: <PrivateRoute Component={<CoursePlayPage />} />,
          },
          {
            path: "/error",
            element: <ErrorPage />,
          },
          {
            path: "*",
            element: <ErrorPage />,
          },
        ],
      },
    ],
  },
];

export default routes;
