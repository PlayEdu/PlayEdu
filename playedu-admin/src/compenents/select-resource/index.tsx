import { useEffect, useState } from "react";
import { Row, Modal,  } from "antd";
import styles from "./index.module.less";
import { UploadVideoSub } from "../../compenents";

interface PropsInterface {
  defaultKeys: any[];
  open: boolean;
  onSelected: (arr: any[], videos: any[]) => void;
  onCancel: () => void;
}

type selVideosModel = {
  name: string;
  rid: number;
  type: string;
  duration: number;
};

export const SelectResource = (props: PropsInterface) => {
  const [refresh, setRefresh] = useState(true);
  const [selectKeys, setSelectKeys] = useState<number[]>([]);
  const [selectVideos, setSelectVideos] = useState<selVideosModel[]>([]);

  useEffect(() => {
    setRefresh(!refresh);
  }, [props.open]);

  return (
    <>
      {props.open ? (
        <Modal
          title="视频库"
          centered
          closable={false}
          onCancel={() => {
            setSelectKeys([]);
            setSelectVideos([]);
            props.onCancel();
          }}
          open={true}
          width={800}
          maskClosable={false}
          onOk={() => {
            props.onSelected(selectKeys, selectVideos);
            setSelectKeys([]);
            setSelectVideos([]);
          }}
        >
          <Row>
            <div className="float-left mt-24">
              <UploadVideoSub
                label="视频"
                defaultCheckedList={props.defaultKeys}
                open={refresh}
                onSelected={(arr: any[], videos: any[]) => {
                  setSelectKeys(arr);
                  setSelectVideos(videos);
                }}
              />
            </div>
          </Row>
        </Modal>
      ) : null}
    </>
  );
};
