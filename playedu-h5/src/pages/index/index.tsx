import { useEffect, useRef, useState } from "react";
import { Dropdown, PullToRefresh, Skeleton, Tabs } from "antd-mobile";
import { sleep } from "antd-mobile/es/utils/sleep";
import { DropdownRef } from "antd-mobile/es/components/dropdown";
import { user } from "../../api/index";
import styles from "./index.module.scss";
import { useSelector } from "react-redux";
import { useNavigate, useLocation } from "react-router-dom";
import { Footer, Empty } from "../../components";
import { CoursesModel } from "./compenents/courses-model";
import { isEmptyObject } from "../../utils/index";

type LocalUserLearnHourRecordModel = {
  [key: number]: UserLearnHourRecordModel;
};

type LocalUserLearnHourCountModel = {
  [key: number]: number;
};

const IndexPage = () => {
  const ref = useRef<DropdownRef>(null);
  const navigate = useNavigate();
  const result = new URLSearchParams(useLocation().search);
  const [loading, setLoading] = useState(false);
  const [tabKey, setTabKey] = useState(result.get("tab") || "0");
  const [coursesList, setCoursesList] = useState<CourseModel[]>([]);
  const [categories, setCategories] = useState<any>([]);
  const [categoryId, setCategoryId] = useState<number>(
    Number(result.get("cid") || 0)
  );
  const [categoryText, setCategoryText] = useState<string>(
    String(result.get("catName") || "所有分类")
  );
  const [learnCourseRecords, setLearnCourseRecords] =
    useState<LocalUserLearnHourRecordModel>({});
  const [learnCourseHourCount, setLearnCourseHourCount] =
    useState<LocalUserLearnHourCountModel>({});
  const systemConfig = useSelector((state: any) => state.systemConfig.value);
  const currentDepId = useSelector(
    (state: any) => state.loginUser.value.currentDepId
  );
  const items = [
    {
      key: "0",
      label: `全部`,
    },
    {
      key: "1",
      label: `必修课`,
    },
    {
      key: "2",
      label: `选修课`,
    },
    {
      key: "3",
      label: `已学完`,
    },
    {
      key: "4",
      label: `未学完`,
    },
  ];

  useEffect(() => {
    document.title = systemConfig.systemName || "首页";
  }, [systemConfig]);

  useEffect(() => {
    getParams();
  }, []);

  useEffect(() => {
    setLoading(true);
    if (currentDepId === 0) {
      setLoading(false);
      return;
    }
    getData();
  }, [currentDepId, categoryId, tabKey]);

  const getData = () => {
    user.courses(currentDepId, categoryId).then((res: any) => {
      const records = res.data.learn_course_records;
      setLearnCourseRecords(records);
      setLearnCourseHourCount(res.data.user_course_hour_count);
      if (Number(tabKey) === 0) {
        setCoursesList(res.data.courses);
      } else if (Number(tabKey) === 1) {
        const arr: any = [];
        res.data.courses.map((item: any) => {
          if (item.is_required === 1) {
            arr.push(item);
          }
        });
        setCoursesList(arr);
      } else if (Number(tabKey) === 2) {
        const arr: any = [];
        res.data.courses.map((item: any) => {
          if (item.is_required === 0) {
            arr.push(item);
          }
        });
        setCoursesList(arr);
      } else if (Number(tabKey) === 3) {
        const arr: any = [];
        res.data.courses.map((item: any) => {
          if (records[item.id] && records[item.id].progress >= 10000) {
            arr.push(item);
          }
        });
        setCoursesList(arr);
      } else if (Number(tabKey) === 4) {
        const arr: any = [];
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
      if (!isEmptyObject(categories)) {
        const new_arr: any[] = checkArr(categories, 0);
        new_arr.unshift({
          key: 0,
          title: "所有分类",
        });
        setCategories(new_arr);
      }
    });
  };

  const checkArr = (categories: any[], id: number) => {
    const arr = [];
    for (let i = 0; i < categories[id].length; i++) {
      if (!categories[categories[id][i].id]) {
        arr.push({
          title: categories[id][i].name,
          key: categories[id][i].id,
        });
      } else {
        const new_arr: any[] = checkArr(categories, categories[id][i].id);
        arr.push({
          title: categories[id][i].name,
          key: categories[id][i].id,
          children: new_arr,
        });
      }
    }
    return arr;
  };

  const renderChildCategory = (data: any) => {
    return (
      <>
        {data.map((item: any) => (
          <div key={item.key} className={styles["child-item"]}>
            <div
              className={
                item.key === categoryId
                  ? styles["act-category-child-tit"]
                  : styles["category-child-tit"]
              }
              onClick={() => {
                setCategoryId(item.key);
                setCategoryText(item.title);
                navigate(
                  "/?cid=" +
                    item.key +
                    "&catName=" +
                    item.title +
                    "&tab=" +
                    tabKey
                );
                ref.current?.close();
              }}
            >
              {item.title}
            </div>
            {item.children &&
              item.children.length > 0 &&
              renderChildCategory(item.children)}
          </div>
        ))}
      </>
    );
  };

  return (
    <div className="main-body">
      <div className={styles["tabs-box"]}>
        <Tabs
          activeKey={tabKey}
          onChange={(key: any) => {
            setTabKey(key);
            setTimeout(() => {
              navigate(
                "/?cid=" +
                  categoryId +
                  "&catName=" +
                  categoryText +
                  "&tab=" +
                  key
              );
            }, 250);
          }}
          style={{
            "--fixed-active-line-width": "20px",
            "--active-line-height": "3px",
            "--active-title-color": "rgba(0,0,0,0.88)",
            "--title-font-size": "16px",
          }}
        >
          {items.map((item) => (
            <Tabs.Tab title={item.label} key={item.key} />
          ))}
        </Tabs>
      </div>
      <div className={styles["category-content"]}>
        <Dropdown ref={ref}>
          <Dropdown.Item key="sorter" title={categoryText}>
            <div className={styles["category-box"]}>
              {categories.map((item: any) => (
                <div
                  key={item.key}
                  className={
                    item.key === categoryId
                      ? styles["active-category-item"]
                      : styles["category-item"]
                  }
                >
                  <div
                    className={styles["category-tit"]}
                    onClick={() => {
                      setCategoryId(item.key);
                      setCategoryText(item.title);
                      navigate(
                        "/?cid=" +
                          item.key +
                          "&catName=" +
                          item.title +
                          "&tab=" +
                          tabKey
                      );
                      ref.current?.close();
                    }}
                  >
                    {item.title}
                  </div>
                  {item.children &&
                    item.children.length > 0 &&
                    renderChildCategory(item.children)}
                </div>
              ))}
            </div>
          </Dropdown.Item>
        </Dropdown>
      </div>
      <div
        className="float-left"
        style={{ position: "relative", paddingTop: 96 }}
      >
        <PullToRefresh
          onRefresh={async () => {
            setLoading(true);
            await sleep(700);
            getData();
          }}
        >
          <div className={styles["list-box"]}>
            {loading &&
              Array.from({ length: 2 }).map((_, i) => (
                <div
                  style={{
                    width: "100%",
                    height: 75,
                    display: "flex",
                    alignItems: "center",
                    marginBottom: 30,
                    marginTop: 30,
                  }}
                  key={i}
                >
                  <Skeleton
                    animated
                    style={{
                      width: 100,
                      height: 75,
                      borderRadius: 8,
                      marginRight: 15,
                    }}
                  />
                  <div
                    style={{
                      flex: 1,
                      height: 75,
                      display: "flex",
                      flexDirection: "column",
                      justifyContent: "space-between",
                    }}
                  >
                    <Skeleton animated style={{ width: "100%", height: 21 }} />
                    <Skeleton animated style={{ width: "100%", height: 24 }} />
                  </div>
                </div>
              ))}
            {!loading && coursesList.length === 0 && <Empty></Empty>}
            {!loading && coursesList.length > 0 && (
              <>
                {coursesList.map((item: any) => (
                  <div className={styles["item"]} key={item.id}>
                    <CoursesModel
                      id={item.id}
                      title={item.title}
                      thumb={item.thumb}
                      isRequired={item.is_required}
                      record={learnCourseRecords[item.id]}
                      hourCount={learnCourseHourCount[item.id]}
                    ></CoursesModel>
                  </div>
                ))}
                <Footer></Footer>
              </>
            )}
          </div>{" "}
        </PullToRefresh>
      </div>
    </div>
  );
};

export default IndexPage;
