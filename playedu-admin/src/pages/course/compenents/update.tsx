import React, { useState, useEffect } from "react";
import {
  Space,
  Radio,
  Button,
  Drawer,
  Form,
  TreeSelect,
  DatePicker,
  Input,
  message,
  Image,
  Spin,
  Tag,
} from "antd";
import styles from "./update.module.less";
import { course, department } from "../../../api/index";
import { UploadImageButton, SelectRange } from "../../../compenents";
import defaultThumb1 from "../../../assets/thumb/thumb1.png";
import defaultThumb2 from "../../../assets/thumb/thumb2.png";
import defaultThumb3 from "../../../assets/thumb/thumb3.png";
import dayjs from "dayjs";
import moment from "moment";

interface PropInterface {
  id: number;
  open: boolean;
  onCancel: () => void;
}

interface Option {
  value: string | number;
  title: string;
  children?: Option[];
}

export const CourseUpdate: React.FC<PropInterface> = ({
  id,
  open,
  onCancel,
}) => {
  const [form] = Form.useForm();
  const [init, setInit] = useState(true);
  const [loading, setLoading] = useState(false);
  const [departments, setDepartments] = useState<Option[]>([]);
  const [categories, setCategories] = useState<Option[]>([]);
  const [resourceUrl, setResourceUrl] = useState<ResourceUrlModel>({});
  const [thumb, setThumb] = useState("");
  const [depIds, setDepIds] = useState<number[]>([]);
  const [deps, setDeps] = useState<any[]>([]);
  const [idsVisible, setIdsVisible] = useState(false);
  const [type, setType] = useState("open");

  useEffect(() => {
    setInit(true);
    if (id === 0) {
      return;
    }
    if (open) {
      getParams();
      getCategory();
      getDetail();
    }
  }, [form, id, open]);

  const getCategory = () => {
    course.createCourse().then((res: any) => {
      const categories = res.data.categories;
      if (JSON.stringify(categories) !== "{}") {
        const new_arr: any = checkArr(categories, 0, null);
        setCategories(new_arr);
      }
    });
  };
  const getParams = () => {
    department.departmentList({}).then((res: any) => {
      const departments = res.data.departments;
      const departCount = res.data.dep_user_count;
      if (JSON.stringify(departments) !== "{}") {
        const new_arr: any = checkArr(departments, 0, departCount);
        setDepartments(new_arr);
      }
    });
  };

  const getDetail = () => {
    course.course(id).then((res: any) => {
      let type = res.data.dep_ids.length > 0 ? "elective" : "open";
      let chapterType = res.data.chapters.length > 0 ? 1 : 0;
      form.setFieldsValue({
        title: res.data.course.title,
        thumb: res.data.course.thumb,
        category_ids: res.data.category_ids,
        isRequired: res.data.course.is_required,
        short_desc: res.data.course.short_desc,
        hasChapter: chapterType,
        type: type,
        published_at: res.data.course.sort_at
          ? dayjs(res.data.course.sort_at)
          : "",
      });
      setType(type);
      setThumb(res.data.course.thumb);
      let deps = res.data.deps;
      if (deps && JSON.stringify(deps) !== "{}") {
        getDepsDetail(deps);
      }
      if (deps && JSON.stringify(deps) !== "{}") {
        form.setFieldsValue({ ids: [1, 2] });
      }
      setResourceUrl(res.data.resource_url);
      setThumb(
        res.data.course.thumb === -1
          ? defaultThumb1
          : res.data.course.thumb === -2
          ? defaultThumb2
          : res.data.course.thumb === -3
          ? defaultThumb3
          : res.data.resource_url[res.data.course.thumb]
      );
      setInit(false);
    });
  };

  const getDepsDetail = (deps: any) => {
    let arr: any = [];
    let arr2: any = [];
    Object.keys(deps).map((v, i) => {
      arr.push(Number(v));
      arr2.push({
        key: Number(v),
        title: {
          props: {
            children: deps[v],
          },
        },
      });
    });
    setDepIds(arr);
    setDeps(arr2);
  };

  const getNewTitle = (title: any, id: number, counts: any) => {
    if (counts) {
      let value = counts[id] || 0;
      return title + "(" + value + ")";
    } else {
      return title;
    }
  };

  const checkArr = (departments: any[], id: number, counts: any) => {
    const arr = [];
    for (let i = 0; i < departments[id].length; i++) {
      if (!departments[departments[id][i].id]) {
        arr.push({
          title: getNewTitle(
            departments[id][i].name,
            departments[id][i].id,
            counts
          ),
          value: departments[id][i].id,
        });
      } else {
        const new_arr: any = checkArr(
          departments,
          departments[id][i].id,
          counts
        );
        arr.push({
          title: getNewTitle(
            departments[id][i].name,
            departments[id][i].id,
            counts
          ),
          value: departments[id][i].id,
          children: new_arr,
        });
      }
    }
    return arr;
  };

  const onFinish = (values: any) => {
    if (loading) {
      return;
    }
    let dep_ids: any[] = [];
    if (type === "elective") {
      dep_ids = depIds;
    }
    setLoading(true);
    course
      .updateCourse(
        id,
        values.title,
        values.thumb,
        values.short_desc,
        1,
        values.isRequired,
        dep_ids,
        values.category_ids,
        [],
        [],
        values.published_at
      )
      .then((res: any) => {
        setLoading(false);
        message.success("保存成功！");
        onCancel();
      })
      .catch((e) => {
        setLoading(false);
      });
  };

  const onFinishFailed = (errorInfo: any) => {
    console.log("Failed:", errorInfo);
  };

  const getType = (e: any) => {
    setType(e.target.value);
  };

  const disabledDate = (current: any) => {
    return current && current >= moment().add(0, "days"); // 选择时间要大于等于当前天。若今天不能被选择，去掉等号即可。
  };

  return (
    <>
      {open ? (
        <Drawer
          title="编辑课程"
          onClose={onCancel}
          maskClosable={false}
          open={true}
          footer={
            <Space className="j-r-flex">
              <Button onClick={() => onCancel()}>取 消</Button>
              <Button
                loading={loading}
                onClick={() => form.submit()}
                type="primary"
              >
                确 认
              </Button>
            </Space>
          }
          width={634}
        >
          {init && (
            <div className="float-left text-center mt-30">
              <Spin></Spin>
            </div>
          )}
          <div
            className="float-left mt-24"
            style={{ display: init ? "none" : "block" }}
          >
            <SelectRange
              defaultDepIds={depIds}
              defaultDeps={deps}
              open={idsVisible}
              onCancel={() => setIdsVisible(false)}
              onSelected={(selDepIds, selDeps) => {
                setDepIds(selDepIds);
                setDeps(selDeps);
                form.setFieldsValue({
                  ids: selDepIds,
                });
                setIdsVisible(false);
              }}
            />
            <Form
              form={form}
              name="update-basic"
              labelCol={{ span: 5 }}
              wrapperCol={{ span: 19 }}
              initialValues={{ remember: true }}
              onFinish={onFinish}
              onFinishFailed={onFinishFailed}
              autoComplete="off"
            >
              <Form.Item
                label="课程分类"
                name="category_ids"
                rules={[{ required: true, message: "请选择课程分类!" }]}
              >
                <TreeSelect
                  showCheckedStrategy={TreeSelect.SHOW_ALL}
                  allowClear
                  multiple
                  style={{ width: 424 }}
                  treeData={categories}
                  placeholder="请选择课程分类"
                  treeDefaultExpandAll
                />
              </Form.Item>
              <Form.Item
                label="课程名称"
                name="title"
                rules={[{ required: true, message: "请在此处输入课程名称!" }]}
              >
                <Input
                  allowClear
                  style={{ width: 424 }}
                  placeholder="请在此处输入课程名称"
                />
              </Form.Item>
              <Form.Item
                label="课程属性"
                name="isRequired"
                rules={[{ required: true, message: "请选择课程属性!" }]}
              >
                <Radio.Group>
                  <Radio value={1}>必修课</Radio>
                  <Radio value={0} style={{ marginLeft: 22 }}>
                    选修课
                  </Radio>
                </Radio.Group>
              </Form.Item>
              <Form.Item
                label="指派部门"
                name="type"
                rules={[{ required: true, message: "请选择指派部门!" }]}
              >
                <Radio.Group onChange={getType}>
                  <Radio value="open">全部部门</Radio>
                  <Radio value="elective">选择部门</Radio>
                </Radio.Group>
              </Form.Item>
              {type === "elective" && (
                <>
                  <Form.Item
                    label="选择部门"
                    name="ids"
                    rules={[{ required: true, message: "请选择部门!" }]}
                  >
                    <div
                      className="d-flex"
                      style={{
                        width: "100%",
                        flexWrap: "wrap",
                        marginBottom: -8,
                      }}
                    >
                      <Button
                        type="default"
                        style={{ marginBottom: 14 }}
                        onClick={() => setIdsVisible(true)}
                      >
                        添加范围
                      </Button>
                      <div
                        className="d-flex"
                        style={{
                          width: "100%",
                          flexWrap: "wrap",
                          marginBottom: -16,
                        }}
                      >
                        {deps.length > 0 &&
                          deps.map((item: any, i: number) => (
                            <Tag
                              key={i}
                              closable
                              style={{
                                height: 32,
                                lineHeight: "32px",
                                fontSize: 14,
                                color: "#FF4D4F",
                                background: "rgba(255,77,79,0.1)",
                                marginRight: 16,
                                marginBottom: 16,
                              }}
                              onClose={(e) => {
                                e.preventDefault();
                                let arr = [...deps];
                                let arr2 = [...depIds];
                                arr.splice(i, 1);
                                arr2.splice(i, 1);
                                setDeps(arr);
                                setDepIds(arr2);
                                form.setFieldsValue({
                                  ids: arr2,
                                });
                              }}
                            >
                              {item.title.props.children}
                            </Tag>
                          ))}
                      </div>
                    </div>
                  </Form.Item>
                </>
              )}

              <Form.Item
                label="课程封面"
                name="thumb"
                rules={[{ required: true, message: "请上传课程封面!" }]}
              >
                <div className="d-flex">
                  <Image
                    src={thumb}
                    width={160}
                    height={120}
                    style={{ borderRadius: 6 }}
                    preview={false}
                  />
                  <div className="c-flex ml-8 flex-1">
                    <div className="d-flex mb-28">
                      <div
                        className={
                          thumb === defaultThumb1
                            ? styles["thumb-item-avtive"]
                            : styles["thumb-item"]
                        }
                        onClick={() => {
                          setThumb(defaultThumb1);
                          form.setFieldsValue({
                            thumb: -1,
                          });
                        }}
                      >
                        <Image
                          src={defaultThumb1}
                          width={80}
                          height={60}
                          style={{ borderRadius: 6 }}
                          preview={false}
                        />
                      </div>
                      <div
                        className={
                          thumb === defaultThumb2
                            ? styles["thumb-item-avtive"]
                            : styles["thumb-item"]
                        }
                        onClick={() => {
                          setThumb(defaultThumb2);
                          form.setFieldsValue({
                            thumb: -2,
                          });
                        }}
                      >
                        <Image
                          src={defaultThumb2}
                          width={80}
                          height={60}
                          style={{ borderRadius: 6 }}
                          preview={false}
                        />
                      </div>
                      <div
                        className={
                          thumb === defaultThumb3
                            ? styles["thumb-item-avtive"]
                            : styles["thumb-item"]
                        }
                        onClick={() => {
                          setThumb(defaultThumb3);
                          form.setFieldsValue({
                            thumb: -3,
                          });
                        }}
                      >
                        <Image
                          src={defaultThumb3}
                          width={80}
                          height={60}
                          style={{ borderRadius: 6 }}
                          preview={false}
                        />
                      </div>
                    </div>
                    <div className="d-flex">
                      <UploadImageButton
                        text="更换封面"
                        onSelected={(url, id) => {
                          setThumb(url);
                          form.setFieldsValue({ thumb: id });
                        }}
                      ></UploadImageButton>
                      <span className="helper-text ml-8">
                        （推荐尺寸:400x300px）
                      </span>
                    </div>
                  </div>
                </div>
              </Form.Item>
              <Form.Item label="课程简介" name="short_desc">
                <Input.TextArea
                  style={{ width: 424, minHeight: 80 }}
                  allowClear
                  placeholder="请输入课程简介（最多200字）"
                  maxLength={200}
                />
              </Form.Item>
              <Form.Item label="上架时间">
                <Space align="baseline" style={{ height: 32 }}>
                  <Form.Item name="published_at">
                    <DatePicker
                      disabledDate={disabledDate}
                      format="YYYY-MM-DD HH:mm:ss"
                      style={{ width: 240 }}
                      showTime
                      placeholder="请选择上架时间"
                    />
                  </Form.Item>
                  <div className="helper-text">
                    （上架时间越晚，排序越靠前）
                  </div>
                </Space>
              </Form.Item>
            </Form>
          </div>
        </Drawer>
      ) : null}
    </>
  );
};
