package com.laorencel.uilibrary.http.adapter;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * 解决：int 字端，但后台返回“”，这时会报错 java.lang.NumberFormatException: empty String
 */
public class IntegerTypeAdapter extends TypeAdapter<Number> {
    @Override
    public void write(JsonWriter out, Number value) throws IOException {
        out.value(value);
//        out.peek()
    }

    @Override
    public Number read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        try {
            String result = in.nextString();
            if ("".equals(result)) {
                return null;
            }
            return Integer.parseInt(result);
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }

//        return null;
    }
}
