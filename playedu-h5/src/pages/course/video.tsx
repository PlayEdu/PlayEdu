import { useEffect, useRef, useState } from "react";
import styles from "./video.module.scss";
import { useParams, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { course as Course } from "../../api/index";
import { Toast, Image } from "antd-mobile";
import backIcon from "../../assets/images/commen/icon-back-n.png";
import { Empty } from "../../components";
import { HourCompenent } from "./compenents/videoHour";

declare const window: any;

type LocalUserLearnHourRecordModel = {
  [key: number]: UserLearnHourRecordModel;
};

type LocalCourseHour = {
  [key: number]: CourseHourModel[];
};

const CoursePlayPage = () => {
  const navigate = useNavigate();
  const params = useParams();
  const systemConfig = useSelector((state: any) => state.systemConfig.value);
  const user = useSelector((state: any) => state.loginUser.value.user);
  const [playUrl, setPlayUrl] = useState("");
  const [playDuration, setPlayDuration] = useState(0);
  const [playendedStatus, setPlayendedStatus] = useState(false);
  const [lastSeeValue, setLastSeeValue] = useState({});
  const [course, setCourse] = useState<CourseModel | null>(null);
  const [hour, setHour] = useState<any>({});
  const [loading, setLoading] = useState(false);
  const [isLastpage, setIsLastpage] = useState(false);
  const [totalHours, setTotalHours] = useState<any>([]);
  const [playingTime, setPlayingTime] = useState(0);
  const [watchedSeconds, setWatchedSeconds] = useState(0);
  const [chapters, setChapters] = useState<ChapterModel[]>([]);
  const [hours, setHours] = useState<LocalCourseHour | null>(null);
  const [learnHourRecord, setLearnHourRecord] =
    useState<LocalUserLearnHourRecordModel>({});
  const myRef = useRef(0);
  const playRef = useRef(0);
  const watchRef = useRef(0);
  const totalRef = useRef(0);

  useEffect(() => {
    getCourse();
    getDetail();
  }, [params.courseId, params.hourId]);

  useEffect(() => {
    myRef.current = playDuration;
  }, [playDuration]);

  useEffect(() => {
    playRef.current = playingTime;
  }, [playingTime]);

  useEffect(() => {
    watchRef.current = watchedSeconds;
  }, [watchedSeconds]);

  useEffect(() => {
    totalRef.current = hour.duration;
  }, [hour]);

  const getCourse = () => {
    Course.detail(Number(params.courseId)).then((res: any) => {
      setChapters(res.data.chapters);
      setHours(res.data.hours);
      if (res.data.learn_hour_records) {
        setLearnHourRecord(res.data.learn_hour_records);
      }
      let totalHours: any = [];
      if (res.data.chapters.length === 0) {
        setTotalHours(res.data.hours[0]);
        totalHours = res.data.hours[0];
      } else if (res.data.chapters.length > 0) {
        const arr: any = [];
        for (let key in res.data.hours) {
          res.data.hours[key].map((item: any) => {
            arr.push(item);
          });
        }
        setTotalHours(arr);
        totalHours = arr;
      }
      //判断是否是最后的课时
      const index = totalHours.findIndex(
        (i: any) => i.id === Number(params.hourId)
      );
      if (index === totalHours.length - 1) {
        setIsLastpage(true);
      } else {
        setIsLastpage(false);
      }
    });
  };

  const getDetail = () => {
    if (loading) {
      return true;
    }
    setLoading(true);
    Course.play(Number(params.courseId), Number(params.hourId))
      .then((res: any) => {
        let courseItem: CourseModel = res.data.course;
        setCourse(courseItem);
        setHour(res.data.hour);
        document.title = res.data.hour.title;
        let record = res.data.user_hour_record;
        let params = null;
        if (record && record.finished_duration && record.is_finished === 0) {
          params = {
            time: 5,
            pos: record.finished_duration,
          };
          setLastSeeValue(params);
          setWatchedSeconds(record.finished_duration);
        } else if (record && record.is_finished === 1) {
          setWatchedSeconds(res.data.hour.duration);
        }
        getVideoUrl(res.data.hour.rid, params);
        setLoading(false);
      })
      .catch((e) => {
        setLoading(false);
      });
  };

  const getVideoUrl = (rid: number, data: any) => {
    Course.playUrl(Number(params.courseId), Number(params.hourId)).then(
      (res: any) => {
        window.player && window.player.destroy();
        setPlayUrl(res.data.resource_url[rid]);
        initDPlayer(res.data.resource_url[rid], 0, data);
      }
    );
  };

  const initDPlayer = (playUrl: string, isTrySee: number, params: any) => {
    let banDrag =
      systemConfig.playerIsDisabledDrag &&
      watchRef.current < totalRef.current &&
      watchRef.current === 0;
    window.player = new window.DPlayer({
      container: document.getElementById("meedu-player-container"),
      autoplay: false,
      video: {
        url: playUrl,
        pic: systemConfig.playerPoster,
      },
      try: isTrySee === 1,
      bulletSecret: {
        enabled: systemConfig.playerIsEnabledBulletSecret,
        text: systemConfig.playerBulletSecretText
          .replace("{name}", user.name)
          .replace("{email}", user.email)
          .replace("{idCard}", user.id_card),
        size: "14px",
        color: systemConfig.playerBulletSecretColor || "red",
        opacity: Number(systemConfig.playerBulletSecretOpacity),
      },
      ban_drag: banDrag,
      last_see_pos: params,
    });
    // 监听播放进度更新evt
    window.player.on("timeupdate", () => {
      let currentTime = parseInt(window.player.video.currentTime);
      if (
        systemConfig.playerIsDisabledDrag &&
        watchRef.current < totalRef.current &&
        currentTime - playRef.current >= 2 &&
        currentTime > watchRef.current
      ) {
        Toast.show("首次学习禁止快进");
        window.player.seek(watchRef.current);
      } else {
        setPlayingTime(currentTime);
        playTimeUpdate(parseInt(window.player.video.currentTime), false);
      }
    });
    window.player.on("ended", () => {
      if (
        systemConfig.playerIsDisabledDrag &&
        watchRef.current < totalRef.current &&
        window.player.video.duration - playRef.current >= 2
      ) {
        window.player.seek(playRef.current);
        return;
      }
      setPlayendedStatus(true);
      setPlayingTime(0);
      playTimeUpdate(parseInt(window.player.video.currentTime), true);
      exitFullscreen();
      window.player && window.player.destroy();
    });
    setLoading(false);
  };

  const playTimeUpdate = (duration: number, isEnd: boolean) => {
    if (duration - myRef.current >= 10 || isEnd === true) {
      setPlayDuration(duration);
      Course.record(
        Number(params.courseId),
        Number(params.hourId),
        duration
      ).then((res: any) => {});
      Course.playPing(Number(params.courseId), Number(params.hourId)).then(
        (res: any) => {}
      );
    }
  };

  const goNextVideo = () => {
    const index = totalHours.findIndex(
      (i: any) => i.id === Number(params.hourId)
    );
    if (index === totalHours.length - 1) {
      setIsLastpage(true);
      Toast.show("已经是最后一节了！");
    } else if (index < totalHours.length - 1) {
      setIsLastpage(false);
      navigate(`/course/${params.courseId}/hour/${totalHours[index + 1].id}`, {
        replace: true,
      });
    }
  };

  const exitFullscreen = () => {
    let de: any;
    de = document;
    if (de.fullscreenElement !== null) {
      de.exitFullscreen();
    } else if (de.mozCancelFullScreen) {
      de.mozCancelFullScreen();
    } else if (de.webkitCancelFullScreen) {
      de.webkitCancelFullScreen();
    }
  };

  const playVideo = (cid: number, id: number) => {
    navigate(`/course/${cid}/hour/${id}`, { replace: true });
  };

  return (
    <div className="main-body">
      <div className={styles["video-body"]}>
        <Image
          className={styles["back-icon"]}
          src={backIcon}
          onClick={() => {
            window.player && window.player.destroy();
            navigate(-1);
          }}
        />
        <div className={styles["video-box"]}>
          <div
            className="play-box"
            style={{ display: playendedStatus ? "none" : "block" }}
            id="meedu-player-container"
          ></div>
          {playendedStatus && (
            <div className={styles["alert-message"]}>
              {isLastpage && (
                <div
                  className={styles["alert-button"]}
                  onClick={() => {
                    navigate(-1);
                  }}
                >
                  恭喜你学完最后一节
                </div>
              )}
              {!isLastpage && (
                <div
                  className={styles["alert-button"]}
                  onClick={() => {
                    setLastSeeValue({});
                    setPlayendedStatus(false);
                    goNextVideo();
                  }}
                >
                  播放下一节
                </div>
              )}
            </div>
          )}
        </div>
      </div>
      <div className={styles["chapters-hours-cont"]}>
        {chapters.length === 0 && !hours && <Empty />}
        {chapters.length === 0 && hours && (
          <div className={styles["hours-list-box"]} style={{ marginTop: 10 }}>
            {hours[0].map((item: CourseHourModel) => (
              <div key={item.id} className={styles["hours-it"]}>
                <HourCompenent
                  id={item.id}
                  cid={item.course_id}
                  title={item.title}
                  record={learnHourRecord[item.id]}
                  duration={item.duration}
                  vid={Number(params.hourId)}
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
                      vid={Number(params.hourId)}
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
    </div>
  );
};

export default CoursePlayPage;
