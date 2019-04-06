package com.wufuqiang.utils;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @ author wufuqiang
 * @ date 2019/4/6/006 - 15:13
 **/
public class MapUtils {
    public static String getmaxbyMap(Map<String,Long> datamap){
        if(datamap.isEmpty()){
            return  null;
        }
        TreeMap<Long,String> map = new TreeMap<Long, String>(new Comparator<Long>() {
            public int compare(Long o1, Long o2) {
                return o2.compareTo(o1);
            }
        });
        for(Map.Entry<String,Long> entry :datamap.entrySet()){
            String key = entry.getKey();
            Long value = entry.getValue();
            map.put(value,key);
        }
        return map.get(map.firstKey());
    }
}
