import { useEffect, useState } from "react";
import { Row, Col, Empty, Table, Spin, Typography, Input, Button } from "antd";
import type { ColumnsType } from "antd/es/table";
import { resource } from "../../api";
import styles from "./index.module.less";
import { DurationText, TreeCategory } from "../../compenents";

interface VideoItem {
  id: number;
  name: string;
  created_at: string;
  type: string;
  url: string;
  path: string;
  size: number;
  extension: string;
  admin_id: number;
}

interface DataType {
  id: React.Key;
  name: string;
  created_at: string;
  type: string;
  url: string;
  path: string;
  size: number;
  extension: string;
  admin_id: number;
}

interface PropsInterface {
  defaultCheckedList: any[];
  label: string;
  open: boolean;
  onSelected: (arr: any[], videos: []) => void;
}

export const UploadVideoSub = (props: PropsInterface) => {
  const [init, setInit] = useState(true);
  const [category_ids, setCategoryIds] = useState<any>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [videoList, setVideoList] = useState<VideoItem[]>([]);
  const [videosExtra, setVideoExtra] = useState<any>([]);
  const [refresh, setRefresh] = useState(false);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(10);
  const [total, setTotal] = useState(0);
  const [selectedRowKeys, setSelectedRowKeys] = useState<any>([]);
  const [title, setTitle] = useState("");

  // 加载列表
  useEffect(() => {
    setInit(true);
    getvideoList();
  }, [props.open, category_ids, refresh, page, size]);

  useEffect(() => {
    if (props.defaultCheckedList.length > 0) {
      setSelectedRowKeys(props.defaultCheckedList);
    }
  }, [props.defaultCheckedList]);

  // 获取列表
  const getvideoList = () => {
    setLoading(true);
    let categoryIds = category_ids.join(",");
    resource
      .resourceList(page, size, "", "", title, "VIDEO", categoryIds)
      .then((res: any) => {
        setTotal(res.data.result.total);
        setVideoExtra(res.data.videos_extra);
        setVideoList(res.data.result.data);
        setLoading(false);
        setInit(false);
      })
      .catch((err) => {
        setLoading(false);
        setInit(false);
        console.log("错误,", err);
      });
  };

  // 重置列表
  const resetVideoList = () => {
    setPage(1);
    setVideoList([]);
    setTitle("");
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

  const columns: ColumnsType<DataType> = [
    {
      title: "视频",
      render: (_, record: any) => (
        <div className="d-flex">
          <i
            className="iconfont icon-icon-video"
            style={{
              fontSize: 14,
              color: "rgba(0,0,0,0.3)",
            }}
          />
          <div className="video-title ml-8">{record.name}</div>
        </div>
      ),
    },
    {
      title: "时长",
      render: (_, record: any) => (
        <div>
          <DurationText
            duration={videosExtra[record.id].duration}
          ></DurationText>
        </div>
      ),
    },
  ];

  const rowSelection = {
    selectedRowKeys: selectedRowKeys,
    onChange: (selectedRowKeys: React.Key[], selectedRows: DataType[]) => {
      let row: any = selectedRows;
      let arrVideos: any = [];
      if (row) {
        for (var i = 0; i < row.length; i++) {
          if (props.defaultCheckedList.indexOf(row[i].id) === -1) {
            arrVideos.push({
              name: row[i].name,
              type: row[i].type,
              rid: row[i].id,
              duration: videosExtra[row[i].id].duration,
            });
          }
        }
        props.onSelected(selectedRowKeys, arrVideos);
      }
      setSelectedRowKeys(selectedRowKeys);
    },
    getCheckboxProps: (record: any) => ({
      disabled: props.defaultCheckedList.indexOf(record.id) !== -1, //禁用的条件
    }),
  };

  return (
    <>
      <Row style={{ width: 752, minHeight: 520 }}>
        <Col span={7}>
          <div className="float-left">
            <TreeCategory
              selected={[]}
              type="no-cate"
              text={props.label}
              onUpdate={(keys: any) => setCategoryIds(keys)}
            />
          </div>
        </Col>
        <Col span={17}>
          <Row style={{ marginBottom: 24, paddingLeft: 10 }}>
            <div className="float-left  j-b-flex">
              <div className="d-flex"></div>
              <div className="d-flex">
                <div className="d-flex mr-24">
                  <Typography.Text>名称：</Typography.Text>
                  <Input
                    value={title}
                    onChange={(e) => {
                      setTitle(e.target.value);
                    }}
                    allowClear
                    style={{ width: 160 }}
                    placeholder="请输入名称关键字"
                  />
                </div>
                <div className="d-flex">
                  <Button className="mr-16" onClick={resetVideoList}>
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
          </Row>
          {init && (
            <div className="float-left text-center mt-30">
              <Spin></Spin>
            </div>
          )}
          <div
            className={styles["video-list"]}
            style={{ display: init ? "none" : "block" }}
          >
            {videoList.length === 0 && (
              <Col span={24} style={{ marginTop: 150 }}>
                <Empty description="暂无视频" />
              </Col>
            )}
            {videoList.length > 0 && (
              <div className="list-select-column-box c-flex">
                <Table
                  rowSelection={{
                    type: "checkbox",
                    ...rowSelection,
                  }}
                  columns={columns}
                  dataSource={videoList}
                  loading={loading}
                  pagination={paginationProps}
                  rowKey={(record) => record.id}
                />
              </div>
            )}
          </div>
        </Col>
      </Row>
    </>
  );
};
