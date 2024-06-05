import { createSlice } from "@reduxjs/toolkit";
import { message } from "antd";

type UserInterface = {
  id: number;
  name: string;
  email: string;
};

type UserStoreInterface = {
  user: UserInterface | null;
  isLogin: boolean;
  permissions: string[];
  uploadStatus: boolean;
  uploadCateIds: number[];
};

let defaultValue: UserStoreInterface = {
  user: null,
  isLogin: false,
  permissions: [],
  uploadStatus: false,
  uploadCateIds: [],
};

const loginUserSlice = createSlice({
  name: "loginUser",
  initialState: {
    value: defaultValue,
  },
  reducers: {
    loginAction(stage, e) {
      stage.value.user = e.payload.user;
      stage.value.permissions = e.payload.permissions;
      stage.value.isLogin = true;
    },
    logoutAction(stage) {
      stage.value.user = null;
      stage.value.isLogin = false;
    },
    uploadAction(stage, e) {
      if (
        stage.value.uploadStatus === true &&
        e.payload.uploadStatus === true
      ) {
        message.error("请点击右下角悬浮窗");
      }
      stage.value.uploadStatus = e.payload.uploadStatus;
      stage.value.uploadCateIds = e.payload.uploadCateIds;
    },
  },
});

export default loginUserSlice.reducer;
export const { loginAction, logoutAction, uploadAction } =
  loginUserSlice.actions;

export type { UserStoreInterface };
