package com.laorencel.uilibrary.http.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * 解决：String 字端，但后台返回了对象
 */
public class StringTypeAdapter extends TypeAdapter<String> {
    @Override
    public void write(JsonWriter out, String value) throws IOException {
        out.value(value);
    }

    @Override
    public String read(JsonReader in) throws IOException {
        // LogUtils.e("read");
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        // number类的也会执行这个方法，但是number返回null 会报错，所以把Number排除
        if (in.peek() != JsonToken.STRING && in.peek() != JsonToken.NUMBER) {
            // LogUtils.e("typeAdapter","stringTypeAdapter 不是String = "+in.peek());
            //不是String，直接跳过，不解析
            in.skipValue();
            return null;
        }
        String result = in.nextString();

        // LogUtils.e("typeAdapter","stringTypeAdapter ="+result);
        return result;
//        return null;
    }
}
