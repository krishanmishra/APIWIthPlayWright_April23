package com.qa.api.tests;

import com.api.data.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class CreateUserTestWithJSON {
    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;

    @BeforeTest
    public void setup() {
        playwright = Playwright.create();
        request = playwright.request();
        requestContext = request.newContext();
    }

    @AfterTest
    public void tearDown() {
        playwright.close();
    }


    @Test
    public void createUserTestWithJSONFile() throws IOException {

       //get json file
        byte[] fileBytes=null;
        File file=new File("./src/test/data/user.json");
        fileBytes=Files.readAllBytes(file.toPath());

        APIResponse apiPostResponse= requestContext.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-Type","application/json")
                        .setHeader("Authorization","Bearer 21f0f7f4493434a19640d094bc45429758caadd3776822127eead30108bd939d")
                        .setData(fileBytes));

        ObjectMapper objectMapper=new ObjectMapper();
        JsonNode postJsonResponse =objectMapper.readTree(apiPostResponse.body());
        System.out.println(postJsonResponse.toPrettyString());

        // capture id from post json response

        String userId=postJsonResponse.get("id").asText();
        System.out.println("user id: "+userId);

        //Get call:Fetch the same user by id
        System.out.println("=================Get Response ===========================");


        APIResponse apiGetResponse= requestContext.get("https://gorest.co.in/public/v2/users/"+userId,
                RequestOptions.create()
                        .setHeader("Authorization","Bearer 21f0f7f4493434a19640d094bc45429758caadd3776822127eead30108bd939d"));

        System.out.println(apiGetResponse.status());
        Assert.assertEquals(apiGetResponse.statusText(),"OK");
        Assert.assertEquals(apiGetResponse.status(),200);
        System.out.println(apiGetResponse.text());
        Assert.assertTrue(apiGetResponse.text().contains(userId));
        Assert.assertTrue(apiGetResponse.text().contains("krishan"));
        //Assert.assertTrue(apiGetResponse.text().contains(emailId));
    }


}
