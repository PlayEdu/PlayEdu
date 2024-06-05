import { useEffect, useState } from "react";
import { Row, Col, Spin, Tree, Popover, Space, Image } from "antd";
import { useNavigate, useLocation } from "react-router-dom";
import { user } from "../../api/index";
import styles from "./index.module.scss";
import { useSelector } from "react-redux";
import { CoursesModel } from "./compenents/courses-model";
import { Empty } from "../../compenents";
import myLesoon from "../../assets/images/commen/icon-mylesoon.png";
import studyTime from "../../assets/images/commen/icon-studytime.png";
import iconRoute from "../../assets/images/commen/icon-route.png";
import { studyTimeFormat } from "../../utils/index";

type StatsModel = {
  learn_duration: number;
  nun_required_course_count: number;
  nun_required_finished_course_count: number;
  nun_required_finished_hour_count: number;
  nun_required_hour_count: number;
  required_course_count: number;
  required_finished_course_count: number;
  required_finished_hour_count: number;
  required_hour_count: number;
  today_learn_duration: number;
};

type LearnCourseRecordsModel = {
  [key: number]: CourseRecordModel;
};

type CategoryModel = {
  key: number;
  title: any;
  children?: CategoryModel[];
};

const IndexPage = () => {
  const navigate = useNavigate();
  const result = new URLSearchParams(useLocation().search);
  const systemConfig = useSelector((state: any) => state.systemConfig.value);
  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState<boolean>(false);
  const [tabKey, setTabKey] = useState(Number(result.get("tab") || 0));
  const [coursesList, setCoursesList] = useState<CourseModel[]>([]);
  const [categories, setCategories] = useState<CategoryModel[]>([]);
  const [categoryId, setCategoryId] = useState<number>(
    Number(result.get("cid") || 0)
  );
  const [categoryText, setCategoryText] = useState<string>(
    String(result.get("catName") || "所有分类")
  );
  const [selectKey, setSelectKey] = useState<any>([0]);
  const [learnCourseRecords, setLearnCourseRecords] =
    useState<LearnCourseRecordsModel>({});
  const [learnCourseHourCount, setLearnCourseHourCount] = useState<any>({});
  const [stats, setStats] = useState<StatsModel | null>(null);
  const currentDepId = useSelector(
    (state: any) => state.loginUser.value.currentDepId
  );

  useEffect(() => {
    getParams();
  }, []);

  useEffect(() => {
    let arr = [];
    arr.push(Number(result.get("cid") || 0));
    setSelectKey(arr);
  }, [result.get("cid")]);

  useEffect(() => {
    if (currentDepId === 0) {
      return;
    }
    getData();
  }, [tabKey, currentDepId, categoryId]);

  useEffect(() => {
    document.title = systemConfig.systemName || "首页";
  }, [systemConfig]);

  const hide = () => {
    setOpen(false);
  };

  const getData = () => {
    setLoading(true);
    user.courses(currentDepId, categoryId).then((res: any) => {
      const records: LearnCourseRecordsModel = res.data.learn_course_records;
      setStats(res.data.stats);
      setLearnCourseRecords(records);
      setLearnCourseHourCount(res.data.user_course_hour_count);
      if (tabKey === 0) {
        setCoursesList(res.data.courses);
      } else if (tabKey === 1) {
        const arr: CourseModel[] = [];
        res.data.courses.map((item: any) => {
          if (item.is_required === 1) {
            arr.push(item);
          }
        });
        setCoursesList(arr);
      } else if (tabKey === 2) {
        const arr: CourseModel[] = [];
        res.data.courses.map((item: any) => {
          if (item.is_required === 0) {
            arr.push(item);
          }
        });
        setCoursesList(arr);
      } else if (tabKey === 3) {
        const arr: CourseModel[] = [];
        res.data.courses.map((item: any) => {
          if (records[item.id] && records[item.id].progress >= 10000) {
            arr.push(item);
          }
        });
        setCoursesList(arr);
      } else if (tabKey === 4) {
        const arr: CourseModel[] = [];
        res.data.courses.map((item: any) => {
          if (
            !records[item.id] ||
            (records[item.id] && records[item.id].progress < 10000)
          ) {
            arr.push(item);
          }
        });
        setCoursesList(arr);
      }
      setLoading(false);
    });
  };

  const getParams = () => {
    user.coursesCategories().then((res: any) => {
      const categories = res.data.categories;
      if (JSON.stringify(categories) !== "{}") {
        const new_arr: CategoryModel[] = checkArr(categories, 0);
        new_arr.unshift({
          key: 0,
          title: "所有分类",
        });
        setCategories(new_arr);
      }
    });
  };

  const checkArr = (categories: any[], id: number) => {
    const arr: CategoryModel[] = [];
    for (let i = 0; i < categories[id].length; i++) {
      if (!categories[categories[id][i].id]) {
        arr.push({
          title: (
            <span style={{ marginRight: 20 }}>{categories[id][i].name}</span>
          ),
          key: categories[id][i].id,
        });
      } else {
        const new_arr: CategoryModel[] = checkArr(
          categories,
          categories[id][i].id
        );
        arr.push({
          title: (
            <span style={{ marginRight: 20 }}>{categories[id][i].name}</span>
          ),
          key: categories[id][i].id,
          children: new_arr,
        });
      }
    }
    return arr;
  };

  const items = [
    {
      key: 0,
      label: `全部`,
    },
    {
      key: 1,
      label: `必修课`,
    },
    {
      key: 2,
      label: `选修课`,
    },
    {
      key: 3,
      label: `已学完`,
    },
    {
      key: 4,
      label: `未学完`,
    },
  ];

  const onChange = (key: number) => {
    setTabKey(key);
    navigate(
      "/?cid=" + categoryId + "&catName=" + categoryText + "&tab=" + key
    );
  };

  const onSelect = (selectedKeys: any, info: any) => {
    setCategoryId(selectedKeys[0]);
    if (info.node.key === 0) {
      setCategoryText(info.node.title);
      setSelectKey(selectedKeys);
      hide();
      navigate(
        "/?cid=" +
          selectedKeys[0] +
          "&catName=" +
          info.node.title +
          "&tab=" +
          tabKey
      );
    } else {
      setCategoryText(info.node.title.props.children);
      setSelectKey(selectedKeys);
      hide();
      navigate(
        "/?cid=" +
          selectedKeys[0] +
          "&catName=" +
          info.node.title.props.children +
          "&tab=" +
          tabKey
      );
    }
  };

  const handleOpenChange = (newOpen: boolean) => {
    setOpen(newOpen);
  };

  const dropItem = (
    <div
      style={{
        maxHeight: 600,
        overflowX: "hidden",
        overflowY: "auto",
      }}
    >
      <Tree
        selectedKeys={selectKey}
        switcherIcon={null}
        onSelect={onSelect}
        treeData={categories}
        blockNode
        defaultExpandAll={true}
      />
    </div>
  );

  return (
    <div className="main-body">
      <div className="content">
        <div className={styles["top-cont"]}>
          <div className={styles["top-item"]}>
            <div className={styles["title"]}>
              <img className={styles["icon"]} src={myLesoon} />
              <span>课程进度</span>
            </div>
            <div className={styles["info"]}>
              <div className={styles["info-item"]}>
                <span>必修课：已学完课程</span>
                <strong> {stats?.required_finished_course_count || 0} </strong>
                <span>/ {stats?.required_course_count || 0}</span>
              </div>
              {stats && stats.nun_required_course_count > 0 && (
                <div className={styles["info-item"]}>
                  <span>选修课：已学完课程</span>
                  <strong>
                    {" "}
                    {stats?.nun_required_finished_course_count || 0}{" "}
                  </strong>
                  <span>/ {stats?.nun_required_course_count || 0}</span>
                </div>
              )}
            </div>
          </div>
          <div className={styles["top-item"]}>
            <div className={styles["title"]}>
              <img className={styles["icon"]} src={studyTime} />
              <span>学习时长</span>
            </div>
            {stats ? (
              <div className={styles["info"]}>
                <div className={styles["info-item"]}>
                  今日：
                  {studyTimeFormat(stats.today_learn_duration)[0] !== 0 && (
                    <>
                      <strong>
                        {" "}
                        {studyTimeFormat(stats.today_learn_duration)[0] ||
                          0}{" "}
                      </strong>
                      小时
                    </>
                  )}
                  <strong>
                    {" "}
                    {studyTimeFormat(stats.today_learn_duration)[1] || 0}{" "}
                  </strong>
                  分钟
                </div>
                <div className={styles["info-item"]}>
                  累计：
                  {studyTimeFormat(stats.learn_duration || 0)[0] !== 0 && (
                    <>
                      <strong>
                        {" "}
                        {studyTimeFormat(stats.learn_duration || 0)[0] ||
                          0}{" "}
                      </strong>
                      小时
                    </>
                  )}
                  <strong>
                    {" "}
                    {studyTimeFormat(stats.learn_duration || 0)[1] || 0}{" "}
                  </strong>
                  分钟
                </div>
              </div>
            ) : null}
          </div>
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
          <Popover
            content={dropItem}
            placement="bottomRight"
            open={open}
            trigger="click"
            onOpenChange={handleOpenChange}
          >
            <Space className={styles["dropButton"]}>
              {categoryText}
              <i
                className="iconfont icon-icon-xiala"
                style={{ fontSize: 16 }}
              />
            </Space>
          </Popover>
        </div>
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

        {!loading && coursesList.length === 0 && (
          <Row
            style={{
              width: 1200,
              margin: "0 auto",
              paddingTop: 14,
              minHeight: 301,
            }}
          >
            <Col span={24}>
              <Empty />
            </Col>
          </Row>
        )}
        {!loading && coursesList.length > 0 && (
          <div className={styles["courses-list"]}>
            {coursesList.map((item: any) => (
              <div key={item.id}>
                {learnCourseRecords[item.id] && (
                  <CoursesModel
                    id={item.id}
                    title={item.title}
                    thumb={item.thumb}
                    isRequired={item.is_required}
                    progress={Math.floor(
                      learnCourseRecords[item.id].progress / 100
                    )}
                  ></CoursesModel>
                )}

                {!learnCourseRecords[item.id] &&
                  learnCourseHourCount[item.id] &&
                  learnCourseHourCount[item.id] > 0 && (
                    <CoursesModel
                      id={item.id}
                      title={item.title}
                      thumb={item.thumb}
                      isRequired={item.is_required}
                      progress={1}
                    ></CoursesModel>
                  )}
                {!learnCourseRecords[item.id] &&
                  !learnCourseHourCount[item.id] && (
                    <CoursesModel
                      id={item.id}
                      title={item.title}
                      thumb={item.thumb}
                      isRequired={item.is_required}
                      progress={0}
                    ></CoursesModel>
                  )}
              </div>
            ))}
          </div>
        )}
      </div>
      <div className={styles["extra"]}>{systemConfig.pcIndexFooterMsg}</div>
    </div>
  );
};

export default IndexPage;
