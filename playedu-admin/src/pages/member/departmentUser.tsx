import { useState, useEffect } from "react";
import styles from "./departmentUser.module.less";
import {
  Typography,
  Input,
  Modal,
  Image,
  Button,
  Space,
  message,
  Table,
  Select,
} from "antd";
import { useNavigate, useLocation } from "react-router-dom";
import { BackBartment, DurationText } from "../../compenents";
import { dateFormat } from "../../utils/index";
import { user as member } from "../../api/index";
const { Column, ColumnGroup } = Table;
import * as XLSX from "xlsx";

interface DataType {
  id: React.Key;
  title: string;
  type: string;
  created_at: string;
  total_duration: number;
  finished_duration: number;
  is_finished: boolean;
}

const MemberDepartmentProgressPage = () => {
  const result = new URLSearchParams(useLocation().search);
  const [loading, setLoading] = useState<boolean>(false);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(10);
  const [list, setList] = useState<any>([]);
  const [total, setTotal] = useState(0);
  const [refresh, setRefresh] = useState(false);
  const [courses, setCourses] = useState<any>([]);
  const [records, setRecords] = useState<any>({});
  const [totalHour, setTotalHour] = useState(0);
  const [name, setName] = useState<string>("");
  const [email, setEmail] = useState<string>("");
  const [id_card, setIdCard] = useState<string>("");
  const [showMode, setShowMode] = useState<string>("all");
  const [did, setDid] = useState(Number(result.get("id")));
  const [title, setTitle] = useState(String(result.get("title")));
  const [exportLoading, setExportLoading] = useState(false);
  const modes = [
    { label: "全部", value: "all" },
    { label: "不显示公开课", value: "only_dep" },
  ];

  useEffect(() => {
    setDid(Number(result.get("id")));
    setTitle(String(result.get("title")));
    resetData();
  }, [result.get("id"), result.get("title")]);

  useEffect(() => {
    getData();
  }, [refresh, page, size]);

  const getData = () => {
    if (loading) {
      return;
    }
    setLoading(true);
    member
      .departmentProgress(did, page, size, {
        sort_field: "",
        sort_algo: "",
        name: name,
        email: email,
        id_card: id_card,
        show_mode: showMode,
      })
      .then((res: any) => {
        setList(res.data.data);
        setTotal(res.data.total);
        let data = res.data.courses;
        let arr = [];
        let value = 0;
        for (let key in data) {
          arr.push(data[key]);
          value += data[key].class_hour;
        }
        setCourses(arr);
        setTotalHour(value);
        setRecords(res.data.user_course_records);
        setLoading(false);
      });
  };

  const resetData = () => {
    setName("");
    setEmail("");
    setIdCard("");
    setShowMode("all");
    setPage(1);
    setSize(10);
    setList([]);
    setRefresh(!refresh);
  };

  const paginationProps = {
    current: page, //当前页码
    pageSize: size,
    total: total, // 总条数
    onChange: (page: number, pageSize: number) =>
      handlePageChange(page, pageSize), //改变页码的函数
    showSizeChanger: true,
  };

  const handlePageChange = (page: number, pageSize: number) => {
    setPage(page);
    setSize(pageSize);
  };

  // const getTotalHours = (params: any) => {
  //   if (params) {
  //     let value = 0;
  //     for (let key in params) {
  //       value += params[key].hour_count;
  //     }
  //     return value;
  //   } else {
  //     return 0;
  //   }
  // };

  const getFinishedHours = (params: any) => {
    if (params) {
      let value = 0;
      for (let key in params) {
        value += params[key].finished_count;
      }
      return value;
    } else {
      return 0;
    }
  };

  const exportExcel = () => {
    if (exportLoading) {
      return;
    }
    setExportLoading(true);
    let filter = {
      sort_field: "",
      sort_algo: "",
      name: name,
      email: email,
      id_card: id_card,
      show_mode: showMode,
    };
    member.departmentProgress(did, page, total, filter).then((res: any) => {
      if (res.data.total === 0) {
        message.error("数据为空");
        setExportLoading(false);
        return;
      }
      let filename = title + "学习进度.xlsx";
      let sheetName = "sheet1";
      let data = [];
      let arr = ["学员"];
      let data2 = res.data.courses;
      let arr2: any = [];
      let value = 0;
      for (let key in data2) {
        arr2.push(data2[key]);
        value += data2[key].class_hour;
      }
      let w_totalHour = value;
      let w_courses = arr2;
      let w_records = res.data.user_course_records;
      w_courses.map((item: any) => {
        arr.push(item.title);
      });
      arr.push("总计课时");
      data.push(arr);

      res.data.data.forEach((item: any) => {
        let arr = [item.name];
        w_courses.map((it: any) => {
          if (w_records && w_records[item.id] && w_records[item.id][it.id]) {
            if (w_records && w_records[item.id][it.id].is_finished === 1) {
              arr.push("已学完");
            } else {
              arr.push(
                w_records &&
                  w_records[item.id][it.id].finished_count +
                    " / " +
                    it.class_hour
              );
            }
          } else {
            arr.push(0 + " / " + it.class_hour);
          }
        });
        arr.push(getFinishedHours(w_records[item.id]) + " / " + w_totalHour);
        data.push(arr);
      });

      const jsonWorkSheet = XLSX.utils.json_to_sheet(data);
      const workBook: XLSX.WorkBook = {
        SheetNames: [sheetName],
        Sheets: {
          [sheetName]: jsonWorkSheet,
        },
      };
      XLSX.writeFile(workBook, filename);
      setExportLoading(false);
    });
  };

  return (
    <div className="playedu-main-body">
      <div className="float-left mb-24">
        <BackBartment title={title + "学习进度"} />
      </div>
      <div className="float-left j-b-flex mb-24">
        <div className="d-flex">
          <Button type="default" onClick={() => exportExcel()}>
            批量导出表格
          </Button>
          <div className="helper-text ml-24">
            （以下表格内数字对应的是表头课程的“已学完课时数/总课时数”）
          </div>
        </div>
        <div className="d-flex">
          <div className="d-flex mr-24 ">
            <Typography.Text>姓名：</Typography.Text>
            <Input
              value={name}
              onChange={(e) => {
                setName(e.target.value);
              }}
              allowClear
              style={{ width: 160 }}
              placeholder="请输入姓名关键字"
            />
          </div>
          {/* <div className="d-flex mr-24">
            <Typography.Text>邮箱：</Typography.Text>
            <Input
              value={email}
              onChange={(e) => {
                setEmail(e.target.value);
              }}
              allowClear
              style={{ width: 160 }}
              placeholder="请输入邮箱"
            />
          </div>
          <div className="d-flex mr-24">
            <Typography.Text>模式：</Typography.Text>
            <Select
              style={{ width: 160 }}
              allowClear
              placeholder="请选择"
              value={showMode}
              onChange={(value: string) => setShowMode(value)}
              options={modes}
            />
          </div> */}
          <div className="d-flex">
            <Button className="mr-16" onClick={resetData}>
              重 置
            </Button>
            <Button
              type="primary"
              onClick={() => {
                setPage(1);
                setRefresh(!refresh);
              }}
            >
              查 询
            </Button>
          </div>
        </div>
      </div>
      <div className="float-left">
        <Table
          bordered
          dataSource={list}
          loading={loading}
          pagination={paginationProps}
          rowKey={(record) => record.id}
          scroll={{ x: 1200 }}
        >
          <Column
            fixed="left"
            title="学员"
            dataIndex="name"
            key="name"
            width={150}
            render={(_, record: any) => (
              <>
                <Image
                  style={{ borderRadius: "50%" }}
                  src={record.avatar}
                  preview={false}
                  width={40}
                  height={40}
                />
                <span className="ml-8">{record.name}</span>
              </>
            )}
          />
          {courses.map((item: any) => (
            <Column
              title={item.title}
              ellipsis={true}
              dataIndex="id"
              key={item.id}
              width={168}
              render={(_, record: any) => (
                <>
                  {records[record.id] && records[record.id][item.id] ? (
                    records[record.id][item.id].is_finished === 1 ? (
                      <span>已学完</span>
                    ) : (
                      <>
                        <span>
                          {records[record.id][item.id].finished_count}
                        </span>{" "}
                        / <span>{item.class_hour}</span>
                      </>
                    )
                  ) : (
                    <>
                      <span>0</span> / <span>{item.class_hour}</span>
                    </>
                  )}
                </>
              )}
            />
          ))}
          <Column
            fixed="right"
            title="总计课时"
            dataIndex="id"
            key="id"
            width={150}
            render={(_, record: any) => (
              <>
                <span>{getFinishedHours(records[record.id])}</span> /{" "}
                <span>{totalHour}</span>
              </>
            )}
          />
        </Table>
      </div>
    </div>
  );
};
export default MemberDepartmentProgressPage;
