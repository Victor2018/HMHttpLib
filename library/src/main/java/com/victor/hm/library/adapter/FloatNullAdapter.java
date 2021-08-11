package com.victor.hm.library.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.math.BigDecimal;

public class FloatNullAdapter extends TypeAdapter<Float> {

    @Override
    public void write(JsonWriter jsonWriter, Float aFloat) throws IOException {
        jsonWriter.value(aFloat);
    }

    @Override
    public Float read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.STRING) {
            jsonReader.skipValue(); //跳过当前
            return -1f;
        }
        BigDecimal bigDecimal = new BigDecimal(jsonReader.nextString());
        return bigDecimal.floatValue();
    }
}
