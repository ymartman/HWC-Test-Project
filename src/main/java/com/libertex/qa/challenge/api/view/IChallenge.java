package com.libertex.qa.challenge.api.view;

import com.libertex.qa.challenge.api.model.ClientData;
import com.libertex.qa.challenge.api.model.LoginRequest;
import com.libertex.qa.challenge.api.model.LogoutRequest;
import com.libertex.qa.challenge.api.model.response.ClientsData;
import com.libertex.qa.challenge.api.model.response.GeneralData;
import com.libertex.qa.challenge.api.model.response.HelloData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IChallenge {

    /**
     * @return a list of usernames from DB
     */
    @GET("/challenge/clients")
    Call<ClientsData> getClients();

    /**
     * Create new client by providing Username and Full Name. Client is stored in im-memory DB
     */
    @POST("/challenge/clients")
    Call<GeneralData> postClients(@Body ClientData clientData);

    /**
     * Returns Hello message for client. Valid 'X-Session-Id' header is required
     */
    @GET("/challenge/hello")
    Call<HelloData> hello();

    /**
     * Create Client session. Session ID is returned as header 'X-Session-Id'
     */
    @POST("/challenge/login")
    Call<GeneralData> login(@Body LoginRequest loginRequest);

    /**
     * Logout and remove Client Session
     */
    @POST("/challenge/logout")
    Call<GeneralData> logout(@Body LogoutRequest clientData);
}
