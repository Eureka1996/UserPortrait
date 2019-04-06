package com.wufuqiang.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * @ author wufuqiang
 * @ date 2019/4/6/006 - 12:33
 **/
public class ReadProperties {
    public final static Config config = ConfigFactory.load("kafkatopic.properties") ;
    public static String getKey(String key){
        return config.getString(key).trim() ;
    }
}
