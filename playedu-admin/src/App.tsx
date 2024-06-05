import { Suspense } from "react";
import styles from "./App.module.less";
import { useRoutes } from "react-router-dom";
import routes from "./routes";
import LoadingPage from "./pages/loading";

function App() {
  const Views = () => useRoutes(routes);

  return (
    <Suspense fallback={<LoadingPage />}>
      <div className={styles.App}>
        <Views />
      </div>
    </Suspense>
  );
}

export default App;
