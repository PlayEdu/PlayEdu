package xyz.playedu.api.bus;

import xyz.playedu.api.constant.BackendConstant;

public class BackendBus {

    public static boolean inUnAuthWhitelist(String uri) {
        for (int i = 0; i < BackendConstant.UN_AUTH_URI_WHITELIST.length; i++) {
            if (uri.equals(BackendConstant.UN_AUTH_URI_WHITELIST[i])) {
                return true;
            }
        }
        return false;
    }

}
