import { useState, useEffect } from "react";
import { PullToRefresh, Skeleton } from "antd-mobile";
import { sleep } from "antd-mobile/es/utils/sleep";
import styles from "./index.module.scss";
import { course } from "../../api/index";
import { Empty } from "../../components";
import { CoursesModel } from "./compenents/courses-model";
import defaultThumb1 from "../../assets/thumb/thumb1.png";
import defaultThumb2 from "../../assets/thumb/thumb2.png";
import defaultThumb3 from "../../assets/thumb/thumb3.png";
import moment from "moment";

const StudyPage = () => {
  const [loading, setLoading] = useState(false);
  const [todayCourses, setTodayCourses] = useState<CourseModel[]>([]);
  const [yesterdayCourses, setYesterdayCourses] = useState<CourseModel[]>([]);
  const [courses, setCourses] = useState<CourseModel[]>([]);
  const [resourceUrl, setResourceUrl] = useState<ResourceUrlModel>({});

  useEffect(() => {
    document.title = "最近学习";
  }, []);

  useEffect(() => {
    setLoading(true);
    getCourses();
  }, []);

  const getCourses = () => {
    course
      .latestLearn()
      .then((res: any) => {
        if (res.data.resource_url && res.data.user_latest_learns) {
          setResourceUrl(res.data.resource_url);
          let data = res.data.user_latest_learns;
          let today: CourseModel[] = [];
          let yesterday: CourseModel[] = [];
          let box: CourseModel[] = [];
          if (data && data.length > 0) {
            data.map((item: any) => {
              let time = moment(item.hour_record.updated_at)
                .utcOffset(0)
                .format("YYYY-MM-DD HH:mm:ss");
              if (moment(time).isSame(moment(), "day")) {
                today.push(item);
              } else if (
                moment(time).isSame(moment().subtract(1, "day"), "day")
              ) {
                yesterday.push(item);
              } else {
                box.push(item);
              }
            });
          }
          setTodayCourses(today);
          setYesterdayCourses(yesterday);
          setCourses(box);
        }
        setLoading(false);
      })
      .catch((e) => {
        setLoading(false);
      });
  };

  return (
    <div className="main-body">
      <div className={styles["title"]}>最近学习</div>
      <div className="float-left" style={{ position: "relative" }}>
        <PullToRefresh
          onRefresh={async () => {
            setLoading(true);
            await sleep(700);
            getCourses();
          }}
        >
          <div className={styles["list-box"]}>
            {loading &&
              Array.from({ length: 2 }).map((_, i) => (
                <div className={styles["item"]} key={i}>
                  <Skeleton
                    animated
                    style={{
                      width: 100,
                      height: 75,
                      borderRadius: 8,
                      marginRight: 15,
                    }}
                  />
                  <div className={styles["item-info"]}>
                    <Skeleton animated style={{ width: "100%", height: 21 }} />
                    <Skeleton animated style={{ width: "100%", height: 24 }} />
                  </div>
                </div>
              ))}
            {!loading &&
              courses.length === 0 &&
              todayCourses.length === 0 &&
              yesterdayCourses.length === 0 && <Empty></Empty>}
            {!loading && (
              <>
                {todayCourses.length > 0 && (
                  <>
                    <div className={styles["label"]}>今日</div>
                    {todayCourses.map((item: any, index: number) => (
                      <div key={index} style={{ width: "100%" }}>
                        {item.course && (
                          <CoursesModel
                            id={item.course.id}
                            title={item.course.title}
                            thumb={
                              item.course.thumb === -1
                                ? defaultThumb1
                                : item.course.thumb === -2
                                ? defaultThumb2
                                : item.course.thumb === -3
                                ? defaultThumb3
                                : resourceUrl[item.course.thumb]
                            }
                            isRequired={item.course.is_required}
                            record={item.record}
                          ></CoursesModel>
                        )}
                      </div>
                    ))}
                  </>
                )}
                {yesterdayCourses.length > 0 && (
                  <>
                    <div className={styles["label"]}>昨日</div>
                    {yesterdayCourses.map((item: any, index: number) => (
                      <div key={index} style={{ width: "100%" }}>
                        {item.course && (
                          <CoursesModel
                            id={item.course.id}
                            title={item.course.title}
                            thumb={
                              item.course.thumb === -1
                                ? defaultThumb1
                                : item.course.thumb === -2
                                ? defaultThumb2
                                : item.course.thumb === -3
                                ? defaultThumb3
                                : resourceUrl[item.course.thumb]
                            }
                            isRequired={item.course.is_required}
                            record={item.record}
                          ></CoursesModel>
                        )}
                      </div>
                    ))}
                  </>
                )}
                {courses.length > 0 && (
                  <>
                    <div className={styles["label"]}>更早</div>
                    {courses.map((item: any, index: number) => (
                      <div key={index} style={{ width: "100%" }}>
                        {item.course && (
                          <CoursesModel
                            id={item.course.id}
                            title={item.course.title}
                            thumb={
                              item.course.thumb === -1
                                ? defaultThumb1
                                : item.course.thumb === -2
                                ? defaultThumb2
                                : item.course.thumb === -3
                                ? defaultThumb3
                                : resourceUrl[item.course.thumb]
                            }
                            isRequired={item.course.is_required}
                            record={item.record}
                          ></CoursesModel>
                        )}
                      </div>
                    ))}
                  </>
                )}
              </>
            )}
          </div>
        </PullToRefresh>
      </div>
    </div>
  );
};

export default StudyPage;
