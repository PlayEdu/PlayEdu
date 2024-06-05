import { useState, useEffect, useRef } from "react";
import styles from "./learn.module.less";
import { Row, Image, Table, Button, Select } from "antd";
import { useLocation, useNavigate } from "react-router-dom";
import { BackBartment, DurationText } from "../../compenents";
import { dateFormat } from "../../utils/index";
import { user as member } from "../../api/index";
import * as echarts from "echarts";
import type { ColumnsType } from "antd/es/table";
import { MemberLearnProgressDialog } from "./compenents/progress";

interface DataType {
  id: React.Key;
  charge: number;
  class_hour: number;
  created_at: string;
  is_required: number;
  is_show: number;
  short_desc: string;
  thumb: string;
  title: string;
}

type UserCourseRecordsModel = {
  [key: number]: UserRecordModel;
};

type UserRecordModel = {
  course_id: number;
  created_at: string;
  finished_at?: string;
  finished_count: number;
  hour_count: number;
  id: number;
  is_finished: number;
  progress: number;
  updated_at: string;
  user_id: number;
};

type HourCountModel = {
  [key: number]: number;
};

type OptionModel = {
  label: string;
  value: string;
};

type DepartmentsListModel = {
  reated_at: string;
  id: number;
  name: string;
  parent_chain: string;
  parent_id: number;
  sort: number;
  updated_at: string;
};

type PerCourseRecordsModel = {
  [key: number]: {
    course_id: number;
    created_at: string;
    finished_at: string;
    finished_duration: number;
    hour_id: number;
    id: number;
    is_finished: number;
    real_duration: number;
    total_duration: number;
    updated_at: string;
    user_id: number;
  };
};

const MemberLearnPage = () => {
  let chartRef = useRef(null);
  const navigate = useNavigate();
  const result = new URLSearchParams(useLocation().search);
  const [loading2, setLoading2] = useState(false);
  const [list2, setList2] = useState<DepartmentsListModel[]>([]);
  const [courses, setCourses] = useState<any>({});
  const [deps, setDeps] = useState<OptionModel[]>([]);
  const [depValue, setDepValue] = useState(0);
  const [currentCourses, setCurrentCourses] = useState<DataType[]>([]);
  const [openCourses, setOpenCourses] = useState<CourseModel[]>([]);
  const [records, setRecords] = useState<UserCourseRecordsModel>({});
  const [perRecords, setPerRecords] = useState<PerCourseRecordsModel>({});
  const [hourCount, setHourCount] = useState<HourCountModel>({});
  const [total2, setTotal2] = useState(0);
  const [refresh2, setRefresh2] = useState(false);
  const [uid, setUid] = useState(Number(result.get("id")));
  const [userName, setUserName] = useState<string>(String(result.get("name")));
  const [visiable, setVisiable] = useState(false);
  const [courseId, setcourseId] = useState(0);

  useEffect(() => {
    setUid(Number(result.get("id")));
    setUserName(String(result.get("name")));
    setLoading2(false);
    setRefresh2(!refresh2);
  }, [result.get("id"), result.get("name")]);

  useEffect(() => {
    getZxtData();
    return () => {
      window.onresize = null;
    };
  }, [uid]);

  useEffect(() => {
    getLearnCourses();
  }, [refresh2, uid]);

  useEffect(() => {
    if (depValue === 0) {
      return;
    }
    let arr = [...courses[depValue]];
    let arr2 = [...openCourses];
    if (arr2.length > 0) {
      var data = arr.concat(arr2);
      setCurrentCourses(data);
    } else {
      setCurrentCourses(arr);
    }
  }, [depValue]);

  const getZxtData = () => {
    member.learnStats(uid).then((res: any) => {
      renderView(res.data);
    });
  };

  const minuteFormat = (duration: number) => {
    if (duration === 0) {
      return "0小时0分0秒";
    }
    let h = Math.trunc(duration / 3600);
    let m = Math.trunc((duration % 3600) / 60);
    let s = Math.trunc((duration % 3600) % 60);
    return h + "小时" + m + "分" + s + "秒";
  };

  const renderView = (params: any) => {
    const timeData: any = [];
    const valueData: any = [];
    params.map((item: any) => {
      timeData.push(item.key);
      valueData.push(item.value / 1000);
    });
    let dom: any = chartRef.current;
    let myChart = echarts.init(dom);
    myChart.setOption({
      tooltip: {
        trigger: "axis",
        formatter: function (params: any) {
          //  只粘贴formatter了
          let relVal = params[0].axisValueLabel;
          for (let i = 0; i < params.length; i++) {
            relVal +=
              "<br/>" +
              params[i].marker +
              params[i].seriesName +
              ":  " +
              minuteFormat(params[i].value);
          }
          return relVal;
        },
      },
      legend: {
        data: ["每日学习时长"],
        x: "right",
      },
      grid: {
        left: "3%",
        right: "4%",
        bottom: "3%",
        containLabel: true,
      },
      xAxis: {
        type: "category",
        boundaryGap: false,
        data: timeData,
      },
      yAxis: {
        type: "value",
      },
      series: [
        {
          name: "每日学习时长",
          type: "line",
          data: valueData,
          color: "#ff4d4f",
        },
      ],
    });
    window.onresize = () => {
      myChart.resize();
    };
  };

  const getLearnCourses = () => {
    if (loading2) {
      return;
    }
    setLoading2(true);
    member.learnAllCourses(uid).then((res: any) => {
      setList2(res.data.departments);
      setCourses(res.data.dep_courses);
      setOpenCourses(res.data.open_courses);
      setHourCount(res.data.user_course_hour_count);
      setRecords(res.data.user_course_records);
      setPerRecords(res.data.per_course_earliest_records);
      if (res.data.departments.length > 0) {
        let box: OptionModel[] = [];
        res.data.departments.map((item: any) => {
          box.push({
            label: item.name,
            value: String(item.id),
          });
        });
        setDepValue(Number(box[0].value));
        setDeps(box);
      } else {
        setDepValue(0);
        setDeps([]);
      }
      setLoading2(false);
    });
  };

  const column2: ColumnsType<DataType> = [
    {
      title: "课程名称",
      dataIndex: "title",
      render: (_, record: any) => (
        <div className="d-flex">
          <Image
            src={record.thumb}
            preview={false}
            width={80}
            height={60}
            style={{ borderRadius: 6 }}
          />
          <span className="ml-8">{record.title}</span>
        </div>
      ),
    },
    {
      title: "课程进度",
      dataIndex: "total_duration",
      render: (_, record: any) => (
        <>
          <span>
            已完成课时：
            {(records[record.id] && records[record.id].finished_count) ||
              0} / {record.class_hour}
          </span>
        </>
      ),
    },
    {
      title: "第一次学习时间",
      dataIndex: "created_at",
      render: (_, record: any) => (
        <>
          {perRecords[record.id] ? (
            <span>{dateFormat(perRecords[record.id].created_at)}</span>
          ) : (
            <span>-</span>
          )}
        </>
      ),
    },
    {
      title: "学习完成时间",
      dataIndex: "finished_at",
      render: (_, record: any) => (
        <>
          {records[record.id] && records[record.id].finished_at ? (
            <span>{dateFormat(String(records[record.id].finished_at))}</span>
          ) : (
            <span>-</span>
          )}
        </>
      ),
    },
    {
      title: "学习进度",
      render: (_, record: any) => (
        <>
          {records[record.id] ? (
            <span
              className={
                Math.floor(
                  (records[record.id].finished_count /
                    records[record.id].hour_count) *
                    100
                ) >= 100
                  ? "c-green"
                  : "c-red"
              }
            >
              {Math.floor(
                (records[record.id].finished_count /
                  records[record.id].hour_count) *
                  100
              )}
              %
            </span>
          ) : hourCount[record.id] && hourCount[record.id] > 0 ? (
            <span className="c-red">1%</span>
          ) : (
            <span className="c-red">0%</span>
          )}
        </>
      ),
    },
    {
      title: "操作",
      key: "action",
      fixed: "right",
      width: 100,
      render: (_, record: any) => (
        <Button
          type="link"
          className="b-link c-red"
          onClick={() => {
            setcourseId(record.id);
            setVisiable(true);
          }}
        >
          明细
        </Button>
      ),
    },
  ];

  return (
    <>
      <Row className="playedu-main-top mb-24">
        <MemberLearnProgressDialog
          open={visiable}
          uid={uid}
          id={courseId}
          onCancel={() => {
            setVisiable(false);
            setRefresh2(!refresh2);
          }}
        ></MemberLearnProgressDialog>
        <div className="float-left mb-24">
          <BackBartment title={userName + "的学习明细"} />
        </div>
        <div className={styles["charts"]}>
          <div
            ref={chartRef}
            style={{
              width: "100% !important",
              height: 300,
              position: "relative",
            }}
          ></div>
        </div>
        <div className="float-left mt-24">
          {list2.length > 1 && (
            <div className="d-flex mb-24">
              <span>切换部门：</span>
              <Select
                style={{ width: 160 }}
                allowClear
                placeholder="请选择部门"
                value={String(depValue)}
                onChange={(value: string) => setDepValue(Number(value))}
                options={deps}
              />
            </div>
          )}
          <Table
            columns={column2}
            dataSource={currentCourses}
            loading={loading2}
            pagination={false}
            rowKey={(record) => record.id}
          />
        </div>
      </Row>
    </>
  );
};
export default MemberLearnPage;
