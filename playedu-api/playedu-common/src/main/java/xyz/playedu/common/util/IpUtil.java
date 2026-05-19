/*
 * Copyright (C) 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.common.util;

import jakarta.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

@Slf4j
public class IpUtil {

    private static final String XDB_CLASSPATH = "ip2region/ip2region.xdb";
    private static volatile Searcher ipSearcher;

    private static Searcher getSearcher() {
        if (ipSearcher == null) {
            synchronized (IpUtil.class) {
                if (ipSearcher == null) {
                    try (InputStream in =
                            IpUtil.class.getClassLoader().getResourceAsStream(XDB_CLASSPATH)) {
                        if (in == null) {
                            log.error("ip2region xdb 资源未找到: {}", XDB_CLASSPATH);
                            return null;
                        }
                        ByteArrayOutputStream buf = new ByteArrayOutputStream();
                        byte[] chunk = new byte[8192];
                        int n;
                        while ((n = in.read(chunk)) != -1) {
                            buf.write(chunk, 0, n);
                        }
                        ipSearcher = Searcher.newWithBuffer(buf.toByteArray());
                    } catch (Exception e) {
                        log.error("初始化 ip2region 失败 msg={}", e.getMessage());
                    }
                }
            }
        }
        return ipSearcher;
    }

    /**
     * 获取客户端IP
     *
     * @return IP地址 (113.67.10.194)
     * @author fzr
     */
    public static String getIpAddress() {
        HttpServletRequest request = RequestUtil.handler();
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : getMultistageReverseProxyIp(ip);
    }

    /**
     * 根据IP获取所在地址
     *
     * @param ip Ip地址
     * @return String (广州省-广州市)
     * @author fzr
     */
    public static String getRealAddressByIP(String ip) {
        String UNKNOWN = "未知";

        if (StringUtil.isEmpty(ip)) {
            return UNKNOWN;
        }

        if (IpUtil.internalIp(ip)) {
            return "内网";
        }

        Searcher searcher = getSearcher();
        if (searcher == null) {
            return UNKNOWN;
        }

        try {
            // xdb 返回格式：国家|省份|城市|ISP|国家代码，例如 "中国|浙江省|杭州市|电信|CN"
            String region = searcher.search(ip);
            if (StringUtil.isEmpty(region)) {
                return UNKNOWN;
            }
            String[] parts = region.split("\\|", -1);
            String country = parts.length > 0 ? cleanField(parts[0]) : "";
            String pro = parts.length > 1 ? cleanField(parts[1]) : "";
            String city = parts.length > 2 ? cleanField(parts[2]) : "";
            if (!StringUtil.isEmpty(pro) && !StringUtil.isEmpty(city)) {
                return String.format("%s-%s", pro, city);
            }
            if (!StringUtil.isEmpty(pro)) {
                return pro;
            }
            if (!StringUtil.isEmpty(country)) {
                return country;
            }
            return UNKNOWN;
        } catch (Exception e) {
            log.warn("获取地理位置异常 ip={} msg={}", ip, e.getMessage());
        }
        return UNKNOWN;
    }

    private static String cleanField(String s) {
        if (s == null || "0".equals(s)) {
            return "";
        }
        return s.trim();
    }

    /**
     * 检查是否为内部IP地址
     *
     * @param ip IP地址
     * @return 结果
     */
    public static boolean internalIp(String ip) {
        byte[] address = textToNumericFormatV4(ip);
        return internalIp(address) || "127.0.0.1".equals(ip);
    }

    /**
     * 检查是否为内部IP地址
     *
     * @param address byte地址
     * @return 结果
     * @author fzr
     */
    private static boolean internalIp(byte[] address) {
        if (address == null || address.length < 2) {
            return true;
        }
        final byte b0 = address[0];
        final byte b1 = address[1];
        // 10.x.x.x/8
        final byte SECTION_1 = 0x0A;
        // 172.16.x.x/12
        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;
        // 192.168.x.x/16
        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;
        switch (b0) {
            case SECTION_1:
                return true;
            case SECTION_2:
                if (b1 >= SECTION_3 && b1 <= SECTION_4) {
                    return true;
                }
            case SECTION_5:
                if (b1 == SECTION_6) {
                    return true;
                }
            default:
                return false;
        }
    }

    /**
     * 将IPv4地址转换成字节
     *
     * @param text IPv4地址
     * @return byte 字节
     * @author fzr
     */
    public static byte[] textToNumericFormatV4(String text) {
        if (text.length() == 0) {
            return null;
        }

        byte[] bytes = new byte[4];
        String[] elements = text.split("\\.", -1);
        try {
            long l;
            int i;
            switch (elements.length) {
                case 1:
                    l = Long.parseLong(elements[0]);
                    if ((l < 0L) || (l > 4294967295L)) {
                        return null;
                    }
                    bytes[0] = (byte) (int) (l >> 24 & 0xFF);
                    bytes[1] = (byte) (int) ((l & 0xFFFFFF) >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 2:
                    l = Integer.parseInt(elements[0]);
                    if ((l < 0L) || (l > 255L)) {
                        return null;
                    }
                    bytes[0] = (byte) (int) (l & 0xFF);
                    l = Integer.parseInt(elements[1]);
                    if ((l < 0L) || (l > 16777215L)) {
                        return null;
                    }
                    bytes[1] = (byte) (int) (l >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 3:
                    for (i = 0; i < 2; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return null;
                        }
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    l = Integer.parseInt(elements[2]);
                    if ((l < 0L) || (l > 65535L)) {
                        return null;
                    }
                    bytes[2] = (byte) (int) (l >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 4:
                    for (i = 0; i < 4; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return null;
                        }
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    break;
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return bytes;
    }

    /**
     * 获取IP地址
     *
     * @return 本地IP地址
     * @author fzr
     */
    public static String getHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ignored) {
        }
        return "127.0.0.1";
    }

    /**
     * 从多级反向代理中获得第一个非unknown IP地址
     *
     * @param ip 获得的IP地址
     * @return 第一个非unknown IP地址
     * @author fzr
     */
    public static String getMultistageReverseProxyIp(String ip) {
        if (ip != null && ip.indexOf(",") > 0) {
            final String[] ips = ip.trim().split(",");
            for (String subIp : ips) {
                if (!isUnknown(subIp)) {
                    ip = subIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * 检测给定字符串是否为未知,多用于检测HTTP请求相关
     *
     * @param checkString 被检测的字符串
     * @return 是否未知
     * @author fzr
     */
    public static boolean isUnknown(String checkString) {
        return StringUtil.isBlank(checkString) || "unknown".equalsIgnoreCase(checkString);
    }
}
