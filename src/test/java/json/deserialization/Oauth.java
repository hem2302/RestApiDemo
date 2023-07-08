package json.deserialization;

import static io.restassured.RestAssured.given;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;

public class Oauth {
	public static void main(String[] args) {

		// Get Authorisation code

		// WebDriver driver = new ChromeDriver();
		// driver.get(
		// "https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
		// driver.findElement(By.cssSelector("input[type*='email']")).sendKeys("hemakumar.1986@gmail.com");
		// //
		// driver.findElement(By.cssSelector("input[type*='email']")).sendKeys(Keys.ENTER);
		// //
		// driver.findElement(By.cssSelector("input[type*='password']")).sendKeys("Hema2302***");
		// //
		// driver.findElement(By.cssSelector("input[type*='password']")).sendKeys(Keys.ENTER);
		// String getAuthCodeUrl = driver.getCurrentUrl();

		String getAuthCodeUrl = "https://www.googleapis.com/oauth2/v4/token?code=4%2F0AbUR2VMmnGvcGMLk2gXNZfan2I5llXZSAVlkw0s3rdtZMjL-DOGjkDuJ4qUj7OCZ6U0neA&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&client_secret=erZOWM9g3UtwNRj340YYaK_W&redirect_uri=https://rahulshettyacademy.com/getCourse.php&grant_type=authorization_code";
				
		String tempUrl = getAuthCodeUrl.split("code=")[1];
		String authCode = tempUrl.split("&scope=")[0];
		System.out.println("The auth code is: " + authCode);
		// Get Access Token
		String getAccessToken = given().urlEncodingEnabled(false).log().all().queryParams("code", authCode)
				.queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
				.queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
				.queryParams("grant_type", "authorization_code").when().log().all()
				.post("https://www.googleapis.com/oauth2/v4/token").asString();
		JsonPath js = new JsonPath(getAccessToken);
		String accessToken = js.getString("access_token");

		// Actual Request
		GetCourse gc = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON).when()
				.get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);
		System.out.println(gc.getLinkedIn());
		System.out.println(gc.getInstructor());
		System.out.println(gc.getCourses().getApi().get(1).getCourseTitle());
		System.out.println(gc.getCourses().getWebAutomation().get(1).getCourseTitle());

		List<Api> apiCourses = gc.getCourses().getApi();
		for (int i = 0; i < apiCourses.size(); i++) {
			if (apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI WebServices testing")) {
				System.out.println("The price of API - soapUI Webserices testing is: " + apiCourses.get(i).getPrice());
			}
		}
		// Create one array and compare with the results of another arraylist
		String[] courseTitles = { "Seleniu Webdriver Java", "Cypress", "Protractor" };
		// Get the courses of WebAutomation
		List<WebAutomation> webAutomationCourses = gc.getCourses().getWebAutomation();
		// Conver the array into arraylist using Arrrays.asList
		ArrayList<String> a = new ArrayList<String>();
		for (int i = 0; i < webAutomationCourses.size(); i++) {
			System.out.println("The web automation course titles are " + webAutomationCourses.get(i).getCourseTitle());
			a.add(webAutomationCourses.get(i).getCourseTitle());
		}
		List<String> b = Arrays.asList(courseTitles);
		// Assert.assertTrue(a.equals(b));
		Assert.assertEquals(a, b);

		// System.out.println(outPut);
	}
}
