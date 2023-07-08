package ecommerceapi.endtoend;

import static io.restassured.RestAssured.given;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

public class EcommerceApi {
	public static void main(String[] args) {

		// Login details serialize
		LoginDetailsSerialize login = new LoginDetailsSerialize();
		login.setUserEmail("hemakumar.1986@gmail.com");
		login.setUserPassword("abcdef12345");

		// Login
		// relaxedHTTPSValidation method is used to bypass any proxy or ssl
		// certification issues.
		RequestSpecification reqsepc = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).build();
		RequestSpecification reqLogin = given().relaxedHTTPSValidation().spec(reqsepc).body(login);
		LoginDetailsDeserialize loginResponse = reqLogin.when().log().all().post("/api/ecom/auth/login").then()
				.extract().response().as(LoginDetailsDeserialize.class);

		// login details Deserialize
		System.out.println("The login response session/bearer/auth token is " + loginResponse.getToken());
		System.out.println("The login response message is " + loginResponse.getMessage());
		System.out.println("The login response user id is " + loginResponse.getUserId());
		String userId = loginResponse.getUserId();
		String sessionToken = loginResponse.getToken();
		// Add Product

		RequestSpecification addProductReqSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("authorization", sessionToken).build();

		RequestSpecification addProduct = given().spec(addProductReqSpec).param("productName", "laptop")
				.param("productAddedBy", userId).param("productCategory", "tech").param("productSubCategory", "laptop")
				.param("productPrice", "11500").param("productDescription", "laptop original")
				.param("productFor", "everyone").multiPart("productImage", new File("D://laptop.png"));
		// Add product Deserialize
		AddProductDeserialize addProductResponse = addProduct.when().log().all().post("/api/ecom/product/add-product")
				.then().extract().response().as(AddProductDeserialize.class);

		System.out.println("The add product response product Id is " + addProductResponse.getProductId());
		System.out.println("The add product response message is " + addProductResponse.getMessage());
		String productId = addProductResponse.getProductId();

		// Create Order
		// Create object for the OrderDetails class and set the country and product id
		OrderDetails details = new OrderDetails();
		details.setCountry("India");
		details.setProductOrderedId(productId);

		// Create the arraylist and add the orderdetails objects into the list
		List<OrderDetails> orderList = new ArrayList<OrderDetails>();
		orderList.add(details);

		// Create object for the main orderdeserialize class and set the order list to
		// match the json format as per the requirement
		CreateOrderSerialize createOrderSerialize = new CreateOrderSerialize();
		createOrderSerialize.setOrders(orderList);

		RequestSpecification createOrderSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).addHeader("authorization", sessionToken).build();
		RequestSpecification createOrder = given().log().all().spec(createOrderSpec).body(createOrderSerialize);

		String createOrderResponse = createOrder.when().log().all().post("/api/ecom/order/create-order").then()
				.extract().response().asString();

		System.out.println("---------------Result of create order-----------");
		System.out.println(createOrderResponse);

		// Delete Order
		RequestSpecification deleteOrderSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("authorization", sessionToken).build();

		RequestSpecification deleteOrder = given().spec(deleteOrderSpec).pathParam("productId", productId);
		String deleteOrderResponse = deleteOrder.when().log().all()
				.delete("/api/ecom/product/delete-product/{productId}").then().extract().response().asString();
		System.out.println("----------Delete Order confirmation----------");
		JsonPath js = new JsonPath(deleteOrderResponse);
		System.out.println("Delete order conrimation message is: " + js.get("message"));
	}

}
