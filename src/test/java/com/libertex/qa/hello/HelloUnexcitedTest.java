package com.libertex.qa.hello;

import com.libertex.qa.challenge.api.model.response.GeneralData;
import com.libertex.qa.challenge.api.view.ChallengeService;
import com.libertex.qa.core.api.CallExecutor;
import io.qameta.allure.Description;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class HelloUnexcitedTest {

    /**
     * Test Case:
     * 1)Send hello request without xCode
     * 2)Verify error is 401 and result code unauthorized!
     */
    @Test
    @Description("Test hello on random uuid")
    public void helloUnexcited(){

        //hello
        GeneralData response = CallExecutor
                .errorBody(
                        new ChallengeService()
                                .getService()
                                .hello() , 401, "Unauthorized",
                        GeneralData.class);

        assertEquals(response.getResultCode(), "Unauthorized", "Unexpected ResultCode");

    }
}
