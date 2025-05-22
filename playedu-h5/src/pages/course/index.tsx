import { useEffect, useState } from "react";
import { Image, ProgressCircle, Tabs, Toast } from "antd-mobile";
import styles from "./index.module.scss";
import { useNavigate, useLocation, useParams } from "react-router-dom";
import backIcon from "../../assets/images/commen/icon-back-n.png";
import { course as vod } from "../../api/index";
import { isEmptyObject, isWechat, isIOS } from "../../utils/index";
import { Empty } from "../../components";
import { HourCompenent } from "./compenents/hour";

type LocalUserLearnHourRecordModel = {
  [key: number]: UserLearnHourRecordModel;
};

type LocalCourseHour = {
  [key: number]: CourseHourModel[];
};

type tabModal = {
  key: number;
  label: string;
};

type attachModal = {
  id: number;
  course_id: number;
  rid: number;
  sort: number;
  title: string;
  type: string;
  url?: string;
};

const CoursePage = () => {
  const { courseId } = useParams();
  const navigate = useNavigate();
  const result = new URLSearchParams(useLocation().search);
  const [course, setCourse] = useState<CourseModel | null>(null);
  const [chapters, setChapters] = useState<ChapterModel[]>([]);
  const [hours, setHours] = useState<LocalCourseHour | null>(null);
  const [learnRecord, setLearnRecord] = useState<UserLearnRecordModel | null>(
    null
  );
  const [learnHourRecord, setLearnHourRecord] =
    useState<LocalUserLearnHourRecordModel>({});

  const [courseTypeText, setCourseTypeText] = useState("");
  const [userCourseProgress, setUserCourseProgress] = useState(0);
  const [tabKey, setTabKey] = useState(Number(result.get("tab") || 1));
  const [attachments, setAttachments] = useState<attachModal[]>([]);
  const [items, setItems] = useState<tabModal[]>([]);
  const [downLoadTemplateURL, setDownLoadTemplateURL] = useState("");

  useEffect(() => {
    getDetail();
  }, [courseId]);

  const getDetail = () => {
    vod.detail(Number(courseId)).then((res: any) => {
      let courseItem: CourseModel = res.data.course;
      document.title = courseItem.title || "课程详情";
      setCourse(courseItem);
      setChapters(res.data.chapters);
      setHours(res.data.hours);
      if (res.data.learn_record) {
        setLearnRecord(res.data.learn_record);
      }
      if (res.data.learn_hour_records) {
        setLearnHourRecord(res.data.learn_hour_records);
      }
      let arr = res.data.attachments;
      let tabs = [
        {
          key: 1,
          label: `课程目录`,
        },
      ];
      if (arr.length > 0) {
        tabs.push({
          key: 2,
          label: `课程附件`,
        });
        setAttachments(arr);
      }
      setItems(tabs);
    });
  };

  useEffect(() => {
    if (course) {
      setCourseTypeText(course.is_required === 1 ? "必修课" : "选修课");
    }
  }, [course]);

  useEffect(() => {
    if (learnRecord?.progress) {
      setUserCourseProgress(Math.floor(learnRecord.progress / 100));
    } else if (learnHourRecord && !isEmptyObject(learnHourRecord)) {
      setUserCourseProgress(1);
    } else {
      setUserCourseProgress(0);
    }
  }, [learnRecord, learnHourRecord]);

  const playVideo = (cid: number, id: number) => {
    navigate(`/course/${cid}/hour/${id}`);
  };

  const downLoadFile = (cid: number, id: number, rid: number) => {
    vod.downloadAttachment(cid, id).then((res: any) => {
      let url = res.data.resource_url[rid];
      if (isWechat()) {
        if (isIOS()) {
          Toast.show("请点击右上角···浏览器打开下载");
        }
        var input = document.createElement("input");
        input.value = url;
        document.body.appendChild(input);
        input.select();
        document.execCommand("Copy");
        document.body.removeChild(input);
        window.open(url);
      } else {
        if (isIOS()) {
          setDownLoadTemplateURL(url);
          setTimeout(() => {
            let $do: any = document.querySelector("#downLoadExcel");
            $do.click();
          }, 500);
        } else {
          window.open(url);
        }
      }
    });
  };

  return (
    <div className="main-body">
      <a
        style={{ display: "none" }}
        id="downLoadExcel"
        href={downLoadTemplateURL}
        download={true}
      ></a>
      <div className="main-header" style={{ backgroundColor: "#FF4D4F" }}>
        <Image
          className="back-icon"
          src={backIcon}
          onClick={() => {
            if (window.history.length <= 1) {
              // 将页面跳转到首页
              navigate("/");
            } else {
              // 返回到前一个页面
              navigate(-1);
            }
          }}
        />
      </div>
      <div className={styles["top-content"]}>
        <div className={styles["title"]}>{course?.title + ""}</div>
        <div className={styles["info-content"]}>
          <div className={styles["info"]}>
            <div className={styles["record"]}>
              已学完课时{" "}
              <strong>
                {learnRecord ? learnRecord.finished_count || 0 : 0}
              </strong>{" "}
              / {course?.class_hour}
            </div>
            <div className={styles["type"]}>{courseTypeText}</div>
          </div>
          <div className={styles["progress-box"]}>
            <ProgressCircle
              percent={userCourseProgress}
              style={{
                "--size": "80px",
                "--fill-color": "#FFFFFF",
                "--track-color": "#ffffff4D",
                "--track-width": "7px",
              }}
            >
              <span className={styles.num}>{userCourseProgress}%</span>
            </ProgressCircle>
          </div>
        </div>
      </div>
      <div className={styles["other-content"]}>
        <div className="course-tab-box">
          <Tabs
            activeLineMode="fixed"
            activeKey={String(tabKey)}
            onChange={(key: any) => {
              setTabKey(Number(key));
            }}
            style={{
              "--fixed-active-line-width": "20px",
              "--active-line-height": "3px",
              "--active-title-color": "rgba(0,0,0,0.88)",
              "--active-line-border-radius": "2px",
              "--title-font-size": "16px",
              "--content-padding": "18px",
            }}
          >
            {items.map((item) => (
              <Tabs.Tab title={item.label} key={item.key} />
            ))}
          </Tabs>
        </div>
        {tabKey === 1 && (
          <>
            {course?.short_desc && (
              <>
                <div className={styles["desc"]}>{course.short_desc}</div>
                <div className={styles["line"]}></div>
              </>
            )}
            <div className={styles["chapters-hours-cont"]}>
              {chapters.length === 0 && !hours && <Empty />}

              {chapters.length === 0 && hours && (
                <div
                  className={styles["hours-list-box"]}
                  style={{ marginTop: 10 }}
                >
                  {hours[0].map((item: CourseHourModel) => (
                    <div key={item.id} className={styles["hours-it"]}>
                      <HourCompenent
                        id={item.id}
                        cid={item.course_id}
                        title={item.title}
                        record={learnHourRecord[item.id]}
                        duration={item.duration}
                        onSuccess={(cid: number, id: number) => {
                          playVideo(cid, id);
                        }}
                      ></HourCompenent>
                    </div>
                  ))}
                </div>
              )}

              {chapters.length > 0 && hours && (
                <div className={styles["hours-list-box"]}>
                  {chapters.map((item: ChapterModel) => (
                    <div key={item.id} className={styles["chapter-it"]}>
                      <div className={styles["chapter-name"]}>{item.name}</div>
                      {hours[item.id]?.map((it: CourseHourModel) => (
                        <div key={it.id} className={styles["hours-it"]}>
                          <HourCompenent
                            id={it.id}
                            cid={item.course_id}
                            title={it.title}
                            record={learnHourRecord[it.id]}
                            duration={it.duration}
                            onSuccess={(cid: number, id: number) => {
                              playVideo(cid, id);
                            }}
                          ></HourCompenent>
                        </div>
                      ))}
                    </div>
                  ))}
                </div>
              )}
            </div>
          </>
        )}
        {tabKey === 2 && (
          <div className={styles["attachments-cont"]}>
            {attachments.map((item: any, index: number) => (
              <div key={index} className={styles["attachments-item"]}>
                <div className={styles["left-cont"]}>
                  <div className={styles["label"]}>
                    <i
                      className="iconfont icon-icon-file"
                      style={{
                        fontSize: 15,
                        color: "rgba(0,0,0,0.3)",
                        marginRight: 5,
                      }}
                    />
                    <span>课件</span>
                  </div>
                  <div className={styles["title"]}>
                    {item.title}.{item.ext}
                  </div>
                </div>
                <div
                  className={styles["download"]}
                  onClick={() =>
                    downLoadFile(item.course_id, item.id, item.rid)
                  }
                >
                  下载
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default CoursePage;
