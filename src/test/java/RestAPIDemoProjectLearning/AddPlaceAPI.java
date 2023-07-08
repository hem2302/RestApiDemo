package RestAPIDemoProjectLearning;

import io.restassured.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import io.restassured.path.json.*;

import files.Payloads;

public class AddPlaceAPI {

	public static void main(String[] args) {

		// Validate if the Add API is working as expected
		// Add Place -> Update place with new address ->get place to validate if new
		// address is present in the response

		// Rest API automation will basically work on three principles.
		// 1. Given - Give all the input details here - key parameters, body, header
		// goes in to given section.
		// 2. When - Give the http code and submit the api - Resource and http methods
		// will always go into the When section
		// 3. Then - Validate the response for the submitted api

		RestAssured.baseURI = "https://rahulshettyacademy.com";
		// post API - Post place ID
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-type", "application/json")
				.body(Payloads.addPlace()).when().post("maps/api/place/add/json").then().assertThat().statusCode(200)
				.body("scope", equalTo("APP")).header("server", "Apache/2.4.41 (Ubuntu)").extract().asString();
		System.out.println(response);

		JsonPath js = new JsonPath(response);// This class is to parse the json
		String placeId = js.getString("place_id");
		System.out.println("The place ID is: " + placeId);

		// Put API - Update place ID
		String newAddress = "70 winter walk, India";

		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body("{\r\n" + "\"place_id\":\"" + placeId + "\",\r\n" + "\"address\":\"" + newAddress + "\",\r\n"
						+ "\"key\":\"qaclick123\"\r\n" + "}\r\n" + "")
				.when().put("maps/api/place/update/json").then().log().all().assertThat().statusCode(200)
				.body("msg", equalTo("Address successfully updated"));

		// Get API - Get the place id	

		String getPlaceResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
				.when().get("maps/api/place/get/json").then().assertThat().statusCode(200).extract().response()
				.asString();
		JsonPath js1 = new JsonPath(getPlaceResponse);
		String extractedAddress = js1.getString("address");
		Assert.assertEquals(newAddress, extractedAddress);
		System.out.println("The new Address is: "+ newAddress);
		System.out.println("The extracted Address is: "+extractedAddress);
	}
}
