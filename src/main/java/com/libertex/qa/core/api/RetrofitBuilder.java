package com.libertex.qa.core.api;

import com.google.gson.*;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Класс для построения ретрофит клиента
 */
public class RetrofitBuilder {

    /** Эндпоинт REST сервиса*/
    private String url;
    /** HTTP клиент */
    private OkHttpClient httpClient;
    /** validate eagerly*/
    private boolean validateEagerly = true;
    /** */
    private List<CallAdapter.Factory> callAdapters = new ArrayList<>();

    private Converter.Factory convectorFactory = getDefault();

    /** Установить адресс эндпоинта*/
    public RetrofitBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    /** Задать http клиент*/
    public RetrofitBuilder setHttpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    public RetrofitBuilder setConvectorFactory(Converter.Factory convectorFactory) {
        this.convectorFactory = convectorFactory;
        return this;
    }

    /** When calling Retrofit.create(java.lang.Class<T>) on the resulting Retrofit instance,
     *  eagerly validate the configuration of all methods in the supplied interface.*/
    public RetrofitBuilder setValidateEagerly(boolean validateEagerly) {
        this.validateEagerly = validateEagerly;
        return this;
    }

    /**
     * Добавить call adapter
     */
    public RetrofitBuilder addCallAdapterFactory(CallAdapter.Factory factory){
        callAdapters.add(factory);
        return this;
    }

    /** построить retrofit клиент*/
    public Retrofit build(){


        //Собрать клиент
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(convectorFactory)
                .validateEagerly(validateEagerly)
                .client(httpClient);

        //Добавить call adapters (например RxJava3CallAdapterFactory для юзения rx java)
        for (CallAdapter.Factory callAdapter: callAdapters)
            builder.addCallAdapterFactory(callAdapter);

        return builder.build();
    }

    private Converter.Factory getDefault(){
        //Сериализаторы/Десериализаторы для json
        JsonSerializer<Date> serializerDate =
                (src, typeOfSrc, context)
                        -> src == null ? null : new JsonPrimitive(src.getTime() * 1000);

        JsonDeserializer<Date> deserializerDate =
                (json, typeOfT, context)
                        -> json == null ? null : new Date(json.getAsLong()/1000);

        //Клиент для перевода json в обьекты
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, serializerDate)
                .registerTypeAdapter(Date.class, deserializerDate)
                .setDateFormat("yyyy.MM.dd HH:mm")
                .create();

        return GsonConverterFactory.create(gson);
    }

}
