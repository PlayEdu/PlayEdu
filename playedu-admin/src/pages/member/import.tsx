import { useState } from "react";
import * as XLSX from "xlsx";
import { Row, Col, Button, Upload, message } from "antd";
import { BackBartment } from "../../compenents";
import { user } from "../../api/index";
import { useNavigate } from "react-router-dom";
import { getHost } from "../../utils/index";

const MemberImportPage = () => {
  const navigate = useNavigate();
  const [errorData, setErrorData] = useState<any>([]);

  const uploadProps = {
    accept: ".xls,.xlsx,application/vnd.ms-excel",
    beforeUpload: (file: any) => {
      const f = file;
      const reader = new FileReader();
      reader.onload = (e: any) => {
        const datas = e.target.result;
        const workbook = XLSX.read(datas, {
          type: "binary",
        });
        const first_worksheet = workbook.Sheets[workbook.SheetNames[0]];
        const jsonArr = XLSX.utils.sheet_to_json(first_worksheet, {
          header: 1,
        });
        handleImpotedJson(jsonArr, file);
      };
      reader.readAsBinaryString(f);
      return false;
    },
  };
  const handleImpotedJson = (jsonArr: any[], file: any) => {
    jsonArr.splice(0, 2); // 去掉表头[第一行规则描述,第二行表头名]
    let data: any[] = [];
    for (let i = 0; i < jsonArr.length; i++) {
      let tmpItem = jsonArr[i];
      if (typeof tmpItem === undefined) {
        break;
      }
      if (tmpItem.length === 0) {
        //空行
        continue;
      }
      data.push({
        deps: tmpItem[0],
        email: tmpItem[1],
        password: tmpItem[2],
        name: tmpItem[3],
        id_card: tmpItem[4],
      });
    }

    storeBatchTableData(data);
  };

  const storeBatchTableData = (data: any) => {
    user
      .storeBatch(2, data)
      .then(() => {
        setErrorData([]);
        message.success("导入成功！");
        navigate(-1);
      })
      .catch((e) => {
        if (e.code === -1) {
          setErrorData(e.data);
        }
      });
  };

  const download = () => {
    let url = getHost() + "template/学员批量导入模板.xlsx";
    window.open(url);
  };

  return (
    <>
      <Row className="playedu-main-body">
        <Col>
          <div className="float-left mb-24">
            <BackBartment title="学员批量导入" />
          </div>
          <div className="float-left d-flex  mb-24">
            <Upload {...uploadProps}>
              <Button type="primary">导入Excel</Button>
            </Upload>
            <Button type="link" className="ml-15" danger onClick={download}>
              下载「学员批量导入模板」
            </Button>
          </div>
          <div className="float-left c-flex">
            {errorData &&
              errorData.map((item: any, index: number) => {
                return (
                  <div key={index} className="c-red mb-10">
                    {item}
                  </div>
                );
              })}
          </div>
        </Col>
      </Row>
    </>
  );
};

export default MemberImportPage;
