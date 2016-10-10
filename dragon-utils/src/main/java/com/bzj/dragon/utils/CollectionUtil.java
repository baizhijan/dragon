package com.bzj.dragon.utils;

/**
 * User:aaronbai@tcl.com
 * Date:2016-10-10
 * Time:14:59
 */
public class CollectionUtil {
    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length <= 0;
    }
}
