package com.victor.hm.library.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static final Gson mGson = buildGson();

    /**
     * 日志标签
     */
    private static final String TAG = "JsonUtils";

    public static String toJSONString(Object object) {
        try {
            String json = mGson.toJson(object);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        try {
            T result = mGson.fromJson(text, clazz);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T parseObject(String text, Type clazz) {
        try {
            return mGson.fromJson(text, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        if (TextUtils.isEmpty(text)) return null;
        if (clazz == null) return null;
        try {
            Type type = new TypeToken<ArrayList<JsonObject>> () {}.getType();
            List<JsonObject> jsonObjects = mGson.fromJson(text, type);
            List<T> arrayList = new ArrayList();

            for (JsonObject jsonObject : jsonObjects) {
                arrayList.add(mGson.fromJson(jsonObject, clazz));
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Gson buildGson() {
        return new GsonBuilder().registerTypeAdapterFactory(new NullTypeAdapterFactory()).create();
    }

}
