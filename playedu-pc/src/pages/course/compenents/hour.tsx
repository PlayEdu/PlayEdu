import React from "react";
import { useNavigate } from "react-router-dom";
import styles from "./hour.module.scss";
import { durationFormat } from "../../../utils/index";

interface PropInterface {
  id: number;
  cid: number;
  title: string;
  duration: number;
  record: any;
  progress: number;
}

export const HourCompenent: React.FC<PropInterface> = ({
  id,
  cid,
  title,
  duration,
  record,
  progress,
}) => {
  const navigate = useNavigate();
  return (
    <>
      <div
        className={styles["item"]}
        onClick={() => {
          navigate(`/course/${cid}/hour/${id}`);
        }}
      >
        <div className={styles["left-item"]}>
          <i className="iconfont icon-icon-video"></i>
          <div className={styles["title"]}>
            {title}({durationFormat(Number(duration))})
          </div>
        </div>
        <div className="d-flex">
          {progress >= 0 && progress < 100 && (
            <>
              {progress === 0 && <div className={styles["link"]}>开始学习</div>}
              {progress !== 0 && (
                <>
                  <div className={styles["record"]}>
                    上次学习到
                    {durationFormat(Number(record.finished_duration || 0))}
                  </div>
                  <div className={styles["link"]}>继续学习</div>
                </>
              )}
            </>
          )}
          {progress >= 100 && <div className={styles["complete"]}>已学完</div>}
        </div>
      </div>
    </>
  );
};
