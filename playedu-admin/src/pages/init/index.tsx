import { useDispatch } from "react-redux";
import { Outlet } from "react-router-dom";
import { loginAction } from "../../store/user/loginUserSlice";
import {
  SystemConfigStoreInterface,
  saveConfigAction,
} from "../../store/system/systemConfigSlice";
import { UploadVideoFloatButton } from "../../compenents/upload-video-float-button";

interface Props {
  loginData?: any;
  configData?: any;
}

const InitPage = (props: Props) => {
  const dispatch = useDispatch();
  if (props.loginData) {
    dispatch(loginAction(props.loginData));
  }

  if (props.configData) {
    let config: SystemConfigStoreInterface = {
      "ldap-enabled": props.configData["ldap-enabled"],
      systemName: props.configData["system.name"],
      systemLogo: props.configData["system.logo"],
      systemPcUrl: props.configData["system.pc_url"],
      systemH5Url: props.configData["system.h5_url"],
      resourceUrl: props.configData["resource_url"],
      memberDefaultAvatar: props.configData["member.default_avatar"],
      courseDefaultThumbs: props.configData["default.course_thumbs"],
      departments: props.configData["departments"],
      resourceCategories: props.configData["resource_categories"],
    };
    dispatch(saveConfigAction(config));
  }

  return (
    <>
      <Outlet />
      <UploadVideoFloatButton></UploadVideoFloatButton>
    </>
  );
};

export default InitPage;
