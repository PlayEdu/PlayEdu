/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.util;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/3 10:51
 */
public class PrivacyUtil {

    public static String hidePhone(String phone) {
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    public static String hideEmail(String email) {
        return email.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
    }

    public static String hideIDCard(String idCard) {
        return idCard.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1*****$2");
    }

    public static String hideChineseName(String chineseName) {
        if (chineseName == null) {
            return null;
        }
        return desValue(chineseName, 1, 0, "*");
    }

    /**
     * 对字符串进行脱敏操作
     *
     * @param origin 原始字符串
     * @param prefixNoMaskLen 左侧需要保留几位明文字段
     * @param suffixNoMaskLen 右侧需要保留几位明文字段
     * @param maskStr 用于遮罩的字符串, 如'*'
     * @return 脱敏后结果
     */
    public static String desValue(
            String origin, int prefixNoMaskLen, int suffixNoMaskLen, String maskStr) {
        if (origin == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0, n = origin.length(); i < n; i++) {
            if (i < prefixNoMaskLen) {
                sb.append(origin.charAt(i));
                continue;
            }
            if (i > (n - suffixNoMaskLen - 1)) {
                sb.append(origin.charAt(i));
                continue;
            }
            sb.append(maskStr);
        }
        return sb.toString();
    }
}
