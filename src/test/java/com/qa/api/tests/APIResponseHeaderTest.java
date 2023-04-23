package com.qa.api.tests;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.HttpHeader;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.http.HttpHeaders;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Map;

public class APIResponseHeaderTest {

    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;

    @BeforeTest
    public void setup() {
        playwright = Playwright.create();
        request = playwright.request();
        requestContext = request.newContext();
    }

    @Test

    public void getHeaderTest() {
        //request #1
        APIResponse apiresponse = requestContext.get("https://gorest.co.in/public/v2/users");
        int statusCode = apiresponse.status();

        System.out.println("Response status code : " + statusCode);

        Assert.assertEquals(statusCode, 200);

        //Using Map
        Map<String,String> headersMap=apiresponse.headers();
        headersMap.forEach((k,v)-> System.out.println(k+" : "+v));
        System.out.println("total response headers: "+headersMap.size());
        Assert.assertEquals(headersMap.get("content-type"),"application/json; charset=utf-8");
        Assert.assertEquals(headersMap.get("server"),"cloudflare");

        //Using list:
        System.out.println("===================List of headers===========");
       List<HttpHeader> headersList= apiresponse.headersArray();  //order based collection -List
        for(HttpHeader e:headersList){
            System.out.println(e.name+" : "+e.value);
        }
    }


    @AfterTest
    public void tearDown() {
        playwright.close();
    }
}
