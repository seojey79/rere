package com.seojey.domain.db.hbase.util;

/**
 * Created by kimjun on 16. 5. 31..
 */

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.seojey.domain.type.KeyFormatType;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.util.Bytes;


/**
 * Created by coupang on 2015. 8. 27..
 */
@Slf4j
public class RecommendUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private RecommendUtils() {
    }

    public static byte[] toBytes(Object obj) throws JsonProcessingException {
        if (obj == null) {
            return null;
        }

        if (obj instanceof Long) {
            return Bytes.toBytes((Long) obj);
        } else if (obj instanceof Integer) {
            return Bytes.toBytes((Integer) obj);
        } else if (obj instanceof Double) {
            return Bytes.toBytes((Double) obj);
        } else if (obj instanceof Float) {
            return Bytes.toBytes((Float) obj);
        } else if (obj instanceof String) {
            return Bytes.toBytes((String) obj);
        } else {
            return MAPPER.writeValueAsBytes(obj);
        }
    }

    public static Object fromBytes(byte[] value, Class<?> type, List<Class> typeArguments) throws IOException {
        if (value == null) {
            return null;
        }
        if (value.length <= 0) {
            return null;
        }
        if (type == Long.class) {
            return Bytes.toLong(value);
        } else if (type == Integer.class) {
            return Bytes.toInt(value);
        } else if (type == Double.class) {
            return Bytes.toDouble(value);
        } else if (type == Float.class) {
            return Bytes.toFloat(value);
        } else if (type == String.class) {
            return Bytes.toString(value);
        } else if (type.isAssignableFrom(List.class)) {
            if (typeArguments.size() == 1) {
                JavaType t = TypeFactory.defaultInstance().constructCollectionType(List.class, typeArguments.get(0));
                return MAPPER.readValue(value, t);
            }
        } else if (type.isAssignableFrom(Map.class)) {
            if (typeArguments.size() == 2) {
                JavaType t = TypeFactory.defaultInstance().constructMapType(Map.class, typeArguments.get(0), typeArguments.get(1));
                return MAPPER.readValue(value, t);
            }
        } else {
            JavaType t = TypeFactory.defaultInstance().constructFromCanonical(type.getCanonicalName());
            return MAPPER.readValue(value, t);
        }
        return null;
    }

    public static byte[] formattingKey(Object target, KeyFormatType type) throws JsonProcessingException {

        if (type == KeyFormatType.REVERSE) {
            return toBytes(Long.reverse((Long) target));
        } else if (type == KeyFormatType.MD5) {
            MessageDigest md5 = initMessageDigest();
            if (md5 != null) {
                return md5.digest(RecommendUtils.toBytes(target));
            }
        }

        return null;
    }


    public static String getHbaseFieldKey(String column, String qualifier) {
        return String.format("%s:%s", column, qualifier);
    }


    private static MessageDigest initMessageDigest() {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nae) {
            log.error("Exception on get instance of MessageDigest", nae.getMessage());
        }

        return md5;
    }
}
