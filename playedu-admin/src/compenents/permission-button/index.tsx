import { Button } from "antd";
import { useSelector } from "react-redux";

interface PropInterface {
  type: "link" | "text" | "primary" | "default";
  text: string;
  p: string;
  class: string;
  icon?: any;
  onClick?: () => void;
  disabled: any;
}

export const PerButton = (props: PropInterface) => {
  const permissions = useSelector(
    (state: any) => state.loginUser.value.permissions
  );
  const isThrough = () => {
    if (!permissions) {
      return false;
    }
    return typeof permissions[props.p] !== "undefined";
  };
  return (
    <>
      {isThrough() && props.type === "link" && (
        <Button
          className={props.class}
          type="link"
          danger
          icon={props.icon}
          onClick={() => {
            props.onClick && props.onClick();
          }}
          disabled={props.disabled}
        >
          {props.text}
        </Button>
      )}
      {isThrough() && props.type !== "link" && (
        <Button
          className={props.class}
          type={props.type}
          icon={props.icon}
          onClick={() => {
            props.onClick && props.onClick();
          }}
          disabled={props.disabled}
        >
          {props.text}
        </Button>
      )}
    </>
  );
};
