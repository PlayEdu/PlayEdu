import React, { useState, useEffect } from "react";
import styles from "./index.module.less";
import closeIcon from "../../../../../assets/images/commen/close.png";

interface PropInterface {
  id: number;
  url: string;
  open: boolean;
  onCancel: () => void;
}

declare const window: any;

export const VideoPlayDialog: React.FC<PropInterface> = ({
  id,
  url,
  open,
  onCancel,
}) => {
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (open && url) {
      initDPlayer(url);
    }
  }, [id, open, url]);

  const initDPlayer = (playUrl: string) => {
    window.player = new window.DPlayer({
      container: document.getElementById("meedu-player-container"),
      autoplay: false,
      video: {
        url: playUrl,
      },
    });
    window.player.on("ended", () => {
      window.player && window.player.destroy();
      onCancel();
    });
  };

  return (
    <>
      {open && (
        <div className={styles["play-mask"]}>
          <div className={styles["play-dialog"]}>
            <div
              className={styles["close-button"]}
              onClick={() => {
                window.player && window.player.destroy();
                onCancel();
              }}
            >
              <img src={closeIcon} />
            </div>
            <div
              className={styles["play-box"]}
              id="meedu-player-container"
            ></div>
          </div>
        </div>
      )}
    </>
  );
};
