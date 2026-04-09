import { __assign } from "tslib";
import { useEffect, useRef } from 'react';
function useWhyDidYouUpdate(componentName, props) {
    var prevProps = useRef({});
    useEffect(function () {
        if (prevProps.current) {
            var allKeys = Object.keys(__assign(__assign({}, prevProps.current), props));
            var changedProps_1 = {};
            allKeys.forEach(function (key) {
                if (!Object.is(prevProps.current[key], props[key])) {
                    changedProps_1[key] = {
                        from: prevProps.current[key],
                        to: props[key],
                    };
                }
            });
            if (Object.keys(changedProps_1).length) {
                console.log('[why-did-you-update]', componentName, changedProps_1);
            }
        }
        prevProps.current = props;
    });
}
export default useWhyDidYouUpdate;
