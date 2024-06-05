import React, { useState, useEffect } from "react";
import { Image, Progress } from "antd";
import { useNavigate } from "react-router-dom";
import styles from "./courses-model.module.scss";
import mediaIcon from "../../../assets/images/commen/icon-medal.png";

interface PropInterface {
  id: number;
  title: string;
  thumb: string;
  isRequired: number;
  progress: number;
}

export const CoursesModel: React.FC<PropInterface> = ({
  id,
  title,
  thumb,
  isRequired,
  progress,
}) => {
  const navigate = useNavigate();
  return (
    <div
      className={styles["item"]}
      onClick={() => {
        navigate(`/course/${id}`);
      }}
    >
      <div className={styles["top-content"]}>
        <Image
          loading="lazy"
          width={120}
          height={90}
          style={{ borderRadius: 10 }}
          src={thumb}
          preview={false}
        />
        <div className={styles["info"]}>
          <div className={styles["title"]}>{title}</div>
          {isRequired === 1 && <div className={styles["type"]}>必修课</div>}
          {isRequired === 0 && (
            <div className={styles["active-type"]}>选修课</div>
          )}
        </div>
      </div>
      <div className={styles["status-content"]}>
        {progress == 0 && (
          <>
            <Progress
              style={{ width: 270 }}
              percent={0}
              strokeColor="#FF4D4F"
              trailColor="#F6F6F6"
              showInfo={false}
            />
            <span>未学习</span>
          </>
        )}
        {progress > 0 && progress < 100 && (
          <Progress
            percent={progress}
            strokeColor="#FF4D4F"
            trailColor="#F6F6F6"
          />
        )}
        {progress >= 100 && (
          <div className={styles["success"]}>
            <Image
              loading="lazy"
              width={24}
              height={24}
              src={mediaIcon}
              preview={false}
            />
            <span className="ml-8">恭喜你学完此课程!</span>
          </div>
        )}
      </div>
    </div>
  );
};
