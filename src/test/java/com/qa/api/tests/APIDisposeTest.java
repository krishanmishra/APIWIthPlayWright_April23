package com.qa.api.tests;

import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class APIDisposeTest {

    public Playwright playwright;
    public APIRequest request;
    public APIRequestContext requestContext;

    public APIResponse apiresponse;


    @BeforeTest
    public void setup() {

        playwright = Playwright.create();
        request = playwright.request();
        requestContext = request.newContext();

    }

    @Test
    public void disposeResponseTest() {

        //request #1
        apiresponse = requestContext.get("https://gorest.co.in/public/v2/users");
        int statusCode = apiresponse.status();

        System.out.println("Response status code : " + statusCode);

        Assert.assertEquals(statusCode, 200);

        String stateresText = apiresponse.statusText();
        System.out.println("status response text: " + stateresText);

        System.out.println("api response in plain text ");
        System.out.println(apiresponse.text());

        apiresponse.dispose(); //will dispose only response body but status code ,url status text remain same
        System.out.println("====print after dispose the response with plain text=====");
        try {
            System.out.println(apiresponse.text());
        } catch (PlaywrightException e) {
            System.out.println("api reps body is disposed");
        }


        int statusCode1 = apiresponse.status();
        System.out.println("Response status code : " + statusCode1);

        String stateresText1 = apiresponse.statusText();
        System.out.println("status response text: " + stateresText1);

        System.out.println("response url: " + apiresponse.url());


        //request response #2
        APIResponse apiresponse1 = requestContext.get("https://reqres.in/api/users/2");

        System.out.println("Get response body for 2nd request");
        System.out.println("status code: " + apiresponse1.status());
        System.out.println("status body: " + apiresponse1.text());


        //request context dispose
        requestContext.dispose();

        System.out.println("response 1 body dispose " + apiresponse1.text());
        System.out.println("response  body dispose "+apiresponse.text());

    }

    @AfterTest
    public void tearDown() {
        playwright.close();
    }

}
