package com.libertex.qa.core.api.okhttp;

import lombok.Setter;
import lombok.experimental.Accessors;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Класс для сборки okHttpClient'a
 */
@Accessors(chain = true)
public class HttpClientBuilder {

    /** Список header'ов */
    private HashMap<String, String> headers = new HashMap<>();
    /** Коннект таймаут */
    @Setter
    private long connectTimeout = 60;
    /** Таймаут на чтение */
    @Setter
    private long readTimeout = 60;
    /** Таймаут на запись*/
    @Setter
    private long writeTimeout = 60;
    /** Игнорировать сертефикацты при запросах*/
    @Setter
    private boolean ignoreSSLCertificate = true;

    /*-------Ignore ssl exceptions ----------------------*/
    /** Класс заглшука для проверки сертификатов*/
    private final TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }
    };

    /** Класс заглушка для работы с ssl контекстом */
    private final SSLContext sslContext = configSSLContextAndFactory();
    /** Фабрика для соединения с сокетами */
    private final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

    /** Конфигурация заглушки для имитации обработки сертефикатов*/
    private SSLContext configSSLContextAndFactory(){
        // Install the all-trusting trust manager
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sslContext;
    }
    /*-----------------------------*/

    //-------------------------------------
    //------------ Builder functions ------
    //-------------------------------------

    /** Добавить http заголовок */
    public HttpClientBuilder addHeader(String param, String value){
        headers.put(param, value);
        return this;
    }

    /** Собрать клиент*/
    public OkHttpClient build(){

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder
                .addInterceptor(
                        chain -> {
                            Request request = chain.request();
                            Request.Builder ongoing = request.newBuilder();
                            headers.forEach(ongoing::addHeader);
                            return chain.proceed(ongoing.build());
                        })
                .addInterceptor(new NetworkLogger())

                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS);
        if (ignoreSSLCertificate)
            builder
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true);

        return builder.build();
    }
}
