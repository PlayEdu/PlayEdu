import { useEffect, useState } from "react";
import { ImageUploader, Skeleton, Toast, Mask, Image } from "antd-mobile";
import { useNavigate } from "react-router-dom";
import { user as member } from "../../api/index";
import { getDepName, studyTimeFormat } from "../../utils/index";
import { loginAction, logoutAction } from "../../store/user/loginUserSlice";
import { ImageUploadItem } from "antd-mobile/es/components/image-uploader";
import styles from "./index.module.scss";
import { useDispatch, useSelector } from "react-redux";
import moreIcon from "../../assets/images/commen/icon-more.png";
import memberDefaultAvatar from "../../assets/thumb/avatar.png";

const MemberPage = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [currentDepartment, setCurrentDepartment] = useState("");
  const [visible, setVisible] = useState(false);
  const [init, setInit] = useState(false);
  const [stats, setStats] = useState<any>({});
  const [fileList, setFileList] = useState<ImageUploadItem[]>([
    {
      url: "",
    },
  ]);
  const [learnTodayHour, setLearnTodayHour] = useState(0);
  const [learnTodayMin, setLearnTodayMin] = useState(0);
  const [learnTotalHour, setLearnTotalHour] = useState(0);
  const [learnTotalMin, setLearnTotalMin] = useState(0);
  const user = useSelector((state: any) => state.loginUser.value.user);
  const departments = useSelector(
    (state: any) => state.loginUser.value.departments
  );
  const currentDepId = useSelector(
    (state: any) => state.loginUser.value.currentDepId
  );
  const resourceUrl = useSelector(
    (state: any) => state.loginUser.value.resourceUrl
  );

  useEffect(() => {
    document.title = "我的";
    getUser();
  }, []);

  useEffect(() => {
    if (departments.length > 0) {
      setCurrentDepartment(getDepName() || departments[0].name);
    }
  }, [departments]);

  useEffect(() => {
    if (currentDepId === 0) {
      return;
    }
    getData();
  }, [currentDepId]);

  const getData = () => {
    setLoading(true);
    member
      .courses(currentDepId, 0)
      .then((res: any) => {
        setStats(res.data.stats);
        let todayData = studyTimeFormat(res.data.stats.today_learn_duration);
        if (todayData) {
          setLearnTodayHour(todayData[0]);
          setLearnTodayMin(todayData[1]);
          if (todayData[1] === 0 && todayData[2] > 0) {
            setLearnTodayMin(1);
          }
        }
        let totalData = studyTimeFormat(res.data.stats.learn_duration);
        if (totalData) {
          setLearnTotalHour(totalData[0]);
          setLearnTotalMin(totalData[1]);
          if (totalData[1] === 0 && totalData[2] > 0) {
            setLearnTodayMin(1);
          }
        }
        setLoading(false);
      })
      .catch((e) => {
        setLoading(false);
      });
  };

  const setClick = () => {
    setVisible(true);
  };

  const getTotal = (num1: number, num2: number) => {
    let value = 0;
    if (num1) {
      value = value + num1;
    }
    if (num2 && num2 > 0) {
      value = value + num2;
    }

    return value;
  };

  // const beforeUpload = (file: File) => {
  //   if (file.size > 2 * 1024 * 1024) {
  //     Toast.show("超过2M限制，不允许上传");
  //     return null;
  //   }
  //   return file;
  // };

  const mockUpload = async (file: File) => {
    setVisible(false);
    const data = new FormData();
    data.append("file", file);
    try {
      let res = await member.avatar(data);
      if (res) {
        Toast.show("头像更换成功");
        await getUser(); //获取登录用户的信息并写入store
      }
    } catch (e) {
      console.error("上传失败", e);
    }
    return {
      url: URL.createObjectURL(file),
    };
  };

  const getUser = async () => {
    let res: any = await member.detail();
    if (res) {
      dispatch(loginAction(res.data));
      setFileList([]);
      setInit(true);
    }
  };

  return (
    <div className={styles["main-body"]}>
      <div className={styles["content-box"]}>
        <div className={styles["top-content"]}>
          <div className={styles["user-info"]}>
            {!init && (
              <>
                <Skeleton
                  animated
                  style={{
                    width: 100,
                    height: 100,
                    borderRadius: "50%",
                    marginRight: 20,
                  }}
                />
                <div className={styles["other-cont"]}>
                  <Skeleton
                    animated
                    style={{ width: "100%", height: 20, marginTop: 15 }}
                  />
                  <Skeleton
                    animated
                    style={{ width: "100%", height: 24, marginTop: 20 }}
                  />
                </div>
              </>
            )}
            {init && (
              <>
                {user ? (
                  <Image
                    width={100}
                    height={100}
                    style={{
                      borderRadius: "50%",
                      marginRight: 20,
                    }}
                    fit="cover"
                    src={
                      user.avatar === -1
                        ? memberDefaultAvatar
                        : resourceUrl[user.avatar]
                    }
                  />
                ) : null}
                <div className={styles["other-cont"]}>
                  <div className={styles["name"]}>{user?.name}</div>
                  <div className={styles["departments"]}>
                    <div className={styles["department-name"]}>
                      {currentDepartment}
                    </div>
                  </div>
                </div>
              </>
            )}
          </div>
          <Image
            className={styles["more-button"]}
            onClick={() => setClick()}
            src={moreIcon}
          />
        </div>
        <div className={styles["stats-content"]}>
          <div className={styles["stat-item"]}>
            <span className={styles["time"]}>
              <strong> {learnTodayHour} </strong>时
              <strong> {learnTodayMin} </strong>分
            </span>
            <span className={styles["tit"]}>今日学习</span>
          </div>
          <div className={styles["stat-item"]}>
            <span className={styles["time"]}>
              <strong> {learnTotalHour} </strong>时
              <strong> {learnTotalMin} </strong>分
            </span>
            <span className={styles["tit"]}>累计学习</span>
          </div>
        </div>
        <div className={styles["records-content"]}>
          <div className={styles["record-item"]}>
            <div className={styles["name"]}>所在部门</div>
            <div className={styles["value"]}>{currentDepartment}</div>
          </div>
          <div className={styles["record-item"]}>
            <div className={styles["name"]}>课时总进度</div>
            <div className={styles["value"]}>
              <strong>
                {getTotal(
                  stats.required_finished_hour_count,
                  stats.nun_required_finished_hour_count
                )}{" "}
              </strong>
              /{" "}
              {getTotal(
                stats.required_hour_count,
                stats.nun_required_hour_count
              )}
            </div>
          </div>
          <div className={styles["record-item"]}>
            <div className={styles["name"]}>必修课</div>
            <div className={styles["value"]}>
              已学完{" "}
              <strong>{stats.required_finished_course_count || 0} </strong>/{" "}
              {stats.required_course_count || 0}
            </div>
          </div>

          <div className={styles["record-item"]}>
            <div className={styles["name"]}>选修课</div>
            {stats.nun_required_course_count > 0 ? (
              <div className={styles["value"]}>
                已学完{" "}
                <strong>
                  {stats.nun_required_finished_course_count || 0}{" "}
                </strong>
                / {stats.nun_required_course_count || 0}
              </div>
            ) : (
              <div className={styles["value"]}>
                已学完 <strong>0 </strong>/ 0
              </div>
            )}
          </div>
        </div>
      </div>
      <Mask
        visible={visible}
        onMaskClick={() => {
          setVisible(false);
        }}
      >
        <div className={styles["dialog-body"]}>
          <div className={styles["dialog-box"]}>
            <div
              className={styles["button-item"]}
              onClick={() => {
                setVisible(false);
                if (departments.length === 1) {
                  Toast.show({
                    content: "暂无可切换部门",
                  });
                  return;
                }
                navigate("/change-department");
              }}
            >
              切换部门
            </div>
            <ImageUploader
              value={fileList}
              onChange={setFileList}
              upload={mockUpload}
              preview={false}
              showFailed={false}
            >
              <div className={styles["button-item"]}>更换头像</div>
            </ImageUploader>
            <div
              className={styles["button-item"]}
              onClick={() => {
                setVisible(false);
                navigate("/change-password");
              }}
            >
              修改密码
            </div>
          </div>
          <div
            className={styles["dialog-button"]}
            onClick={() => {
              setVisible(false);
              dispatch(logoutAction());
              window.location.href = "/login";
            }}
          >
            退出登录
          </div>
        </div>
      </Mask>
    </div>
  );
};

export default MemberPage;
