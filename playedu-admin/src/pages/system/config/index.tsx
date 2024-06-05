import { useState, useEffect } from "react";
import {
  Row,
  Form,
  Input,
  Image,
  Button,
  Tabs,
  message,
  Switch,
  Checkbox,
  Slider,
  Space,
} from "antd";
import { appConfig, system } from "../../../api/index";
import { UploadImageButton } from "../../../compenents";
import { useSelector, useDispatch } from "react-redux";
import type { TabsProps } from "antd";
import type { CheckboxChangeEvent } from "antd/es/checkbox";
import {
  SystemConfigStoreInterface,
  saveConfigAction,
} from "../../../store/system/systemConfigSlice";
import logoIcon from "../../../assets/logo.png";

const SystemConfigPage = () => {
  const dispatch = useDispatch();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [logo, setLogo] = useState("");
  const [thumb, setThumb] = useState("");
  const [avatar, setAvatar] = useState("");
  const [tabKey, setTabKey] = useState(1);
  const [nameChecked, setNameChecked] = useState(false);
  const [emailChecked, setEmailChecked] = useState(false);
  const [idCardchecked, setIdCardChecked] = useState(false);
  const memberDefaultAvatar = useSelector(
    (state: any) => state.systemConfig.value.memberDefaultAvatar
  );

  useEffect(() => {
    getDetail();
  }, [tabKey]);

  const getDetail = () => {
    appConfig.appConfig().then((res: any) => {
      let configData = res.data;
      for (let i = 0; i < configData.length; i++) {
        if (configData[i].key_name === "system.name") {
          form.setFieldsValue({
            "system.name": configData[i].key_value,
          });
        } else if (configData[i].key_name === "system.logo") {
          form.setFieldsValue({
            "system.logo": configData[i].key_value,
          });
          if (configData[i].key_value !== "") {
            setLogo(configData[i].key_value);
          } else {
            setLogo(logoIcon);
          }
        } else if (configData[i].key_name === "system.api_url") {
          form.setFieldsValue({
            "system.api_url": configData[i].key_value,
          });
        } else if (configData[i].key_name === "system.api_url") {
          form.setFieldsValue({
            "system.api_url": configData[i].key_value,
          });
        } else if (configData[i].key_name === "system.pc_url") {
          form.setFieldsValue({
            "system.pc_url": configData[i].key_value,
          });
        } else if (configData[i].key_name === "system.h5_url") {
          form.setFieldsValue({
            "system.h5_url": configData[i].key_value,
          });
        } else if (configData[i].key_name === "player.poster") {
          setThumb(configData[i].key_value);
          form.setFieldsValue({
            "player.poster": configData[i].key_value,
          });
        } else if (configData[i].key_name === "player.disabled_drag") {
          let value = 0;
          if (configData[i].key_value === "1") {
            value = 1;
          }
          form.setFieldsValue({
            "player.disabled_drag": value,
          });
        } else if (
          configData[i].key_name === "player.is_enabled_bullet_secret"
        ) {
          let value = 0;
          if (configData[i].key_value === "1") {
            value = 1;
          }
          form.setFieldsValue({
            "player.is_enabled_bullet_secret": value,
          });
        } else if (configData[i].key_name === "player.bullet_secret_text") {
          if (configData[i].key_value.indexOf("{name}") != -1) {
            setNameChecked(true);
          }
          if (configData[i].key_value.indexOf("{email}") != -1) {
            setEmailChecked(true);
          }
          if (configData[i].key_value.indexOf("{idCard}") != -1) {
            setIdCardChecked(true);
          }
          form.setFieldsValue({
            "player.bullet_secret_text": configData[i].key_value,
          });
        } else if (configData[i].key_name === "player.bullet_secret_color") {
          form.setFieldsValue({
            "player.bullet_secret_color": configData[i].key_value,
          });
        } else if (configData[i].key_name === "player.bullet_secret_opacity") {
          let value = 0;
          if (configData[i].key_value !== "") {
            value = Number(configData[i].key_value) * 100;
          }
          form.setFieldsValue({
            "player.bullet_secret_opacity": value,
          });
        } else if (configData[i].key_name === "system.pc_index_footer_msg") {
          form.setFieldsValue({
            "system.pc_index_footer_msg": configData[i].key_value,
          });
        } else if (configData[i].key_name === "member.default_avatar") {
          if (configData[i].key_value !== "") {
            setAvatar(configData[i].key_value);
          } else {
            setAvatar(memberDefaultAvatar);
          }
          form.setFieldsValue({
            "member.default_avatar": configData[i].key_value,
          });
        } else if (configData[i].key_name === "minio.access_key") {
          form.setFieldsValue({
            "minio.access_key": configData[i].key_value,
          });
        } else if (configData[i].key_name === "minio.secret_key") {
          form.setFieldsValue({
            "minio.secret_key": configData[i].key_value,
          });
        } else if (configData[i].key_name === "minio.bucket") {
          form.setFieldsValue({
            "minio.bucket": configData[i].key_value,
          });
        } else if (configData[i].key_name === "minio.endpoint") {
          form.setFieldsValue({
            "minio.endpoint": configData[i].key_value,
          });
        } else if (configData[i].key_name === "minio.domain") {
          form.setFieldsValue({
            "minio.domain": configData[i].key_value,
          });
        } else if (configData[i].key_name === "ldap.enabled") {
          let value = 0;
          if (configData[i].key_value === "1") {
            value = 1;
          }
          form.setFieldsValue({
            "ldap.enabled": value,
          });
        } else if (configData[i].key_name === "ldap.url") {
          form.setFieldsValue({
            "ldap.url": configData[i].key_value,
          });
        } else if (configData[i].key_name === "ldap.admin_user") {
          form.setFieldsValue({
            "ldap.admin_user": configData[i].key_value,
          });
        } else if (configData[i].key_name === "ldap.admin_pass") {
          form.setFieldsValue({
            "ldap.admin_pass": configData[i].key_value,
          });
        } else if (configData[i].key_name === "ldap.base_dn") {
          form.setFieldsValue({
            "ldap.base_dn": configData[i].key_value,
          });
        }
      }
    });
  };

  const onSwitchChange = (checked: boolean) => {
    if (checked) {
      form.setFieldsValue({ "player.is_enabled_bullet_secret": 1 });
    } else {
      form.setFieldsValue({ "player.is_enabled_bullet_secret": 0 });
    }
  };

  const onDragChange = (checked: boolean) => {
    if (checked) {
      form.setFieldsValue({ "player.disabled_drag": 1 });
    } else {
      form.setFieldsValue({ "player.disabled_drag": 0 });
    }
  };

  const addName = (e: CheckboxChangeEvent) => {
    var value = form.getFieldValue("player.bullet_secret_text");
    if (e.target.checked) {
      value += "{name}";
    } else {
      value = value.replace("{name}", "");
    }
    form.setFieldsValue({
      "player.bullet_secret_text": value,
    });
    setNameChecked(!nameChecked);
  };

  const addEmail = (e: CheckboxChangeEvent) => {
    var value = form.getFieldValue("player.bullet_secret_text");
    if (e.target.checked) {
      value += "{email}";
    } else {
      value = value.replace("{email}", "");
    }
    form.setFieldsValue({
      "player.bullet_secret_text": value,
    });
    setEmailChecked(!emailChecked);
  };
  const addIdCard = (e: CheckboxChangeEvent) => {
    var value = form.getFieldValue("player.bullet_secret_text");
    if (e.target.checked) {
      value += "{idCard}";
    } else {
      value = value.replace("{idCard}", "");
    }
    form.setFieldsValue({
      "player.bullet_secret_text": value,
    });
    setIdCardChecked(!idCardchecked);
  };

  const onFinish = (values: any) => {
    if (loading) {
      return;
    }
    setLoading(true);
    values["player.bullet_secret_opacity"] =
      values["player.bullet_secret_opacity"] / 100;
    appConfig.saveAppConfig(values).then((res: any) => {
      message.success("保存成功！");
      setLoading(false);
      getDetail();
      getSystemConfig();
    });
  };

  const getSystemConfig = async () => {
    system.getSystemConfig().then((res: any) => {
      let data: SystemConfigStoreInterface = {
        "ldap-enabled": res.data["ldap-enabled"],
        systemName: res.data["system.name"],
        systemLogo: res.data["system.logo"],
        systemApiUrl: res.data["system.api_url"],
        systemPcUrl: res.data["system.pc_url"],
        systemH5Url: res.data["system.h5_url"],
        memberDefaultAvatar: res.data["member.default_avatar"],
        courseDefaultThumbs: res.data["default.course_thumbs"],
        departments: res.data["departments"],
        resourceCategories: res.data["resource_categories"],
      };
      dispatch(saveConfigAction(data));
    });
  };

  const onFinishFailed = (errorInfo: any) => {
    console.log("Failed:", errorInfo);
  };

  const onLDAPChange = (checked: boolean) => {
    if (checked) {
      form.setFieldsValue({ "ldap.enabled": 1 });
    } else {
      form.setFieldsValue({ "ldap.enabled": 0 });
    }
  };

  const items: TabsProps["items"] = [
    {
      key: "1",
      label: `网站设置`,
      children: (
        <Form
          form={form}
          name="basic"
          labelCol={{ span: 3 }}
          wrapperCol={{ span: 21 }}
          style={{ width: 1000, paddingTop: 30 }}
          onFinish={onFinish}
          onFinishFailed={onFinishFailed}
          autoComplete="off"
        >
          {logo && (
            <Form.Item
              style={{ marginBottom: 30 }}
              label="PC学员端Logo"
              name="system.logo"
              labelCol={{ style: { marginTop: 4, marginLeft: 24 } }}
            >
              <div className="d-flex">
                <Image preview={false} height={40} src={logo} />
                <div className="d-flex ml-24">
                  <UploadImageButton
                    text="更换Logo"
                    onSelected={(url) => {
                      setLogo(url);
                      form.setFieldsValue({ "system.logo": url });
                    }}
                  ></UploadImageButton>
                </div>
                <div className="helper-text ml-8">
                  （推荐尺寸:240x80px，支持JPG、PNG）
                </div>
              </div>
            </Form.Item>
          )}
          {!logo && (
            <Form.Item
              style={{ marginBottom: 30 }}
              label="PC学员端Logo"
              name="system.logo"
            >
              <div className="d-flex">
                <div className="d-flex ml-24">
                  <UploadImageButton
                    text="更换Logo"
                    onSelected={(url) => {
                      setLogo(url);
                      form.setFieldsValue({ "system.logo": url });
                    }}
                  ></UploadImageButton>
                </div>
                <div className="helper-text ml-8">
                  （推荐尺寸:240x80px，支持JPG、PNG）
                </div>
              </div>
            </Form.Item>
          )}
          <Form.Item
            style={{ marginBottom: 30 }}
            label="API访问地址"
            name="system.api_url"
          >
            <Input style={{ width: 274 }} placeholder="请填写API访问地址" />
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            label="PC学员端地址"
            name="system.pc_url"
          >
            <Input style={{ width: 274 }} placeholder="请填写PC学员端地址" />
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            label="H5学员端地址"
            name="system.h5_url"
          >
            <Input style={{ width: 274 }} placeholder="请填写H5学员端地址" />
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            label="学员端标题"
            name="system.name"
          >
            <Input
              style={{ width: 274 }}
              allowClear
              placeholder="请填写学员端标题"
            />
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            label="学员端页脚"
            name="system.pc_index_footer_msg"
          >
            <Input
              style={{ width: 274 }}
              allowClear
              placeholder="请填写学员端页脚"
            />
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            wrapperCol={{ offset: 3, span: 21 }}
          >
            <Button type="primary" htmlType="submit" loading={loading}>
              保存
            </Button>
          </Form.Item>
        </Form>
      ),
    },
    {
      key: "2",
      label: `播放设置`,
      children: (
        <Form
          form={form}
          name="n-basic"
          labelCol={{ span: 3 }}
          wrapperCol={{ span: 21 }}
          style={{ width: 1000, paddingTop: 30 }}
          onFinish={onFinish}
          onFinishFailed={onFinishFailed}
          autoComplete="off"
        >
          <Form.Item style={{ marginBottom: 30 }} label="禁止拖动进度条">
            <Space align="baseline" style={{ height: 32 }}>
              <Form.Item name="player.disabled_drag" valuePropName="checked">
                <Switch onChange={onDragChange} />
              </Form.Item>
              <div className="helper-text">
                （打开后禁止学员在首次学习中拖动进度条，以防刷课）
              </div>
            </Space>
          </Form.Item>
          <Form.Item style={{ marginBottom: 30 }} label="播放器跑马灯">
            <Space align="baseline" style={{ height: 32 }}>
              <Form.Item
                name="player.is_enabled_bullet_secret"
                valuePropName="checked"
              >
                <Switch onChange={onSwitchChange} />
              </Form.Item>
              <div className="helper-text">
                （打开后播放器会随机出现跑马灯水印，以防录屏传播）
              </div>
            </Space>
          </Form.Item>
          <Form.Item style={{ marginBottom: 30 }} label="跑马灯内容">
            <Space align="baseline" style={{ height: 32 }}>
              <Form.Item name="player.bullet_secret_text">
                <Input
                  style={{ width: 274 }}
                  allowClear
                  placeholder="自定义跑马灯内容"
                  onChange={(e) => {
                    const { value } = e.target;
                    if (!value && e.type !== "change") {
                      setNameChecked(false);
                      setEmailChecked(false);
                      setIdCardChecked(false);
                    }
                  }}
                />
              </Form.Item>
              <Checkbox
                checked={nameChecked}
                className="ml-24"
                onChange={addName}
              >
                姓名
              </Checkbox>
              <Checkbox
                checked={emailChecked}
                className="ml-24"
                onChange={addEmail}
              >
                邮箱
              </Checkbox>
            </Space>
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            label="跑马灯文字颜色"
            name="player.bullet_secret_color"
          >
            <Input type="color" style={{ width: 32, padding: 0 }} />
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            label="跑马灯不透明度"
            name="player.bullet_secret_opacity"
          >
            <Slider style={{ width: 400 }} range defaultValue={[0, 100]} />
          </Form.Item>
          {thumb && (
            <Form.Item
              style={{ marginBottom: 30 }}
              label="播放器封面"
              name="player.poster"
              labelCol={{ style: { marginTop: 75, marginLeft: 42 } }}
            >
              <div className="d-flex">
                <Image
                  preview={false}
                  height={180}
                  src={thumb}
                  style={{ borderRadius: 6 }}
                />
                <div className="d-flex ml-24">
                  <UploadImageButton
                    text="更换封面"
                    onSelected={(url) => {
                      setThumb(url);
                      form.setFieldsValue({ "player.poster": url });
                    }}
                  ></UploadImageButton>
                  <div className="helper-text ml-8">
                    （推荐尺寸:1920x1080px，视频播放未开始时展示）
                  </div>
                </div>
              </div>
            </Form.Item>
          )}
          {!thumb && (
            <Form.Item
              style={{ marginBottom: 30 }}
              label="播放器封面"
              name="player.poster"
            >
              <div className="d-flex">
                <div className="d-flex">
                  <UploadImageButton
                    text="更换封面"
                    onSelected={(url) => {
                      setThumb(url);
                      form.setFieldsValue({ "player.poster": url });
                    }}
                  ></UploadImageButton>
                  <div className="helper-text ml-8">
                    （推荐尺寸:1920x1080px，视频播放未开始时展示）
                  </div>
                </div>
              </div>
            </Form.Item>
          )}
          <Form.Item
            style={{ marginBottom: 30 }}
            wrapperCol={{ offset: 3, span: 21 }}
          >
            <Button type="primary" htmlType="submit" loading={loading}>
              保存
            </Button>
          </Form.Item>
        </Form>
      ),
    },
    {
      key: "3",
      label: `学员设置`,
      children: (
        <Form
          form={form}
          name="m-basic"
          labelCol={{ span: 3 }}
          wrapperCol={{ span: 21 }}
          style={{ width: 1000, paddingTop: 30 }}
          onFinish={onFinish}
          onFinishFailed={onFinishFailed}
          autoComplete="off"
        >
          {avatar && (
            <Form.Item
              style={{ marginBottom: 30 }}
              label="学员默认头像"
              name="member.default_avatar"
              labelCol={{ style: { marginTop: 14, marginLeft: 28 } }}
            >
              <div className="d-flex">
                <Image
                  preview={false}
                  width={60}
                  height={60}
                  src={avatar}
                  style={{ borderRadius: "50%" }}
                />
                <div className="d-flex ml-24">
                  <UploadImageButton
                    text="更换头像"
                    onSelected={(url) => {
                      setAvatar(url);
                      form.setFieldsValue({ "member.default_avatar": url });
                    }}
                  ></UploadImageButton>
                  <div className="helper-text ml-8">（新学员的默认头像）</div>
                </div>
              </div>
            </Form.Item>
          )}
          {!avatar && (
            <Form.Item
              style={{ marginBottom: 30 }}
              label="学员默认头像"
              name="member.default_avatar"
            >
              <div className="d-flex">
                <div className="d-flex">
                  <UploadImageButton
                    text="更换头像"
                    onSelected={(url) => {
                      setAvatar(url);
                      form.setFieldsValue({ "member.default_avatar": url });
                    }}
                  ></UploadImageButton>
                  <div className="helper-text ml-8">（新学员的默认头像）</div>
                </div>
              </div>
            </Form.Item>
          )}
          <Form.Item
            style={{ marginBottom: 30 }}
            wrapperCol={{ offset: 3, span: 21 }}
          >
            <Button type="primary" htmlType="submit" loading={loading}>
              保存
            </Button>
          </Form.Item>
        </Form>
      ),
    },
    {
      key: "4",
      label: `MinIO存储`,
      children: (
        <Form
          form={form}
          name="IO-basic"
          labelCol={{ span: 3 }}
          wrapperCol={{ span: 21 }}
          style={{ width: 1000, paddingTop: 30 }}
          onFinish={onFinish}
          onFinishFailed={onFinishFailed}
          autoComplete="off"
        >
          <Form.Item
            style={{ marginBottom: 30 }}
            label="AccessKey"
            name="minio.access_key"
          >
            <Input
              style={{ width: 274 }}
              allowClear
              placeholder="请填写AccessKey"
            />
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            label="SecretKey"
            name="minio.secret_key"
          >
            <Input
              style={{ width: 274 }}
              allowClear
              placeholder="请填写SecretKey"
            />
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            label="Bucket"
            name="minio.bucket"
          >
            <Input
              style={{ width: 274 }}
              allowClear
              placeholder="请填写Bucket"
            />
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            label="Endpoint"
            name="minio.endpoint"
          >
            <Input
              style={{ width: 274 }}
              allowClear
              placeholder="请填写Endpoint"
            />
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            label="Domain"
            name="minio.domain"
          >
            <Input
              style={{ width: 274 }}
              allowClear
              placeholder="请填写Domain"
            />
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            wrapperCol={{ offset: 3, span: 21 }}
          >
            <Button type="primary" htmlType="submit" loading={loading}>
              保存
            </Button>
          </Form.Item>
        </Form>
      ),
    },
    {
      key: "5",
      label: `LDAP配置`,
      children: (
        <Form
          form={form}
          name="LDAP-basic"
          labelCol={{ span: 3 }}
          wrapperCol={{ span: 21 }}
          style={{ width: 1000, paddingTop: 30 }}
          onFinish={onFinish}
          onFinishFailed={onFinishFailed}
          autoComplete="off"
        >
          <Form.Item
            style={{ marginBottom: 30 }}
            label="启用"
            name="ldap.enabled"
            valuePropName="checked"
          >
            <Switch onChange={onLDAPChange} />
          </Form.Item>
          <Form.Item style={{ marginBottom: 30 }} label="服务地址">
            <Space align="baseline" style={{ height: 32 }}>
              <Form.Item name="ldap.url">
                <Input
                  style={{ width: 274 }}
                  allowClear
                  placeholder="请填写服务地址"
                />
              </Form.Item>
              <div className="helper-text">
                （LDAP的对外服务地址。例如：ldap://ldap.example.com:389）
              </div>
            </Space>
          </Form.Item>
          <Form.Item style={{ marginBottom: 30 }} label="用户名">
            <Space align="baseline" style={{ height: 32 }}>
              <Form.Item name="ldap.admin_user">
                <Input
                  style={{ width: 274 }}
                  allowClear
                  placeholder="请填写用户名"
                />
              </Form.Item>
              <div className="helper-text">
                （用户登录到LDAP。例子：cn=admin,dc=playedu,dc=xyz）
              </div>
            </Space>
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            name="ldap.admin_pass"
            label="密码"
          >
            <Input style={{ width: 274 }} allowClear placeholder="请填写密码" />
          </Form.Item>
          <Form.Item style={{ marginBottom: 30 }} label="基本DN">
            <Space align="baseline" style={{ height: 32 }}>
              <Form.Item name="ldap.base_dn">
                <Input
                  style={{ width: 274 }}
                  allowClear
                  placeholder="请填写基本DN"
                />
              </Form.Item>
              <div className="helper-text">（从此节点搜索用户）</div>
            </Space>
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            wrapperCol={{ offset: 3, span: 21 }}
          >
            <Button type="primary" htmlType="submit" loading={loading}>
              保存
            </Button>
          </Form.Item>
        </Form>
      ),
    },
  ];

  const onChange = (key: string) => {
    setTabKey(Number(key));
  };

  return (
    <>
      <Row className="playedu-main-body">
        <Tabs
          className="float-left"
          defaultActiveKey="1"
          items={items}
          onChange={onChange}
        />
      </Row>
    </>
  );
};

export default SystemConfigPage;
