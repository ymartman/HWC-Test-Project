package com.libertex.qa.hello;

import com.libertex.qa.challenge.api.view.ChallengeService;
import com.libertex.qa.challenge.logic.ClientOperations;
import com.libertex.qa.challenge.api.model.response.GeneralData;
import com.libertex.qa.core.api.CallExecutor;
import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class HelloUnauthorizedTest {
    private String user = RandomStringUtils.randomAlphabetic(8);
    private String fullName = RandomStringUtils.randomAlphabetic(8);

    /**
     * Test Case:
     * 1)Login with the user
     * 2)Logout with the user
     * 3)Send hello request with xCode from step 1
     * 4)Verify error is 401 and result code unauthorized!
     */
    @Test
    @Description("Test for Unauthorized error for Client after logout")
    public void helloUnauthorized(){
        //Login
        String oldXCode = new ClientOperations().login(user);

        //Logout
        new ClientOperations().logout(user);

        //hello
        GeneralData response = CallExecutor
                .errorBody(
                        new ChallengeService()
                                .setXSessionId(oldXCode)
                                .getService().hello() , 401, "Unauthorized",
                        GeneralData.class);

        assertEquals(response.getResultCode(), "Unauthorized", "Unexpected ResultCode");

    }

    @BeforeMethod
    public void preconditions(){
        new ClientOperations().create(user, fullName);
    }
}
