import styles from "./index.module.less";
import { Button, Tooltip } from "antd";
import fangIcon from "../../assets/images/commen/fanghu.png";
import ex1Icon from "../../assets/images/commen/icon5.png";
import ex2Icon from "../../assets/images/commen/icon6.png";

const LicensingPage = () => {
  return (
    <>
      <div className="playedu-main-top">
        <div className={styles["main-title"]}>当前版本信息</div>
        <div className="float-left mt-24">
          <div className={styles["persion"]}>PlayEdu开源版 v2.0</div>
        </div>
        <div className="float-left mt-16">
          <div className={styles["content"]}>
            1.版权归属：PlayEdu开源版版权归杭州白书科技有限公司所有，保留全部使用权。
          </div>
          <div className={styles["content"]}>
            2.代码修改：允许在遵守开源协议的前提下修改代码，但需在修改处添加明确备注，详细说明修改内容。
          </div>
          <div className={styles["content"]}>
            3.版权保护：任何场景下均需保留
            PlayEdu开源版页面及代码中的原有版权信息（如 “Designed By PlayEdu”
            标识、官网链接、开源说明等），严禁删除、修改或篡改，违者需承担法律责任及赔偿。
          </div>
        </div>
        <div className="float-left mt-24">
          <Button
            type="primary"
            onClick={() => {
              window.open("https://www.playeduos.com/");
            }}
          >
            采购企业版本
          </Button>
        </div>
      </div>
      <div className="playedu-main-top mt-24" style={{ position: "relative" }}>
        <div className={styles["main-title"]}>版本功能对比</div>
        <div className="float-left mt-24">
          <div className={styles["contrast-box1"]}>
            <div className={styles["name"]}>功能特性</div>
            <div className={styles["ex"]}>开源版</div>
            <div className={styles["ex2"]}>企业版</div>
          </div>
        </div>
        <div className="float-left mt-30">
          <div className={styles["contrast-box2"]}>
            <div className={styles["name"]}>系统支持</div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>功能支持</span>
            </div>
            <div className={styles["ex"]}>基础功能</div>
            <div className={styles["ex2"]}>全部功能</div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>技术支持</span>
            </div>
            <div className={styles["ex"]}>无</div>
            <Tooltip
              className={styles["ex2"]}
              title="专属售后群以及远程排障服务"
            >
              <img src={fangIcon} className={styles["icon"]} />
              7*10h专业技术服务
            </Tooltip>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>安全认证</span>
            </div>
            <div className={styles["ex"]}>无</div>
            <div className={styles["ex2"]}>CMA国家资质安全认证</div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>部署服务</span>
            </div>
            <div className={styles["ex"]}>无</div>
            <div className={styles["ex2"]}>内外网单机及集群部署</div>
          </div>
          <div
            className={styles["contrast-box3"]}
            style={{ borderRadius: "0px 0px 16px 16px" }}
          >
            <div className={styles["name"]}>
              <strong></strong>
              <span>性能负载</span>
            </div>
            <div className={styles["ex"]}>低</div>
            <div className={styles["ex2"]}>企业级全链路高性能场景</div>
          </div>
        </div>
        <div className="float-left mt-30">
          <div className={styles["contrast-box2"]}>
            <div className={styles["name"]}>资源类型</div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>视频库</span>
            </div>
            <div className={styles["ex"]}>MP4(H264)</div>
            <div className={styles["ex2"]}>MP4|MOV|AVI|WMV|FLV</div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>图片库</span>
            </div>
            <div className={styles["ex"]}>
              <img src={ex1Icon} className={styles["pic"]} />
            </div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>附件库</span>
              <span className={styles["sp"]}>（仅支持上传下载）</span>
            </div>
            <div className={styles["ex"]}>
              <img src={ex1Icon} className={styles["pic"]} />
            </div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>文档库</span>
              <span className={styles["sp"]}>
                （支持Word、PPT、PDF在线预览）
              </span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>音频库</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>证书库</span>
              <span className={styles["sp"]}>（支持自定义证书设计）</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div
            className={styles["contrast-box3"]}
            style={{ borderRadius: "0px 0px 16px 16px" }}
          >
            <div className={styles["name"]}>
              <strong></strong>
              <span>讲师资料</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
        </div>
        <div className="float-left mt-30">
          <div className={styles["contrast-box2"]}>
            <div className={styles["name"]}>考试中心</div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>试题库</span>
              <span className={styles["sp"]}>
                （支持六大题型，支持批量导入）
              </span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div
            className={styles["contrast-box3"]}
            style={{ borderRadius: "0px 0px 16px 16px" }}
          >
            <div className={styles["name"]}>
              <strong></strong>
              <span>试卷库</span>
              <span className={styles["sp"]}>（支持手动组卷，随机组卷）</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
        </div>
        <div className="float-left mt-30">
          <div className={styles["contrast-box2"]}>
            <div className={styles["name"]}>考试中心</div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>线上课</span>
            </div>
            <div className={styles["ex"]}>
              <img src={ex1Icon} className={styles["pic"]} />
            </div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>线下课</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>学习任务</span>
              <span className={styles["sp"]}>
                （支持指派部门以及独立学员、支持多阶段学习、支持闯关模式、支持关联证书奖励）
              </span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>考试任务</span>
              <span className={styles["sp"]}>
                （支持指派部门以及独立学员、支持试题/选项乱序、支持补考、支持关联证书奖励）
              </span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div
            className={styles["contrast-box3"]}
            style={{ borderRadius: "0px 0px 16px 16px" }}
          >
            <div className={styles["name"]}>
              <strong></strong>
              <span>指派方式</span>
            </div>
            <div className={styles["ex"]}>部门</div>
            <div className={styles["ex2"]}>部门|学员|用户组</div>
          </div>
        </div>
        <div className="float-left mt-30">
          <div className={styles["contrast-box2"]}>
            <div className={styles["name"]}>积分激励</div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>积分规则</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>积分调整</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div
            className={styles["contrast-box3"]}
            style={{ borderRadius: "0px 0px 16px 16px" }}
          >
            <div className={styles["name"]}>
              <strong></strong>
              <span>积分排行</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
        </div>
        <div className="float-left mt-30">
          <div className={styles["contrast-box2"]}>
            <div className={styles["name"]}>数据统计</div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>数据概览</span>
            </div>
            <div className={styles["ex"]}>
              <img src={ex1Icon} className={styles["pic"]} />
            </div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>学习排行</span>
            </div>
            <div className={styles["ex"]}>
              <img src={ex1Icon} className={styles["pic"]} />
            </div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>资源统计</span>
            </div>
            <div className={styles["ex"]}>
              <img src={ex1Icon} className={styles["pic"]} />
            </div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>学员信息导出</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>课程学习导出</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>学员学习导出</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>部门学习导出</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>学习任务统计</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>考试任务统计</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div
            className={styles["contrast-box3"]}
            style={{ borderRadius: "0px 0px 16px 16px" }}
          >
            <div className={styles["name"]}>
              <strong></strong>
              <span>考生答卷导出</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
        </div>
        <div className="float-left mt-30">
          <div className={styles["contrast-box2"]}>
            <div className={styles["name"]}>防作弊</div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>防拖拽</span>
            </div>
            <div className={styles["ex"]}>
              <img src={ex1Icon} className={styles["pic"]} />
            </div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>防挂机</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div
            className={styles["contrast-box3"]}
            style={{ borderRadius: "0px 0px 16px 16px" }}
          >
            <div className={styles["name"]}>
              <strong></strong>
              <span>防切屏</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
        </div>
        <div className="float-left mt-30">
          <div className={styles["contrast-box2"]}>
            <div className={styles["name"]}>系统配置</div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>网站设置</span>
            </div>
            <div className={styles["ex"]}>
              <img src={ex1Icon} className={styles["pic"]} />
            </div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>播放设置</span>
            </div>
            <div className={styles["ex"]}>
              <img src={ex1Icon} className={styles["pic"]} />
            </div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div
            className={styles["contrast-box3"]}
            style={{ borderRadius: "0px 0px 16px 16px" }}
          >
            <div className={styles["name"]}>
              <strong></strong>
              <span>学员设置</span>
            </div>
            <div className={styles["ex"]}>
              <img src={ex1Icon} className={styles["pic"]} />
            </div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
        </div>
        <div className="float-left mt-30">
          <div className={styles["contrast-box2"]}>
            <div className={styles["name"]}>单点登录</div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>LDAP</span>
            </div>
            <div className={styles["ex"]}>
              <img src={ex1Icon} className={styles["pic"]} />
            </div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>企业微信</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>钉钉</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>飞书</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>云之家（金蝶）</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>CAS</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>泛微</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>免费支持</div>
          </div>
          <div
            className={styles["contrast-box3"]}
            style={{ borderRadius: "0px 0px 16px 16px" }}
          >
            <div className={styles["name"]}>
              <strong></strong>
              <span>用友</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>免费支持</div>
          </div>
        </div>
        <div className="float-left mt-30">
          <div className={styles["contrast-box2"]}>
            <div className={styles["name"]}>存储方案</div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>MinIO(私有化)</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>阿里云oss</span>
            </div>
            <div className={styles["ex"]}>
              <img src={ex1Icon} className={styles["pic"]} />
            </div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div
            className={styles["contrast-box3"]}
            style={{ borderRadius: "0px 0px 16px 16px" }}
          >
            <div className={styles["name"]}>
              <strong></strong>
              <span>腾讯云cos</span>
            </div>
            <div className={styles["ex"]}>
              <img src={ex1Icon} className={styles["pic"]} />
            </div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
        </div>
        <div className="float-left mt-30">
          <div className={styles["contrast-box2"]}>
            <div className={styles["name"]}>安全保护</div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>视频跑马灯</span>
            </div>
            <div className={styles["ex"]}>
              <img src={ex1Icon} className={styles["pic"]} />
            </div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>HLS视频加密</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>视频防嗅探下载</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>文档水印</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>考试水印</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div
            className={styles["contrast-box3"]}
            style={{ borderRadius: "0px 0px 16px 16px" }}
          >
            <div className={styles["name"]}>
              <strong></strong>
              <span>试题防复制</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
        </div>
        <div className="float-left mt-30">
          <div className={styles["contrast-box2"]}>
            <div className={styles["name"]}>多终端</div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>PC独立网站</span>
            </div>
            <div className={styles["ex"]}>
              <img src={ex1Icon} className={styles["pic"]} />
            </div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div
            className={styles["contrast-box3"]}
            style={{ borderRadius: "0px 0px 16px 16px" }}
          >
            <div className={styles["name"]}>
              <strong></strong>
              <span>移动端H5</span>
            </div>
            <div className={styles["ex"]}>
              <img src={ex1Icon} className={styles["pic"]} />
            </div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
        </div>
        <div className="float-left mt-30">
          <div className={styles["contrast-box2"]}>
            <div className={styles["name"]}>版权信息</div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>代码协议文件</span>
            </div>
            <div className={styles["ex"]}>不可移除</div>
            <div className={styles["ex2"]}>企业授权</div>
          </div>
          <div
            className={styles["contrast-box3"]}
            style={{ borderRadius: "0px 0px 16px 16px" }}
          >
            <div className={styles["name"]}>
              <strong></strong>
              <span>页脚版权链接</span>
            </div>
            <div className={styles["ex"]}>不可移除</div>
            <div className={styles["ex2"]}>企业授权</div>
          </div>
        </div>
        <div className="float-left mt-30">
          <div className={styles["contrast-box2"]}>
            <div className={styles["name"]}>售后服务</div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>安装部署</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>使用手册</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div className={styles["contrast-box3"]}>
            <div className={styles["name"]}>
              <strong></strong>
              <span>系统升级</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
          <div
            className={styles["contrast-box3"]}
            style={{ borderRadius: "0px 0px 16px 16px" }}
          >
            <div className={styles["name"]}>
              <strong></strong>
              <span>专属服务群</span>
            </div>
            <div className={styles["ex"]}></div>
            <div className={styles["ex2"]}>
              <img src={ex2Icon} className={styles["pic"]} />
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default LicensingPage;
