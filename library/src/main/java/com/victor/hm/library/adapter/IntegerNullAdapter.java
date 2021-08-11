package com.victor.hm.library.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.victor.hm.library.util.TextUtils;

import java.io.IOException;

public class IntegerNullAdapter extends TypeAdapter<Number> {

    @Override
    public void write(JsonWriter jsonWriter, Number number) throws IOException {
        if (number == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(number);
    }

    @Override
    public Number read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return 0;
        }
        String value = jsonReader.nextString();
        String stringValue = TextUtils.isEmpty(value) ? "" : value;
        try {
            Integer.valueOf(stringValue);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }
}
