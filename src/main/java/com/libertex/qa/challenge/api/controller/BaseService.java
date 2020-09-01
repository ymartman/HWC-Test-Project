package com.libertex.qa.challenge.api.controller;

import com.libertex.qa.challenge.config.GlobalConfig;
import com.libertex.qa.core.api.RetrofitBuilder;
import com.libertex.qa.core.api.okhttp.HttpClientBuilder;
import lombok.Setter;

/**
 * Abstract class for realization retrofit interfaces
 */
public abstract class BaseService<T> {

    /** service url*/
    @Setter
    private String url = GlobalConfig.getInstance().getChallenge().hostName();

    private HttpClientBuilder httpClientBuilder =new HttpClientBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json");

    /** service instance*/
    private T service;

    /**
     * @return pointer to retrofit interface to initialize
     */
    protected abstract Class<T> getInterfaceClass();

    protected BaseService addHeader(String name, String value){
        httpClientBuilder.addHeader(name, value);
        return this;
    }

    public T getService(){
        service = new RetrofitBuilder()
                .setUrl(url)
                .setHttpClient(httpClientBuilder

                        .build())
                .build()
                .create(getInterfaceClass());
        return service;
    }

}
