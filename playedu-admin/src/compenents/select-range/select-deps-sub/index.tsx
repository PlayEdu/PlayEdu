import { useState } from "react";
import { TreeDeps } from "../../tree-deps";

interface PropsInterface {
  defaultkeys: any[];
  open: boolean;
  onSelected: (arr: any[], videos: any[]) => void;
}

export const SelectDepsSub = (props: PropsInterface) => {
  const [init, setInit] = useState(true);

  return (
    <div
      style={{
        width: 528,
        height: 458,
        overflowY: "auto",
        overflowX: "hidden",
      }}
    >
      <TreeDeps
        selected={props.defaultkeys}
        refresh={props.open}
        showNum={true}
        type=""
        onUpdate={(keys: any, nodes: any) => {
          props.onSelected(keys, nodes);
        }}
      ></TreeDeps>
    </div>
  );
};
