package com.laorencel.uilibrary.http;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

/**
 * Retrofit使用
 * 方法注解:
 * @GET GET请求
 * @POST POST请求
 * @PUT PUT请求
 * @DELETE DELETE请求
 * @HEAD HEAD请求
 * @OPTIONS OPTIONS请求
 * @PATCH PATCH请求
 * @HTTP 作用于方法, 用于发送一个自定义的HTTP请求
 *
 * 标记注解:
 * @FormUrlEncoded 请求体是 From 表单 @POST比起@GET多了一个@FromUrlEncoded的注解。
 *                  如果去掉@FromUrlEncoded在post请求中使用@Field和@FieldMap，那么程序会抛出Java.lang.IllegalArgumentException: @Field parameters can only be used with form encoding. 的错误异常。所以如果平时公司如果是Post请求的话，需要加上这@FromUrlEncoded注解，或者使用@Body参数注解
 * @Multipart 请求体是支持文件上传的 From 表单
 * @Streaming 响应体的数据用流的形式返回，未使用该注解，默认会把数据全部载入内存，之后通过流获取数据也是读取内存中数据，所以返回数据较大时，需要使用该注解。
 *
 * 参数注解:
 *  @Query 主要用于Get请求数据，用于拼接在拼接在Url路径后面的查询参数，一个@Query相当于拼接一个参数，多个参数中间用，隔开
 *  @QueryMap 主要的效果等同于多个@Query参数拼接，主要也用于Get请求网络数据。
 *  @Body 非表单请求体，是结合post请求的
 *  @Field 表单字段，@Field的用法类似于@Query，就不在重复列举了，主要不同的是@Field主要用于Post请求数据。
 *  @FieldMap 表单字段，@FieldMap的用法类似于@QueryMap。两者主要区别是：如果请求为post实现，那么最好传递参数时使用@Field、@FieldMap和@FormUrlEncoded。因为@Query和或QueryMap都是将参数拼接在url后面的，而@Field或@FieldMap传递的参数时放在请求体的。
 *  @Part 表单字段，与 PartMap 配合，适合文件上传情况
 *  @PartMap 表单字段，与 Part 配合，适合文件上传情况；默认接受 Map<String, RequestBody> 类型，非 RequestBody 会通过 Converter 转换
 *
 * 其他注解:
 *  @Headers 静态添加一个或者多个Header请求头
 * @Header 动态添加Header请求头
 * @HeaderMap 使用Map动态添加多个Header请求头
 * @Url    @Url是动态的Url请求数据的注解。需要注意的是使用@Url时，path对应的路径不能包含”/”，不然每个加到host Url后面的东西都会被省略掉。千万注意了
 * @Path    @Path主要用于Get请求，用于替换Url路径中的变量字符。
 */
public class RetrofitClient {
    private RetrofitClient() {
    }

    private static class Builder {
        private static final RetrofitClient INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient get() {
        return Builder.INSTANCE;
    }

    private static final Long CONNECT_TIMEOUT = 10L, READ_TIMEOUT = 10L, WRITE_TIMEOUT = 10L;

    public Retrofit createRetrofit(String baseUrl, List<Converter.Factory> factories, List<Interceptor> interceptors) {
        //        new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(ScalarsConverterFactory.create())
//                //支持RxJava
//                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
//                .client(createOkHttpClient())
//                .build();
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(baseUrl);
        if (null != factories && factories.size() > 0) {
            for (int i = 0; i < factories.size(); i++) {
                builder.addConverterFactory(factories.get(i));
            }
        }
//        //在使用retrofit 以Mutopart 进行表单数据上传时,后台收到的数据有双引号
//        builder.addConverterFactory(ScalarsConverterFactory.create());
        // rxjava适配
        builder.addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()));
        builder.client(createOkHttpClient(interceptors));
        return builder.build();
    }

    public OkHttpClient createOkHttpClient(List<Interceptor> interceptors) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (null != interceptors && interceptors.size() > 0) {
            for (int i = 0; i < interceptors.size(); i++) {
                builder.addInterceptor(interceptors.get(i));
            }
        }

//      Gson gson = new GsonBuilder()
//                .setLenient()
//                .registerTypeAdapter(long.class, LongTypeAdapter)
//                .registerTypeAdapter(Long.class, LongTypeAdapter)
//                .registerTypeAdapter(int.class, IntTypeAdapter)
//                .registerTypeAdapter(Integer.class, IntTypeAdapter)
//                .registerTypeAdapter(String.class,StringTypeAdapter)
//                .create();

        return builder
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();
//        return new OkHttpClient.Builder()
//                //添加log拦截器
//                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)) //okhttp默认的
//                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
//                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
//                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
//                .build();
    }
}
