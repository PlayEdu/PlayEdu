import { useEffect, useState } from "react";
import { Row, Spin, Image, Progress } from "antd";
import styles from "./index.module.scss";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import { course as Course } from "../../api/index";
import mediaIcon from "../../assets/images/commen/icon-medal.png";
import { HourCompenent } from "./compenents/hour";
import { Empty } from "../../compenents";
import iconRoute from "../../assets/images/commen/icon-route.png";
import defaultThumb1 from "../../assets/thumb/thumb1.png";
import defaultThumb2 from "../../assets/thumb/thumb2.png";
import defaultThumb3 from "../../assets/thumb/thumb3.png";

type TabModel = {
  key: number;
  label: string;
};

type AttachModel = {
  id: number;
  course_id: number;
  rid: number;
  sort: number;
  title: string;
  type: string;
  url?: string;
};

type HoursModel = {
  [key: number]: HourModel[];
};

type ChapterModel = {
  course_id: number;
  created_at: string;
  id: number;
  name: string;
  sort: number;
  updated_at: string;
};

type LearnHourRecordsModel = {
  [key: number]: HourRecordModel;
};

const CoursePage = () => {
  const params = useParams();
  const navigate = useNavigate();
  const result = new URLSearchParams(useLocation().search);
  const [loading, setLoading] = useState<boolean>(true);
  const [course, setCourse] = useState<CourseModel | null>(null);
  const [chapters, setChapters] = useState<ChapterModel[]>([]);
  const [hours, setHours] = useState<HoursModel>({});
  const [learnRecord, setLearnRecord] = useState<CourseRecordModel | null>(
    null
  );
  const [learnHourRecord, setLearnHourRecord] = useState<LearnHourRecordsModel>(
    {}
  );
  const [tabKey, setTabKey] = useState(Number(result.get("tab") || 1));
  const [attachments, setAttachments] = useState<AttachModel[]>([]);
  const [resourceUrl, setResourceUrl] = useState<ResourceUrlModel>({});
  const [items, setItems] = useState<TabModel[]>([]);

  useEffect(() => {
    getDetail();
  }, [params.courseId]);

  const getDetail = () => {
    setLoading(true);
    Course.detail(Number(params.courseId))
      .then((res: any) => {
        document.title = res.data.course.title;
        setCourse(res.data.course);
        setChapters(res.data.chapters);
        setHours(res.data.hours);
        setResourceUrl(res.data.resource_url);
        if (res.data.learn_record) {
          setLearnRecord(res.data.learn_record);
        }
        if (res.data.learn_hour_records) {
          setLearnHourRecord(res.data.learn_hour_records);
        }
        let arr: AttachModel[] = res.data.attachments;
        let tabs: TabModel[] = [
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

        setLoading(false);
      })
      .catch((e) => {
        setLoading(false);
      });
  };

  const onChange = (key: number) => {
    setTabKey(key);
    navigate("/course/" + params.courseId + "?tab=" + key);
  };

  const downLoadFile = (
    cid: number,
    id: number,
    rid: number,
    fileName: string,
    type: string
  ) => {
    Course.downloadAttachment(cid, id).then((res: any) => {
      if (type === "TXT") {
        fetch(res.data.resource_url[rid])
          .then((response) => response.blob())
          .then((blob) => {
            const n_url = URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.style.display = "none";
            a.href = n_url;
            a.download = fileName; // 设置下载的文件名
            document.body.appendChild(a);
            a.click(); // 触发点击事件
            // 释放 URL 对象
            URL.revokeObjectURL(n_url);
            document.body.removeChild(a);
          })
          .catch((error) => {
            console.error("下载文件时出错:", error);
          });
      } else {
        window.open(res.data.resource_url[rid]);
      }
    });
  };

  return (
    <div className="container">
      {loading && (
        <Row
          style={{
            width: 1200,
            margin: "0 auto",
            paddingTop: 14,
            minHeight: 301,
          }}
        >
          <div className="float-left d-j-flex mt-50">
            <Spin size="large" />
          </div>
        </Row>
      )}
      {!loading && (
        <>
          <div className={styles["top-cont"]}>
            <div className="j-b-flex">
              <div className="d-flex">
                {course ? (
                  <Image
                    width={120}
                    height={90}
                    style={{ borderRadius: 10 }}
                    preview={false}
                    src={
                      course.thumb === -1
                        ? defaultThumb1
                        : course.thumb === -2
                        ? defaultThumb2
                        : course.thumb === -3
                        ? defaultThumb3
                        : resourceUrl[course.thumb]
                    }
                  />
                ) : null}
                <div className={styles["info"]}>
                  <div className={styles["title"]}>{course?.title}</div>
                  <div className={styles["status"]}>
                    {course?.is_required === 1 && (
                      <div className={styles["type"]}>必修课</div>
                    )}
                    {course?.is_required === 0 && (
                      <div className={styles["active-type"]}>选修课</div>
                    )}
                    {learnRecord && learnRecord.progress / 100 >= 100 && (
                      <div className={styles["success"]}>
                        <Image
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
              </div>
              {(!learnRecord ||
                (learnRecord && JSON.stringify(learnRecord) === "{}")) &&
                JSON.stringify(learnHourRecord) === "{}" && (
                  <Progress
                    type="circle"
                    strokeColor="#FF4D4F"
                    trailColor="#F6F6F6"
                    size={90}
                    strokeWidth={8}
                    percent={0}
                    format={(percent) => `${percent}%`}
                  />
                )}
              {(!learnRecord ||
                (learnRecord && JSON.stringify(learnRecord) === "{}")) &&
                JSON.stringify(learnHourRecord) !== "{}" && (
                  <Progress
                    type="circle"
                    strokeColor="#FF4D4F"
                    trailColor="#F6F6F6"
                    size={90}
                    strokeWidth={8}
                    percent={1}
                    format={(percent) => `${percent}%`}
                  />
                )}
              {learnRecord &&
                JSON.stringify(learnRecord) !== "{}" &&
                JSON.stringify(learnHourRecord) !== "{}" && (
                  <Progress
                    type="circle"
                    strokeColor="#FF4D4F"
                    trailColor="#F6F6F6"
                    size={90}
                    strokeWidth={8}
                    percent={Math.floor(learnRecord.progress / 100)}
                    format={(percent) => `${percent}%`}
                  />
                )}
            </div>
            {course?.short_desc && (
              <div className={styles["desc"]}>{course.short_desc}</div>
            )}
          </div>
          <div className={styles["tabs"]}>
            {items.map((item: any) => (
              <div
                key={item.key}
                className={
                  item.key === tabKey
                    ? styles["tab-active-item"]
                    : styles["tab-item"]
                }
                onClick={() => {
                  onChange(item.key);
                }}
              >
                <div className={styles["tit"]}>{item.label}</div>
                {item.key === tabKey && (
                  <Image
                    className={styles["banner"]}
                    width={40}
                    height={8}
                    preview={false}
                    src={iconRoute}
                    style={{ marginTop: -16 }}
                  />
                )}
              </div>
            ))}
          </div>
          {tabKey === 1 && (
            <div className={styles["chapters-hours-cont"]}>
              {chapters.length === 0 && JSON.stringify(hours) === "{}" && (
                <Empty />
              )}
              {chapters.length === 0 && JSON.stringify(hours) !== "{}" && (
                <div className={styles["hours-list-box"]}>
                  {hours[0].map((item: any, index: number) => (
                    <div key={item.id} className={styles["hours-it"]}>
                      {learnHourRecord[item.id] && (
                        <HourCompenent
                          id={item.id}
                          cid={item.course_id}
                          title={item.title}
                          record={learnHourRecord[item.id]}
                          duration={item.duration}
                          progress={
                            (learnHourRecord[item.id].finished_duration * 100) /
                            learnHourRecord[item.id].total_duration
                          }
                        ></HourCompenent>
                      )}
                      {!learnHourRecord[item.id] && (
                        <HourCompenent
                          id={item.id}
                          cid={item.course_id}
                          title={item.title}
                          record={null}
                          duration={item.duration}
                          progress={0}
                        ></HourCompenent>
                      )}
                    </div>
                  ))}
                </div>
              )}
              {chapters.length > 0 && JSON.stringify(hours) !== "{}" && (
                <div className={styles["hours-list-box"]}>
                  {chapters.map((item: any, index: number) => (
                    <div key={item.id} className={styles["chapter-it"]}>
                      <div className={styles["chapter-name"]}>{item.name}</div>
                      {hours[item.id] &&
                        hours[item.id].map((it: any, int: number) => (
                          <div key={it.id} className={styles["hours-it"]}>
                            {learnHourRecord[it.id] && (
                              <HourCompenent
                                id={it.id}
                                cid={item.course_id}
                                title={it.title}
                                record={learnHourRecord[it.id]}
                                duration={it.duration}
                                progress={
                                  (learnHourRecord[it.id].finished_duration *
                                    100) /
                                  learnHourRecord[it.id].total_duration
                                }
                              ></HourCompenent>
                            )}
                            {!learnHourRecord[it.id] && (
                              <HourCompenent
                                id={it.id}
                                cid={item.course_id}
                                title={it.title}
                                record={null}
                                duration={it.duration}
                                progress={0}
                              ></HourCompenent>
                            )}
                          </div>
                        ))}
                    </div>
                  ))}
                </div>
              )}
            </div>
          )}
          {tabKey === 2 && (
            <div className={styles["attachments-cont"]}>
              {attachments.map((item: any, index: number) => (
                <div key={index} className={styles["attachments-item"]}>
                  <div className={styles["left-cont"]}>
                    <i
                      className="iconfont icon-icon-file"
                      style={{
                        fontSize: 16,
                        color: "rgba(0,0,0,0.3)",
                        marginRight: 10,
                      }}
                    />
                    <span className={styles["title"]}>
                      {item.title}.{item.ext}
                    </span>
                  </div>
                  <div
                    className={styles["download"]}
                    onClick={() =>
                      downLoadFile(
                        item.course_id,
                        item.id,
                        item.rid,
                        `${item.title}.${item.ext}`,
                        item.type
                      )
                    }
                  >
                    下载
                  </div>
                </div>
              ))}
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default CoursePage;
