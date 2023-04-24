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

public class CreateUserPostCallWithPOJO {

    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;

    static String email;

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


    public static String randomEmail(){
        return email="Test"+System.currentTimeMillis()+"@gmail.com";
    }


    @Test
    public void createUserTestWithJSONFile() throws IOException {

       //create user object

        User user=new User("krishan",randomEmail(),"male","active");

        APIResponse apiPostResponse= requestContext.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-Type","application/json")
                        .setHeader("Authorization","Bearer 21f0f7f4493434a19640d094bc45429758caadd3776822127eead30108bd939d")
                        .setData(user));

        System.out.println(apiPostResponse.status());
        System.out.println(apiPostResponse.statusText());
        Assert.assertEquals(apiPostResponse.status(),201);
        Assert.assertEquals(apiPostResponse.statusText(),"Created");

        String responseText=apiPostResponse.text();
        System.out.println(responseText);

        //covert response text/json to POJO ---deserialization

        ObjectMapper objectMapper=new ObjectMapper();
        User actUser= objectMapper.readValue(responseText, User.class);

        System.out.println("==========actual user from the response=======");
        System.out.println(actUser);
        System.out.println(actUser.getEmail());

        Assert.assertEquals(actUser.getName(),user.getName());
        Assert.assertEquals(actUser.getEmail(),user.getEmail());
        Assert.assertNotNull(actUser.getId());
    }
}
