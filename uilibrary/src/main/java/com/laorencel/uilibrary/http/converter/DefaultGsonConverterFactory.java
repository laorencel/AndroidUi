package com.laorencel.uilibrary.http.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * gson解析数据容错处理：
 * 1、int类型为空时，返回了“”，这时会报错 java.lang.NumberFormatException: empty String
 * 2、原始字段原来为String,后期修改为其他数据类型，但是未通知客户端
 * 3、有数据时返回对象，无数据时返回字符串 “”
 */
public class DefaultGsonConverterFactory extends Converter.Factory {
    public static DefaultGsonConverterFactory create() {
        return create(new Gson());
    }

    public static DefaultGsonConverterFactory create(Gson gson) {
        if (gson == null)
            throw new NullPointerException("gson == null");
        return new DefaultGsonConverterFactory(gson);
    }

    private final Gson gson;

    private DefaultGsonConverterFactory(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new DefaultGsonResponseConverter<>(gson, adapter, type);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter adapter = this.gson.getAdapter(TypeToken.get(type));
        return new DefaultGsonRequestConverter<>(this.gson, adapter);
    }
}
