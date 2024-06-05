import React, { useEffect, useState } from "react";
import styles from "./hour.module.scss";
import { durationFormat } from "../../../utils/index";

interface PropInterface {
  id: number;
  cid: number;
  title: string;
  duration: number;
  record: any;
  onSuccess: (cid: number, id: number) => void;
}

export const HourCompenent: React.FC<PropInterface> = ({
  id,
  cid,
  title,
  duration,
  record,
  onSuccess,
}) => {
  const [userProgress, setUserProgress] = useState(0);
  useEffect(() => {
    if (record?.finished_duration && record?.total_duration) {
      setUserProgress((record.finished_duration * 100) / record.total_duration);
    } else {
      setUserProgress(0);
    }
  }, [record]);

  return (
    <>
      <div
        className={styles["item"]}
        onClick={() => {
          onSuccess(cid, id);
        }}
      >
        <div className={styles["top-item"]}>
          <div className="d-flex">
            <i className="iconfont icon-icon-video"></i>
            <span className={styles["label"]}>视频</span>
          </div>
          {userProgress > 0 && userProgress < 100 && (
            <div className={styles["studying"]}>
              <span>
                学习到
                {durationFormat(Number(record.finished_duration || 0))}
              </span>
            </div>
          )}
          {userProgress >= 100 && (
            <div className={styles["complete"]}>
              <span>已学完</span>{" "}
            </div>
          )}
        </div>
        <div className={styles["title"]}>
          {title} ({durationFormat(Number(duration))})
        </div>
      </div>
    </>
  );
};
