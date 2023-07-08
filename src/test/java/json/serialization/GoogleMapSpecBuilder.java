package json.serialization;

import static io.restassured.RestAssured.given;
import java.util.ArrayList;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class GoogleMapSpecBuilder {
	public static void main(String[] args) {

		AddApiSetJson api = new AddApiSetJson();
		api.setAccuracy(50);
		api.setName("hemakumar");
		api.setPhone_number("0123456789");
		api.setAddress("29, abc, abc");
		api.setWebsite("http://google.com");
		api.setLanguage("french-IN");

		// set location
		Location location = new Location();
		location.setLat(-38.383494);
		location.setLng(33.427362);
		api.setLocation(location);
		// set types by creating an array list as it should be in arraylist
		ArrayList<String> typesList = new ArrayList<String>();
		typesList.add("shoe park");
		typesList.add("shop1");
		typesList.add("shop2");
		api.setTypes(typesList);

		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String response = given().queryParam("kay", "qaclick123").header("Content-type", "application/json").body(api)
				.when().post("/maps/api/place/add/json").then().assertThat().statusCode(200).extract().response()
				.asString();
		//JsonPath js = new JsonPath(response);
		System.out.println(response);

	}
}
