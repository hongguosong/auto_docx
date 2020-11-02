package com.example.demo.util;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.util </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2020/1/15 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
    /**
     * 将一组数据固定分组，每组n个元素
     *
     * @param source 要分组的数据源
     * @param n      每组n个元素
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> fixedGrouping2(List<T> source, int n) {

        if (null == source || source.size() == 0 || n <= 0)
            return null;
        List<List<T>> result = new ArrayList<List<T>>();
        int remainder = source.size() % n;
        int size = (source.size() / n);
        for (int i = 0; i < size; i++) {
            List<T> subset = null;
            subset = source.subList(i * n, (i + 1) * n);
            result.add(subset);
        }
        if (remainder > 0) {
            List<T> subset = null;
            subset = source.subList(size * n, size * n + remainder);
            result.add(subset);
        }
        return result;
    }
}
