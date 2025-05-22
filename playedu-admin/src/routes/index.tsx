import { lazy } from "react";
import { RouteObject } from "react-router-dom";
import { login, system } from "../api";

import { getToken } from "../utils";
import KeepAlive from "../compenents/keep-alive";
// 页面加载
import InitPage from "../pages/init";
import LoginPage from "../pages/login";
import WithHeaderWithoutFooter from "../pages/layouts/with-header-without-footer";
import WithoutHeaderWithoutFooter from "../pages/layouts/without-header-without-footer";

//首页
const DashboardPage = lazy(() => import("../pages/dashboard"));
//修改密码页面
const ChangePasswordPage = lazy(() => import("../pages/change-password"));
//资源管理相关
const ResourceCategoryPage = lazy(
  () => import("../pages/resource/resource-category")
);
const ResourceImagesPage = lazy(() => import("../pages/resource/images"));
const ResourceVideosPage = lazy(() => import("../pages/resource/videos"));
const ResourceCoursewarePage = lazy(
  () => import("../pages/resource/courseware")
);
//课程相关
const CoursePage = lazy(() => import("../pages/course/index"));
const CourseUserPage = lazy(() => import("../pages/course/user"));
//学员相关
const MemberPage = lazy(() => import("../pages/member"));
const MemberImportPage = lazy(() => import("../pages/member/import"));
const MemberLearnPage = lazy(() => import("../pages/member/learn"));
const MemberDepartmentProgressPage = lazy(
  () => import("../pages/member/departmentUser")
);
//系统相关
const SystemConfigPage = lazy(() => import("../pages/system/config"));
const SystemAdministratorPage = lazy(
  () => import("../pages/system/administrator")
);
const SystemAdminrolesPage = lazy(() => import("../pages/system/adminroles"));
const SystemLogPage = lazy(() => import("../pages/system/adminlog"));
//部门页面
const DepartmentPage = lazy(() => import("../pages/department"));
//测试
const TestPage = lazy(() => import("../pages/test"));
//错误页面
const ErrorPage = lazy(() => import("../pages/error"));
//使用许可页面
const LicensingPage = lazy(() => import("../pages/licensing/index"));

import PrivateRoute from "../compenents/private-route";

// const LoginPage = lazy(() => import("../pages/login"));

let RootPage: any = null;
if (getToken()) {
  RootPage = lazy(async () => {
    return new Promise<any>(async (resolve) => {
      try {
        let configRes: any = await system.getSystemConfig();
        let userRes: any = await login.getUser();

        resolve({
          default: (
            <InitPage configData={configRes.data} loginData={userRes.data} />
          ),
        });
      } catch (e) {
        console.error("系统初始化失败", e);
        resolve({
          default: <ErrorPage />,
        });
      }
    });
  });
} else {
  RootPage = <InitPage />;
}

const routes: RouteObject[] = [
  {
    path: "/",
    element: RootPage,
    children: [
      {
        path: "/",
        element: <PrivateRoute Component={<WithHeaderWithoutFooter />} />,
        children: [
          {
            path: "/",
            element: <PrivateRoute Component={<DashboardPage />} />,
          },
          {
            path: "/change-password",
            element: <PrivateRoute Component={<ChangePasswordPage />} />,
          },
          {
            path: "/resource-category",
            element: <PrivateRoute Component={<ResourceCategoryPage />} />,
          },
          {
            path: "/images",
            element: <PrivateRoute Component={<ResourceImagesPage />} />,
          },
          {
            path: "/videos",
            element: <PrivateRoute Component={<ResourceVideosPage />} />,
          },
          {
            path: "/courseware",
            element: <PrivateRoute Component={<ResourceCoursewarePage />} />,
          },
          {
            path: "/course",
            element: <PrivateRoute Component={<CoursePage />} />,
          },
          {
            path: "/course/user/:courseId",
            element: <PrivateRoute Component={<CourseUserPage />} />,
          },
          {
            path: "/member",
            element: <KeepAlive />,
            children: [
              {
                path: "/member/index",
                element: <PrivateRoute Component={<MemberPage />} />,
              },
              {
                path: "/member/import",
                element: <PrivateRoute Component={<MemberImportPage />} />,
              },
              {
                path: "/member/learn",
                element: <PrivateRoute Component={<MemberLearnPage />} />,
              },
              {
                path: "/member/departmentUser",
                element: (
                  <PrivateRoute Component={<MemberDepartmentProgressPage />} />
                ),
              },
            ],
          },
          {
            path: "/system/config/index",
            element: <PrivateRoute Component={<SystemConfigPage />} />,
          },
          {
            path: "/system/administrator",
            element: <PrivateRoute Component={<SystemAdministratorPage />} />,
          },
          {
            path: "/system/adminroles",
            element: <PrivateRoute Component={<SystemAdminrolesPage />} />,
          },
          {
            path: "/system/adminlog",
            element: <PrivateRoute Component={<SystemLogPage />} />,
          },
          {
            path: "/department",
            element: <PrivateRoute Component={<DepartmentPage />} />,
          },
          {
            path: "/licensing",
            element: <PrivateRoute Component={<LicensingPage />} />,
          },
        ],
      },
      {
        path: "/",
        element: <WithoutHeaderWithoutFooter />,
        children: [
          {
            path: "/login",
            element: <LoginPage />,
          },
          {
            path: "/test",
            element: <TestPage />,
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
