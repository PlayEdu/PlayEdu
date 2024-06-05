import { createSlice } from "@reduxjs/toolkit";

type SystemConfigStoreInterface = {
  "ldap-enabled"?: boolean;
  systemApiUrl?: string;
  systemPcUrl?: string;
  systemH5Url?: string;
  systemLogo?: string;
  systemName?: string;
  memberDefaultAvatar?: string;
  courseDefaultThumbs?: string[];
  departments?: any;
  resourceCategories?: any;
};

let defaultValue: SystemConfigStoreInterface = {};

const systemConfigSlice = createSlice({
  name: "systemConfig",
  initialState: {
    value: defaultValue,
  },
  reducers: {
    saveConfigAction(stage, e) {
      stage.value = e.payload;
    },
    saveDepartmentsAction(stage, e) {
      stage.value.departments = e.payload;
    },
    saveCategoriesAction(stage, e) {
      stage.value.resourceCategories = e.payload;
    },
  },
});

export default systemConfigSlice.reducer;
export const { saveConfigAction, saveDepartmentsAction, saveCategoriesAction } =
  systemConfigSlice.actions;

export type { SystemConfigStoreInterface };
