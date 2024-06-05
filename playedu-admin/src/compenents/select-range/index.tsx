import { useEffect, useState } from "react";
import { Modal, Tabs, message } from "antd";
import {} from "../../compenents";
import styles from "./index.module.less";
import type { TabsProps } from "antd";
import { SelectDepsSub } from "./select-deps-sub";
import { CloseOutlined } from "@ant-design/icons";

interface PropsInterface {
  defaultDepIds: any[];
  defaultDeps: any[];
  open: boolean;
  onSelected: (selDepIds: any[], selDeps: any[]) => void;
  onCancel: () => void;
}

type selVideosModel = {
  name: string;
  id: number;
};

export const SelectRange = (props: PropsInterface) => {
  const [refresh, setRefresh] = useState(true);
  const [selectKeys, setSelectKeys] = useState<number[]>([]);
  const [selectVideos, setSelectVideos] = useState<selVideosModel[]>([]);

  useEffect(() => {
    setRefresh(!refresh);
  }, [props.open]);

  useEffect(() => {
    setSelectKeys(props.defaultDepIds);
    setSelectVideos(props.defaultDeps);
  }, [props.defaultDepIds, props.defaultDeps, refresh]);

  return (
    <>
      {props.open ? (
        <Modal
          title="选择部门"
          centered
          closable={false}
          onCancel={() => {
            props.onCancel();
          }}
          okText="确定"
          open={true}
          width={800}
          maskClosable={false}
          onOk={() => {
            if (selectKeys.length === 0) {
              message.error("请选择至少一个部门对象");
              return;
            }
            props.onSelected(selectKeys, selectVideos);
          }}
        >
          <div style={{ width: "100%", display: "flex" }}>
            <div style={{ width: 528 }} className="select-range-modal">
              <SelectDepsSub
                defaultkeys={selectKeys}
                open={refresh}
                onSelected={(arr: any[], videos: any[]) => {
                  if (arr && videos) {
                    setSelectKeys(arr);
                    setSelectVideos(videos);
                  }
                }}
              ></SelectDepsSub>
            </div>
            <div className={styles["user-content"]}>
              <div className={styles["title"]}>
                <div className={styles["tit"]}>已选择：</div>
                <div
                  className={styles["link"]}
                  onClick={() => {
                    setSelectKeys([]);
                    setSelectVideos([]);
                  }}
                >
                  清空
                </div>
              </div>
              {selectVideos.length > 0 &&
                selectVideos.map((item: any, index: number) => (
                  <div key={"dep" + index} className={styles["user-item"]}>
                    <div className={styles["user-name"]}>
                      {item.title.props.children}
                    </div>
                    <CloseOutlined
                      style={{
                        fontSize: 10,
                        color: "rgba(0,0,0,0.45)",
                        cursor: "pointer",
                      }}
                      onClick={() => {
                        let arr = [...selectKeys];
                        let arr2 = [...selectVideos];
                        arr.splice(index, 1);
                        arr2.splice(index, 1);
                        setSelectKeys(arr);
                        setSelectVideos(arr2);
                      }}
                    />
                  </div>
                ))}
            </div>
          </div>
        </Modal>
      ) : null}
    </>
  );
};
