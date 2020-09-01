package com.libertex.qa.other;

import com.libertex.qa.challenge.api.model.ClientData;
import com.libertex.qa.challenge.api.model.response.ClientsData;
import com.libertex.qa.challenge.api.model.response.GeneralData;
import com.libertex.qa.challenge.api.view.ChallengeService;
import com.libertex.qa.core.api.CallExecutor;
import io.qameta.allure.Description;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.libertex.qa.core.reporting.Steps.info;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.*;

public class PostInvalidClientsTest {

    @DataProvider(name = "data-provider")
    public Object[][] dataProviderMethod() {
        int overMax = 256;
        return new Object[][] {
                //Выход за пределы границы поля
                { randomAlphabetic(overMax), randomAlphabetic(8)},
                { randomAlphabetic(8), randomAlphabetic(overMax)},
                //TODO пустая строка в полном имени проходит
                { "", randomAlphabetic(8)},
                { randomAlphabetic(8), ""},
                { "", ""},
                //Спец символы
                //TODO прохрдят - должны ли?
                {"cool_gay" + random(8, "@#$%^%$$^%@&"), randomAlphabetic(8)},
                {randomAlphabetic(8),"cool_gay" + random(8, "@#$%^%$$^%@&")},
                {"cool_gay" + random(8, "@#$%^%$$^%@&"), "cool_gay" + random(8, "@#$%^%$$^%@&")}
                 };
    }

    @Test(dataProvider = "data-provider")
    @Description("Check post clients not working")
    public void postInvalidClients(String fullName, String username){

        info("POST invalid new client [" + fullName + "/" + username + "]");
        GeneralData response = CallExecutor
                .errorBody(new ChallengeService().getService()
                                .postClients(
                                    new ClientData()
                                        .setFullName(fullName)
                                        .setUsername(username)
                                    ),
                        500,
                        "Internal Server Error",
                        GeneralData.class
                );
        assertEquals(response.getResultCode(), "UnexpectedError");
        assertNotNull(response.getErrorMessage(), "Error message not returned");

        //TODO can be separated in test case
        //Execute GET clients
        info("Check that GET clients not broken with invalid post");
        ClientsData clients = CallExecutor
                .execute(
                        new ChallengeService().getService().getClients());

        //Assert that exist in database
        info("Assertions");
        assertEquals(
                clients.getResultCode(),
                "Ok",
                "Result code in body is invalid");
    }
}
