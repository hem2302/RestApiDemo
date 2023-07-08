package json.serialization;

import static io.restassured.RestAssured.given;
import java.util.ArrayList;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class GoogleMapAddApi {
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

		// Set Request specification for the common attributes
		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addQueryParam("key", "qaclick123").setContentType(ContentType.JSON).build();

		// Set Response Specification for response common attributes
		ResponseSpecification resspec = new ResponseSpecBuilder().expectStatusCode(200)
				.expectContentType(ContentType.JSON).build();

		RequestSpecification res = given().spec(req).body(api);
		Response response = res.when().post("/maps/api/place/add/json").then().spec(resspec).extract().response();

		// JsonPath js = new JsonPath(response);
		String finalResponse = response.asString();
		System.out.println(finalResponse);

	}
}
