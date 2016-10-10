package com.bzj.dragon.utils;

import java.util.Arrays;

/**
 * User:aaronbai@tcl.com
 * Date:2016-10-10
 * Time:14:55
 */
public class StrUtil {

    public static final String EMPTY = "";

    /**
     * 格式化文本
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param values 参数值
     * @return 格式化后的文本
     */
    public static String format(String template, Object... values) {
        if (CollectionUtil.isEmpty(values) || isBlank(template)) {
            return template;
        }

        final StringBuilder sb = new StringBuilder();
        final int length = template.length();

        int valueIndex = 0;
        char currentChar;
        for (int i = 0; i < length; i++) {
            if (valueIndex >= values.length) {
                sb.append(sub(template, i, length));
                break;
            }

            currentChar = template.charAt(i);
            if (currentChar == '{') {
                final char nextChar = template.charAt(++i);
                if (nextChar == '}') {
                    sb.append(values[valueIndex++]);
                } else {
                    sb.append('{').append(nextChar);
                }
            } else {
                sb.append(currentChar);
            }

        }
        return sb.toString();
    }

    /**
     * 字符串是否为空白 空白的定义如下： <br>
     * 1、为null <br>
     * 2、为不可见字符（如空格）<br>
     * 3、""<br>
     *
     * @param str 被检测的字符串
     * @return 是否为空
     */
    public static boolean isBlank(String str) {
        int length;

        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            // 只要有一个非空字符即为非空字符串
            if (false == Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 改进JDK subString<br>
     * index从0开始计算，最后一个字符为-1<br>
     * 如果from和to位置一样，返回 "" example: abcdefgh 2 3 -> c abcdefgh 2 -3 -> cde
     *
     * @param string String
     * @param fromIndex 开始的index（包括）
     * @param toIndex 结束的index（不包括）
     * @return 字串
     */
    public static String sub(String string, int fromIndex, int toIndex) {
        int len = string.length();

        if (fromIndex < 0) {
            fromIndex = len + fromIndex;

            if (toIndex == 0) {
                toIndex = len;
            }
        }

        if (toIndex < 0) {
            toIndex = len + toIndex;
        }

        if (toIndex < fromIndex) {
            int tmp = fromIndex;
            fromIndex = toIndex;
            toIndex = tmp;
        }

        if (fromIndex == toIndex) {
            return EMPTY;
        }

        char[] strArray = string.toCharArray();
        char[] newStrArray = Arrays.copyOfRange(strArray, fromIndex, toIndex);
        return new String(newStrArray);
    }
}
