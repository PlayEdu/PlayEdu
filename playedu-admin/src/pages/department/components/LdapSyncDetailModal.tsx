import React, { useState, useEffect } from "react";
import { Modal, Card, Row, Col, Statistic, Divider } from "antd";
import { ldap } from "../../../api";
import { LdapSyncItemsModal } from ".";
import { dateFormat } from "../../../utils/index";

interface LdapSyncDetailModalProps {
  record: any;
  open: boolean;
  onCancel: () => void;
}

export const LdapSyncDetailModal: React.FC<LdapSyncDetailModalProps> = ({ 
  record, 
  open, 
  onCancel 
}) => {
  const [loading, setLoading] = useState(false);
  const [detail, setDetail] = useState<any>(null);
  const [itemsVisible, setItemsVisible] = useState(false);
  const [itemsType, setItemsType] = useState<"department" | "user">("department");
  const [itemsAction, setItemsAction] = useState<number>(0);

  useEffect(() => {
    if (open && record) {
      loadDetail();
    }
  }, [open, record]);

  const loadDetail = () => {
    setLoading(true);
    ldap.getSyncRecordDetail(record.id).then((res: any) => {
      setDetail(res.data);
      setLoading(false);
    }).catch(() => {
      setLoading(false);
    });
  };

  const showItems = (type: "department" | "user", action: number) => {
    setItemsType(type);
    setItemsAction(action);
    setItemsVisible(true);
  };

  return (
    <>
      <Modal
        title="同步详情"
        open={open}
        onCancel={onCancel}
        width={888}
        footer={null}
      >
        {detail && (
          <>
            <Card title="基本信息" loading={loading}>
              <Row gutter={16}>
                <Col span={8}>
                  <Statistic title="同步ID" value={detail.id} />
                </Col>
                <Col span={8}>
                  <Statistic 
                    title="同步状态" 
                    value={
                      detail.status === 0 ? "进行中" : 
                      detail.status === 1 ? "成功" : "失败"
                    } 
                  />
                </Col>
                <Col span={8}>
                  <Statistic title="同步时间" value={dateFormat(detail.created_at)} />
                </Col>
              </Row>
            </Card>

            <Divider />

            <Card title="部门同步统计" loading={loading}>
              <Row gutter={16}>
                <Col span={6}>
                  <div 
                    onClick={() => showItems("department", 0)}
                    className="clickable-stat"
                  >
                    <Statistic 
                      title="总部门数" 
                      value={detail.total_department_count} 
                    />
                  </div>
                </Col>
                <Col span={6}>
                  <div 
                    onClick={() => showItems("department", 1)}
                    className="clickable-stat"
                  >
                    <Statistic 
                      title="新增部门" 
                      value={detail.created_department_count}
                    />
                  </div>
                </Col>
                <Col span={6}>
                  <div 
                    onClick={() => showItems("department", 2)}
                    className="clickable-stat"
                  >
                    <Statistic 
                      title="更新部门" 
                      value={detail.updated_department_count}
                    />
                  </div>
                </Col>
                <Col span={6}>
                  <div 
                    onClick={() => showItems("department", 3)}
                    className="clickable-stat"
                  >
                    <Statistic 
                      title="删除部门" 
                      value={detail.deleted_department_count}
                    />
                  </div>
                </Col>
              </Row>
            </Card>

            <Divider />

            <Card title="用户同步统计" loading={loading}>
              <Row gutter={16}>
                <Col span={6}>
                  <div 
                    onClick={() => showItems("user", 0)}
                    className="clickable-stat"
                  >
                    <Statistic 
                      title="总用户数" 
                      value={detail.total_user_count}
                    />
                  </div>
                </Col>
                <Col span={4}>
                  <div 
                    onClick={() => showItems("user", 1)}
                    className="clickable-stat"
                  >
                    <Statistic 
                      title="新增用户" 
                      value={detail.created_user_count}
                    />
                  </div>
                </Col>
                <Col span={4}>
                  <div 
                    onClick={() => showItems("user", 2)}
                    className="clickable-stat"
                  >
                    <Statistic 
                      title="更新用户" 
                      value={detail.updated_user_count}
                    />
                  </div>
                </Col>
                <Col span={4}>
                  <div 
                    onClick={() => showItems("user", 3)}
                    className="clickable-stat"
                  >
                    <Statistic 
                      title="删除用户" 
                      value={detail.deleted_user_count}
                    />
                  </div>
                </Col>
                <Col span={6}>
                  <div 
                    onClick={() => showItems("user", 5)}
                    className="clickable-stat"
                  >
                    <Statistic 
                      title="禁止用户" 
                      value={detail.banned_user_count}
                    />
                  </div>
                </Col>
              </Row>
            </Card>

            {detail.error_message && (
              <>
                <Divider />
                <Card title="错误信息" loading={loading}>
                  <pre>{detail.error_message}</pre>
                </Card>
              </>
            )}
          </>
        )}
      </Modal>

      {detail && (
        <LdapSyncItemsModal
          recordId={detail.id}
          type={itemsType}
          action={itemsAction}
          open={itemsVisible}
          onCancel={() => setItemsVisible(false)}
        />
      )}
    </>
  );
}; 