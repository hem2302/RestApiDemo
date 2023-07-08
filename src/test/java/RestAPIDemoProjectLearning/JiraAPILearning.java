package RestAPIDemoProjectLearning;

import static io.restassured.RestAssured.given;

import java.io.File;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class JiraAPILearning {
	public static void main(String[] args) {
//1. Login to Jira to create session using login API
//2. Add a comment to existing issue using Add comment API
//3. Add an attachment to an existing issue using add attachment API
//4. Get issue details and verify if added comment and attachment exists using get issue API

		// New topics which are covered from the above example
		// How to create session filter for authentication in Rest assured automation
		// introducing
		// Path and query parameters together in single test sending file as attachment
		// using
		// Rest Assured with multipart method parsing complex json response through
		// query parameters
		// Handling HTTP certification validation through automated code.

		SessionFilter session = new SessionFilter();
		RestAssured.baseURI = "http://localhost:8080";
		// create session id and also introduce the sessionfilter class instead parsing
		// the json with multiple code lines

		// Authentication creating session id as per JIRA contract
		given().log().all().relaxedHTTPSValidation().header("Content-Type", "application/json")
				.body("{ \r\n" + "\"username\": \"hemakumar.1986\", \r\n" + "\"password\": \"hemakumar.1986\"\r\n"
						+ "}")
				.filter(session).when().post("/rest/auth/1/session").then().log().all().extract().response().asString();

		// Add a comment to existing created bug
		String postComment = given().log().all().pathParam("key", "10004").header("Content-Type", "application/json")
				.body("{\r\n" + "    \"body\": \"My first comment.\",\r\n" + "    \"visibility\": {\r\n"
						+ "        \"type\": \"role\",\r\n" + "        \"value\": \"Administrators\"\r\n" + "    }\r\n"
						+ "}")
				.filter(session).when().post("/rest/api/2/issue/{key}/comment").then().log().all().assertThat()
				.statusCode(201).extract().response().asString();
		System.out.println(postComment);

		JsonPath js = new JsonPath(postComment);
		String IdOfAddedComment = js.getString("id");
		String addedComment = js.getString("body");

		// Add attachment to the created bug
		// multipart method for adding attachments inn rest assured and header should be
		// multipart/form-data
		given().log().all().header("X-Atlassian-Token", "no-check").filter(session).pathParam("key", "10004")
				.header("Content-Type", "multipart/form-data").multiPart("file", new File("jira.txt")).when()
				.post("rest/api/2/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);

		// Get the issue details
		String getIssueDetails = given().log().all().filter(session).pathParam("key", "10004")
				.queryParam("fields", "comment").when().get("/rest/api/2/issue/{key}").then().log().all().extract()
				.response().asString();
		System.out.println(getIssueDetails);

		JsonPath js1 = new JsonPath(getIssueDetails);
		String getCommentBody = null;
		for (int i = 0; i < js1.getInt("fields.comment.comments.size"); i++) {
			String getCommentId = js1.get("fields.comment.comments[" + i + "].id");
			if (getCommentId.equalsIgnoreCase(IdOfAddedComment)) {
				getCommentBody = js1.get("fields.comment.comments[" + i + "].body");
			}
		}
		if (addedComment.equalsIgnoreCase(getCommentBody)) {
			System.out.println("Comment matching");
			System.out.println("The added comment is: " + addedComment);
			System.out.println("The got comment is: " + getCommentBody);
		} else {
			System.out.println("Comment not matching");
		}
	}
}
