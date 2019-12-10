package com.wm.v1msg.util;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author weimin
 * @ClassName ChannelUtils
 * @Description TODO
 * @date 2019/11/27 15:11
 */
public class ChannelUtils {

    private static final Map<String, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();


    public static void add(String userId,Channel channel){
        CHANNEL_MAP.put(userId,channel);
    }

    public static void remove(String userId){
        CHANNEL_MAP.remove(userId);
    }

    public static Channel getChannel(String userId){
        return CHANNEL_MAP.get(userId);
    }


    public static void remove(Channel channel){
        CHANNEL_MAP.forEach((k,v)->{
            if(v==channel){
                CHANNEL_MAP.remove(k);
            }
        });
        System.out.println("map大小"+CHANNEL_MAP.size());
    }
}
