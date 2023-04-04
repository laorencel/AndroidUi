package com.laorencel.uilibrary.http.converter;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class DefaultGsonResponseConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private TypeAdapter<T> adapter;
    private Type mType;

    DefaultGsonResponseConverter(Gson gson, TypeAdapter<T> mAdapter, Type mType) {
        this.gson = gson;
        this.adapter = mAdapter;
        this.mType = mType;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        try {
            T result = adapter.read(jsonReader);
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonIOException("JSON document was not fully consumed.");
            }
            return result;
        } catch (JsonSyntaxException e) {//当catch到这个错误说明gson解析错误
//            JsonObject res = new JsonObject();
//            res.addProperty("code",-1);
//            res.addProperty("msg","gson 解析错误");
//            return res;
            return null;
        } finally {
            value.close();
        }

//        String json = value.string();
//        try {
//            Result result = gson.fromJson(json, Result.class);
//            if (null != result && result.getCode() != HttpCode.SUCCESS) {
////                    LogUtils.eSimple(result.toString());
//                LogUtils.e(result.toString());
////                throw new CustomException(result.getCode(), "获取数据失败");
//                throw new CustomException(result.getCode(), result.getMsg());
//            }
//            return adapter.read(gson.newJsonReader(new StringReader(json)));
////            return adapter.read(gson.newJsonReader(value.charStream()));//报错 closed
//        } finally {
//            value.close();
//        }
//        return null;
    }
}
