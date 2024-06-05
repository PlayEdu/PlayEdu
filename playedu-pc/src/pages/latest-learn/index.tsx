import React, { useState, useEffect } from "react";
import styles from "./index.module.scss";
import { course } from "../../api/index";
import { Row, Col, Spin, Image, Progress } from "antd";
import { Empty } from "../../compenents";
import mediaIcon from "../../assets/images/commen/icon-medal.png";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";

type LastLearnModel = {
  [key: number]: LearnModel;
};

type LearnModel = {
  course: LastCourseModel;
  hour_record: HourRecordModel;
  last_learn_hour: LastHourModel;
  record: CourseRecordModel;
};

type LastCourseModel = {
  charge?: number;
  class_hour: number;
  created_at?: string;
  id: number;
  is_required: number;
  is_show?: number;
  short_desc: string;
  thumb: string;
  title: string;
};

type LastHourModel = {
  chapter_id: number;
  course_id: number;
  duration: number;
  id: number;
  rid: number;
  sort: number;
  title: string;
  type: string;
};

const LatestLearnPage = () => {
  document.title = "最近学习";
  const navigate = useNavigate();
  const systemConfig = useSelector((state: any) => state.systemConfig.value);
  const [loading, setLoading] = useState<boolean>(false);
  const [courses, setCourses] = useState<LastLearnModel[]>([]);

  useEffect(() => {
    getCourses();
  }, []);

  const getCourses = () => {
    setLoading(true);
    course.latestLearn().then((res: any) => {
      setCourses(res.data);
      setLoading(false);
    });
  };

  return (
    <div className="main-body">
      <div className={styles["content"]}>
        {loading && (
          <Row style={{ width: 1200 }}>
            <div className="float-left d-j-flex mt-50">
              <Spin size="large" />
            </div>
          </Row>
        )}
        {!loading && courses.length === 0 && (
          <Row style={{ width: 1200 }}>
            <Col span={24}>
              <Empty />
            </Col>
          </Row>
        )}
        {!loading &&
          courses.length > 0 &&
          courses.map((item: any, index: number) => (
            <div key={index}>
              {item.course && (
                <div
                  className={styles["item"]}
                  onClick={() => {
                    navigate(`/course/${item.course.id}`);
                  }}
                >
                  <div style={{ width: 120 }}>
                    <Image
                      loading="lazy"
                      src={item.course.thumb}
                      width={120}
                      height={90}
                      style={{ borderRadius: 10 }}
                      preview={false}
                    />
                  </div>
                  <div className={styles["item-info"]}>
                    <div className={styles["top"]}>
                      {item.course.is_required === 1 && (
                        <div className={styles["type"]}>必修课</div>
                      )}
                      {item.course.is_required === 0 && (
                        <div className={styles["active-type"]}>选修课</div>
                      )}
                      <div className={styles["title"]}>{item.course.title}</div>
                    </div>
                    {item.record && (
                      <>
                        {item.last_learn_hour && (
                          <div className={styles["record"]}>
                            上次学到：{item.last_learn_hour.title}
                          </div>
                        )}
                        <div className={styles["progress"]}>
                          {item.record.progress < 10000 && (
                            <Progress
                              percent={Math.floor(item.record.progress / 100)}
                              strokeColor="#FF4D4F"
                              trailColor="#F6F6F6"
                            />
                          )}
                          {item.record.progress >= 10000 && (
                            <>
                              <Image
                                loading="lazy"
                                width={24}
                                height={24}
                                src={mediaIcon}
                                preview={false}
                              />
                              <span className={styles["tip"]}>
                                恭喜你学完此课程!
                              </span>
                            </>
                          )}
                        </div>
                      </>
                    )}
                    {!item.record && (
                      <>
                        {item.last_learn_hour && (
                          <div className={styles["record"]}>
                            上次学到：{item.last_learn_hour.title}
                          </div>
                        )}
                        <div className={styles["progress"]}>
                          <Progress
                            percent={1}
                            strokeColor="#FF4D4F"
                            trailColor="#F6F6F6"
                          />
                        </div>
                      </>
                    )}
                  </div>
                </div>
              )}
            </div>
          ))}
      </div>
      <div className={styles["extra"]}>{systemConfig.pcIndexFooterMsg}</div>
    </div>
  );
};

export default LatestLearnPage;
