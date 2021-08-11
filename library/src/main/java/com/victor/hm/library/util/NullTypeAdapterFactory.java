package com.victor.hm.library.util;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.victor.hm.library.adapter.FloatNullAdapter;
import com.victor.hm.library.adapter.IntegerNullAdapter;

public class NullTypeAdapterFactory<T> implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<T> rawType = (Class<T>) type.getRawType();
        if (rawType == Float.class || rawType == float.class) {
            return (TypeAdapter<T>) new FloatNullAdapter();
        }else if (rawType == Integer.class || rawType == int.class){
            return (TypeAdapter<T>) new IntegerNullAdapter();
        }
        return null;
    }
}