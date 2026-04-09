import ReactDOM from "react-dom/client";
import "./assets/iconfont/iconfont.css";
import "./index.less";
import App from "./App";
import { BrowserRouter } from "react-router-dom";
import { Provider } from "react-redux";
import store from "./store";
import { ConfigProvider } from "antd";
import zhCN from "antd/locale/zh_CN";
import "dayjs/locale/zh-cn";
import AutoScorllTop from "./AutoTop";

ReactDOM.createRoot(document.getElementById("root") as HTMLElement).render(
  <Provider store={store}>
    <ConfigProvider
      locale={zhCN}
      theme={{
        token: {
          colorPrimary: "#1890ff",
          colorLink: "#1890ff",
          borderRadius: 8,
          boxShadow: "0 2px 8px rgba(0, 0, 0, 0.09)"
        },
        components: {
          Button: {
            borderRadius: 8,
          },
          Card: {
            borderRadius: 8,
          },
          Input: {
            borderRadius: 8,
          },
          Select: {
            borderRadius: 8,
          }
        }
      }}
    >
      <BrowserRouter>
        <AutoScorllTop>
          <App />
        </AutoScorllTop>
      </BrowserRouter>
    </ConfigProvider>
  </Provider>
);
