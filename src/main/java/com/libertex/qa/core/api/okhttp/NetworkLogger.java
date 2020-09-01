package com.libertex.qa.core.api.okhttp;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Log chains of Request/Responses for OkHttpClient
 */
public class NetworkLogger implements Interceptor {
    private final static Logger log = LogManager.getLogger(NetworkLogger.class);

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder ongoing = request.newBuilder();
        Response response =chain.proceed(ongoing.build());
        log(response, request);
        return response;
    }

    private void log(Response response, Request request){
        String delimiterLine = "\n" + StringUtils.repeat("-", 60)  + "\n";
        StringBuilder builder = new StringBuilder();
        builder.append(delimiterLine);
        builder.append("Request:")
                .append("\n\tURL: ")
                .append(request.url().toString());
        if (request.body()!= null)
            builder
                    .append("\n\tBody: \n\t")
                    .append(request.body().toString());
        builder.append(delimiterLine);
        builder.append("\nResponse:")
                .append("\n\tCode:")
                .append(response.code())
                .append("\n\tMessage:")
                .append(response.message());

        if (response.body()!= null) {
            builder
                    .append("\n\tBody:\n\t")
                    .append(responseBodyToString(response));
        }
        builder.append(delimiterLine);

        log.debug(builder.toString());
    }

    private String responseBodyToString(Response response){

        if (response.body()!= null) {
            try {
                return response.body().source().peek().readUtf8();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


}
