package com.qa.api.tests;

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

import java.io.IOException;
import java.util.Map;


public class GetAPICall {

    public Playwright playwright;
    public APIRequest request;
    public APIRequestContext requestContext;
   public  APIResponse apiresponse;

    @BeforeTest
    public void setup() {

        playwright = Playwright.create();
        request = playwright.request();
        requestContext = request.newContext();

    }

    @Test
    public void getSpecificUserAPITest() throws IOException {
         apiresponse = requestContext.get("https://gorest.co.in/public/v2/users", RequestOptions.create()
                .setQueryParam("id", 1030493)
                .setQueryParam("status", "inactive"));
        int statusCode = apiresponse.status();

        System.out.println("Response status code : " + statusCode);

        Assert.assertEquals(statusCode, 200);

        String stateresText = apiresponse.statusText();
        System.out.println("status response text: " + stateresText);

        System.out.println("api response in plain text ");
        System.out.println(apiresponse.text());
        System.out.println("------print json Response====== ");
        ObjectMapper objMapper = new ObjectMapper();
        JsonNode jsonResponse = objMapper.readTree(apiresponse.body());
        String jsonprettyresponse = jsonResponse.toPrettyString();
        System.out.println(jsonprettyresponse);
    }


    @Test
    public void getUsersAPITest() throws IOException {

        apiresponse = requestContext.get("https://gorest.co.in/public/v2/users");
        int statusCode = apiresponse.status();

        System.out.println("Response status code : " + statusCode);

        Assert.assertEquals(statusCode, 200);

        String stateresText = apiresponse.statusText();
        System.out.println("status response text: " + stateresText);

        System.out.println("api response in plain text ");
        System.out.println(apiresponse.text());
        System.out.println("------print json Response====== ");
        ObjectMapper objMapper = new ObjectMapper();
        JsonNode jsonResponse = objMapper.readTree(apiresponse.body());
        String jsonprettyresponse = jsonResponse.toPrettyString();
        System.out.println(jsonprettyresponse);
        System.out.println("-----print api url-----");
        System.out.println(apiresponse.url());
        Assert.assertEquals(apiresponse.ok(), true);

        System.out.println("response header==========");
        Map<String, String> mapheader = apiresponse.headers();
        System.out.println(mapheader);

        Assert.assertEquals(mapheader.get("content-type"), "application/json; charset=utf-8");

    }

    @AfterTest
    public void tearDown() {
        playwright.close();
    }


}
