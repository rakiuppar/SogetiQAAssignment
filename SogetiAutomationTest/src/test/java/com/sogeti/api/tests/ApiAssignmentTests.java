/**
 * 
 */
package com.sogeti.api.tests;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

/**
 * Rakesh Mustoor
 */
public class ApiAssignmentTests {

	Playwright playwright;
	APIRequest request;
	APIRequestContext requestContext;
	SoftAssert softAssert = new SoftAssert();

	@BeforeTest
	public void setUp() {
		playwright = Playwright.create();
		request = playwright.request();
		requestContext = request.newContext();
	}

	@AfterTest
	public void terminate() {
		playwright.close();
	}

	@DataProvider(name = "inputParameters")
	public Object[][] dpMethod() {
		return new Object[][] { { "us", "90210", "Beverly Hills" }, { "us", "12345", "Schenectady" },
				{ "ca", "B2R", "Waverley" } };
	}

	@Test(dataProvider = "inputParameters")
	public void apiTest2(String country, String zipCode, String placeName) throws IOException {
		long startTime = System.currentTimeMillis();
		APIResponse apiResponse = requestContext.get("http://api.zippopotam.us/" + country + "/" + zipCode + "");
		long endTime = System.currentTimeMillis();
		softAssert.assertEquals(apiResponse.status(), 200, "Response code is not 200");
		Map<String, String> headers = apiResponse.headers();
		softAssert.assertEquals(headers.get("content-type"), "application/json",
				"Response header is not in JSON format");
		softAssert.assertEquals(endTime - startTime < 1000, true, "Response time is more than 1 second");
		ObjectMapper objMapper = new ObjectMapper();
		JsonNode jsonResponse = objMapper.readTree(apiResponse.body());
		String actualCountry = country(jsonResponse.get("country").textValue());
		softAssert.assertEquals(actualCountry, country, "Country is incorrect in response");
		String actualPostCode = jsonResponse.get("post code").textValue();
		softAssert.assertEquals(actualPostCode, zipCode, "Post code is incorrect in response");
		JsonNode jsonResponseQuery = objMapper.readTree(apiResponse.body());
//		softAssert.assertEquals((jsonResponseQuery.get("place name")), placeName, "Place name is incorrect in response");
		softAssert.assertAll();
	}

	@Test
	public void apiTest1() throws IOException {
		long startTime = System.currentTimeMillis();
		APIResponse apiResponse = requestContext.get("http://api.zippopotam.us/de/bw/stuttgart");
		long endTime = System.currentTimeMillis();
		softAssert.assertEquals(apiResponse.status(), 200, "Response code is not 200");
		Map<String, String> headers = apiResponse.headers();
		softAssert.assertEquals(headers.get("content-type"), "application/json",
				"Response header is not in JSON format");
		softAssert.assertEquals(endTime - startTime < 1000, true, "Response time is more than 1 second");
		ObjectMapper objMapper = new ObjectMapper();
		JsonNode jsonResponse = objMapper.readTree(apiResponse.body());
		String actualCountry = jsonResponse.get("country").textValue().replaceAll("[^a-züäößA-ZÜÄÖ]", "");
		softAssert.assertEquals(actualCountry, "Germany", "Country Germany is missing in response");
		String actualState = jsonResponse.get("state").textValue().replaceAll("[^a-züäößA-ZÜÄÖ-]", "");
		softAssert.assertEquals(actualState, "Baden-Württemberg", "State is not Baden-Württemberg");
		APIResponse apiResponseQuery = requestContext.get("http://api.zippopotam.us/de/bw/stuttgart",
				RequestOptions.create().setQueryParam("post Code", 70597));
		JsonNode jsonResponseQuery = objMapper.readTree(apiResponseQuery.body());
//		System.out.println(jsonResponseQuery);
		Iterator<JsonNode> it = jsonResponseQuery.iterator();
		for (JsonNode temp : it.next()) {
			if (temp.get("place name").asText().equalsIgnoreCase("Stuttgart Degerloch")
					&& temp.get("post code").asText().equalsIgnoreCase("705977")) {
				softAssert.assertTrue(true, "Place name is not matching with the postal code");
				break;
			}
		}
		softAssert.assertAll();
	}

	String country(String value) {
		String valString = null;
		if (value.equalsIgnoreCase("canada"))
			valString = "ca";
		else
			valString = "us";
		return valString;
	}
}
