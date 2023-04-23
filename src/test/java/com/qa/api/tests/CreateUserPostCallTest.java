package com.qa.api.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class CreateUserPostCallTest {

    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;

    static  String emailId;

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

    public static String getRandomEmail(){
        emailId="Test"+System.currentTimeMillis()+"@abc.com";
            return emailId;
    }

    @Test
    public void createUserTest() throws IOException {

        Map<String,Object> data=new HashMap<>();
        data.put("name","krishan");
        data.put("email",getRandomEmail());
        data.put("gender","male");
        data.put("status","active");

       APIResponse apiPostResponse= requestContext.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-Type","application/json")
                        .setHeader("Authorization","Bearer 21f0f7f4493434a19640d094bc45429758caadd3776822127eead30108bd939d")
                        .setData(data));

        System.out.println(apiPostResponse.status());
        System.out.println(apiPostResponse.statusText());
        Assert.assertEquals(apiPostResponse.status(),201);
        Assert.assertEquals(apiPostResponse.statusText(),"Created");

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
        Assert.assertTrue(apiGetResponse.text().contains(emailId));

    }

}
