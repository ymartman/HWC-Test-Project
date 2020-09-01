package com.libertex.qa.challenge.logic;

import com.libertex.qa.challenge.api.model.ClientData;
import com.libertex.qa.challenge.api.model.LoginRequest;
import com.libertex.qa.challenge.api.model.LogoutRequest;
import com.libertex.qa.challenge.api.view.ChallengeService;
import com.libertex.qa.challenge.config.GlobalConfig;
import com.libertex.qa.challenge.api.model.response.GeneralData;
import com.libertex.qa.core.api.CallExecutor;
import io.qameta.allure.Step;
import retrofit2.Response;

import static org.testng.Assert.assertEquals;


public class ClientOperations {

    /**
     * Get X-Code for user
     */
    @Step("Get X-Code for user {username}")
    public String login(String username){
        //Get x code with login
        Response<GeneralData> response = CallExecutor
                .rawExecute(
                        new ChallengeService().getService().login(
                                new LoginRequest().setUsername(username)
                        ));

        //Assert ResultCode
        assertEquals(
                response.body().getResultCode(),
                "Ok",
                "Result code in body is invalid");

        return response.headers().get(GlobalConfig.getInstance().getChallenge().bearerName());
    }

    /**
     * Logout with user
     */
    @Step("Get X-Code for user {username}")
    public void logout(String username){
        //Get x code with login
        Response<GeneralData> response = CallExecutor
                .rawExecute(
                        new ChallengeService().getService().logout(
                                new LogoutRequest().setUsername(username)
                        ));

        //Assert ResultCode
        assertEquals(
                response.body().getResultCode(),
                "Ok",
                "Result code in body is invalid");
    }

    /**
     * Create user
     */
    @Step("Create user {username} / {fullName}")
    public void create(String username, String fullName){
        //Create client
        GeneralData response = CallExecutor
                .execute(new ChallengeService().getService().postClients(
                        new ClientData()
                                .setFullName(fullName)
                                .setUsername(username)
                ));

        //Assert ResultCode
        assertEquals(
                response.getResultCode(),
                "Ok",
                "Result code in body is invalid");
    }
}
