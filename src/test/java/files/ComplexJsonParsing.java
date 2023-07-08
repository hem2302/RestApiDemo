package files;

import io.restassured.path.json.JsonPath;

public class ComplexJsonParsing {

	public static void main(String[] args) {

		// 1. Print No of courses returned by API
		// 2.Print Purchase Amount
		// 3. Print Title of the first course
		// 4. Print All course titles and their respective Prices
		// 5. Print no of copies sold by RPA Course
		// 6. Verify if Sum of all Course prices matches with Purchase Amount

		JsonPath js = new JsonPath(Payloads.coursePrice());
		// Print the number of courses returned by API
		System.out.println("---------------------------------------");
		System.out.println("Print number of courses returned by API");
		System.out.println("---------------------------------------");

		System.out.println(js.getInt("courses.size"));

		// print purchase amount
		System.out.println("-------------------------");
		System.out.println("Print the purchase amount");
		System.out.println("-------------------------");
		System.out.println(js.getInt("dashboard.purchaseAmount"));

		// print title of the first course
		System.out.println("--------------------------------");
		System.out.println("Print title of the first course");
		System.out.println("--------------------------------");
		System.out.println(js.getString("courses.title[0]"));

		// print all the course titles and their respective prices
		System.out.println("--------------------------------------------------------");
		System.out.println("Print all the course titles and their respective prices");
		System.out.println("--------------------------------------------------------");
		for (int i = 0; i < js.getInt("courses.size"); i++) {
			System.out.println(js.getString("courses.title[" + i + "]"));
			System.out.println(js.getString("courses.price[" + i + "]"));

		}

		// Print number of copies sold by RPA course
		System.out.println("-------------------------------------------");
		System.out.println("Print number of copies sold by RPA course");
		System.out.println("-------------------------------------------");

		for (int i = 0; i < js.getInt("courses.size"); i++) {
			String courseTitle = js.getString("courses.title[" + i + "]");
			if (courseTitle.equalsIgnoreCase("RPA")) {
				System.out.println(
						"The number of copies sold by RPA course is: " + js.getInt("courses.copies[" + i + "]"));
			}
		}

		// Verify if sum of all course prices match with Purchase amount
		System.out.println("-------------------------------------------------------------");
		System.out.println("Verify if sum of all course prices match with purchase amount");
		System.out.println("-------------------------------------------------------------");
		int sum = 0;
		int amount = 0;
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		for (int i = 0; i < js.getInt("courses.size"); i++) {
			int price = js.getInt("courses.price[" + i + "]");
			int copies = js.getInt("courses.copies[" + i + "]");
			amount = price * copies;
			sum = sum + amount;
		}
		if (sum == purchaseAmount) {
			System.out.println("purchase amount " + purchaseAmount + " equals " + "the total calculated price " + sum);
		} else {
			System.out.println("purchase amount not equals");
		}
	}
}
