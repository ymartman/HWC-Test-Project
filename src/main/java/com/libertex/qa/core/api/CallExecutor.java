package com.libertex.qa.core.api;

import com.google.gson.GsonBuilder;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.testng.Assert;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public class CallExecutor {

    @Step("Execute http request")
    public static <T> Response<T> rawExecute(Call<T> call){
        Response<T> response;
        try {
            requestInformation(call);
            response = call.execute();
            responseInformation(response);
        } catch (IOException e) {
            AssertionError error = new AssertionError("Failed to execute response");
            error.addSuppressed(e);
            throw error;
        }
        return response;
    }

    /**
     * Execute check tha code is 200 and return body
     */
    public static <T> T execute(Call<T> call){
        return execute(call, 200, "OK");
    }

    /**
     * Execute check tha code and return body
     */
    public static <T> T execute(Call<T> call, int code, String message) {
        Response<T> response = rawExecute(call);
        Assert.assertEquals(response.code(), code,  "Response code not as expected");
        Assert.assertEquals(response.message(), message, "Response code not as expected");
        return response.body();
    }

    /**
     * Вынужденная функция для обработки информации из запросов с ошибочными кодами
     * <br/>
     * <br/>
     * Ретрофит при ошибке перетаскивает сообщение об ошибке в error body, это сделано для избежания скрытия ошибок при
     * сереиализации
     * и
     * это самый серьёзный недостаток ретрофита
     */
    public static <T> T errorBody(Call<?> call, int code, String message, Class<T> tClass) {
        Response<?> response = rawExecute(call);
        Assert.assertEquals(response.code(), code, "Response code not as expected");
        Assert.assertEquals(response.message(), message, "Response code not as expected");
        try {
            return new GsonBuilder().create().fromJson(response.errorBody().source().readUtf8(), tClass);
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    @Attachment
    public static String requestInformation(Call call) {
        StringBuilder builder = new StringBuilder();
        builder
                .append(call.request().method())
                .append(" ")
                .append(call.request().url())
                .append("\n");
        if (call.request().headers().size()>0)
            builder
                .append("Headers:\n")
                .append(call.request().headers())
                .append("\n");
        if (call.request().body() != null)
            builder
                .append("Body:\n")
                .append(call.request().body())
                .append("\n");
        return builder.toString();
    }

    @Attachment
    public static String responseInformation(Response response) {
        StringBuilder builder = new StringBuilder();
        builder
                .append(response.code())
                .append("\n")
                .append(response.message())
                .append("\n")
                .append("Headers:\n")
                .append(response.headers())
                .append("\n")
                .append("Body:\n")
                .append(response.body())
                .append("\n");
        return builder.toString();
    }
}
