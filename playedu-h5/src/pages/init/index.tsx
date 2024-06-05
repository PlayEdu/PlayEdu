import { useState, useEffect } from "react";
import { useDispatch } from "react-redux";
import { Outlet } from "react-router-dom";
import {
  SystemConfigStoreInterface,
  saveConfigAction,
} from "../../store/system/systemConfigSlice";
import { loginAction } from "../../store/user/loginUserSlice";
import { isMobile } from "../../utils";

interface Props {
  loginData?: any;
  configData?: any;
}

export const InitPage = (props: Props) => {
  const dispatch = useDispatch();
  const [init, setInit] = useState<boolean>(false);

  useEffect(() => {
    if (props.loginData) {
      dispatch(loginAction(props.loginData));
    }
    if (props.configData) {
      let config: SystemConfigStoreInterface = {
        //系统配置
        "ldap-enabled": props.configData["ldap-enabled"],
        systemApiUrl: props.configData["system-api-url"],
        systemH5Url: props.configData["system-h5-url"],
        systemLogo: props.configData["system-logo"],
        systemName: props.configData["system-name"],
        systemPcUrl: props.configData["system-pc-url"],
        pcIndexFooterMsg: props.configData["system-pc-index-footer-msg"],
        //播放器配置
        playerPoster: props.configData["player-poster"],
        playerIsEnabledBulletSecret:
          props.configData["player-is-enabled-bullet-secret"] &&
          props.configData["player-is-enabled-bullet-secret"] === "1"
            ? true
            : false,
        playerIsDisabledDrag:
          props.configData["player-disabled-drag"] &&
          props.configData["player-disabled-drag"] === "1"
            ? true
            : false,
        playerBulletSecretText: props.configData["player-bullet-secret-text"],
        playerBulletSecretColor: props.configData["player-bullet-secret-color"],
        playerBulletSecretOpacity:
          props.configData["player-bullet-secret-opacity"],
      };
      dispatch(saveConfigAction(config));
    }
    setInit(true);
    if (
      !isMobile() &&
      props.configData &&
      props.configData["system-pc-url"] !== ""
    ) {
      let url = props.configData["system-pc-url"];
      window.location.href = url;
    }
  }, [props]);

  return (
    <>
      {init && (
        <div>
          <Outlet />
        </div>
      )}
    </>
  );
};
