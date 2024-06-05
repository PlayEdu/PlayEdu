import { useEffect, useRef, useState } from "react";
import { useSelector } from "react-redux";
import { Button } from "antd";
import { useDispatch } from "react-redux";
import { uploadAction } from "../../store/user/loginUserSlice";

interface PropsInterface {
  categoryIds: number[];
  onUpdate: () => void;
}

export const UploadVideoButton = (props: PropsInterface) => {
  const dispatch = useDispatch();
  const uploadStatus = useSelector(
    (state: any) => state.loginUser.value.uploadStatus
  );

  useEffect(() => {
    if (!uploadStatus) {
      props.onUpdate();
    }
  }, [uploadStatus]);

  return (
    <>
      <Button
        type="primary"
        onClick={() => {
          dispatch(
            uploadAction({
              uploadStatus: true,
              uploadCateIds: props.categoryIds,
            })
          );
        }}
      >
        上传视频
      </Button>
    </>
  );
};
