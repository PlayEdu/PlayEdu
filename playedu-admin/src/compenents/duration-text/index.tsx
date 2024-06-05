import { useEffect, useState } from "react";

interface PropInterface {
  duration: number;
}

export const DurationText = (props: PropInterface) => {
  const [hour, setHour] = useState(0);
  const [minute, setMinute] = useState(0);
  const [second, setSecond] = useState(0);
  const duration = props.duration;

  useEffect(() => {
    let h = Math.trunc(duration / 3600);
    let m = Math.trunc((duration % 3600) / 60);
    let s = Math.trunc((duration % 3600) % 60);

    setHour(h);
    setMinute(m);
    setSecond(s);
  }, []);

  return (
    <>
      <span>
        {hour === 0 ? null : hour + ":"}
        {minute >= 10 ? minute : "0" + minute}:
        {second >= 10 ? second : "0" + second}
      </span>
    </>
  );
};
