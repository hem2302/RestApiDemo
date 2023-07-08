package RestAPIDemoProjectLearning;

import static io.restassured.RestAssured.given;

import io.restassured.path.json.JsonPath;

public class Oauth2 {
	public static void main(String[] args) {

		// Get Authorisation code
//		WebDriver driver = new ChromeDriver();
//		driver.get(
//				"https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
//		driver.findElement(By.cssSelector("input[type*='email']")).sendKeys("hemakumar.1986@gmail.com");
//		driver.findElement(By.cssSelector("input[type*='email']")).sendKeys(Keys.ENTER);
//		driver.findElement(By.cssSelector("input[type*='password']")).sendKeys("Hema2302***");
//		driver.findElement(By.cssSelector("input[type*='password']")).sendKeys(Keys.ENTER);

		String getAuthCodeUrl = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AbUR2VOLtUkjN5zF2nXatQDIlZa_mw2T_IgSwxQ0zjdaHbQCgFwFsk1OFn0NMjMXFJwEbw&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";
		String tempUrl = getAuthCodeUrl.split("code=")[1];
		String authCode = tempUrl.split("&scope=")[0];
		System.out.println("The auth code is: " + authCode);
		// Get Access Token
		String getAccessToken = given().urlEncodingEnabled(false).log().all().queryParams("code", authCode)
				.queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
				.queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
				.queryParams("grant_type", "authorization_code").when().log().all()
				.post("https://www.googleapis.com/oauth2/v4/token").then().log().all().extract().response().asString();
		JsonPath js = new JsonPath(getAccessToken);
		String accessToken = js.getString("access_token");

		// Actual Request
		String outPut = given().queryParam("access_token", accessToken).when().log().all()
				.get("https://rahulshettyacademy.com/getCourse.php").asString();
		System.out.println(outPut);
	}
}
