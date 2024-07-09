package com.treeview.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

@Slf4j
public class RedisSerializeUtil {
    private static final JdkSerializationRedisSerializer JDK_SERIALIZATION_REDIS_SERIALIZER = new JdkSerializationRedisSerializer();

    public static <T> byte[] serialize(T obj) {
        try {
            return JDK_SERIALIZATION_REDIS_SERIALIZER.serialize(obj);
        } catch (Exception var2) {
            Exception e = var2;
            log.error("Exception:", e);
            throw new RuntimeException("序列化失败!", e);
        }
    }

    public static <T> T deserialize(byte[] bytes) {
        try {
            return (T) JDK_SERIALIZATION_REDIS_SERIALIZER.deserialize(bytes);
        } catch (Exception e) {
            log.error("Exception:", e);
            throw new RuntimeException("反序列化失败!", e);
        }
    }
}