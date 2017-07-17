package com.qainfotech.Assignment4;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static com.jayway.restassured.RestAssured.*;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

public class AppTestv2 {
	
	Response response;
	String jsonString;
	int id;
	String playerid;
	
	@BeforeClass
	void initialize() {
		RestAssured.baseURI = "http://10.0.1.86/snl/";
		RestAssured.basePath = "/rest/v2/";
	}
	
	@Test(priority=1)
	void TestBoard() {
		response =  given().auth().preemptive().basic("su", "root_pass").when().get("/board.json");
		Assert.assertEquals(response.statusCode(), 200);
	}
	
	@Test(priority=2)
	void Create_new_board_and_get_id() {
		id = given().auth().preemptive().basic("su", "root_pass").when().get("/board/new.json").
				then().extract().path("response.board.id");
		System.out.println(id);
	}
	
	@SuppressWarnings("unchecked")
	@Test(priority=3)
	void add_new_player() {
		JSONObject json = new JSONObject();
	    json.put("board", id);
	    JSONObject jsonObj = new JSONObject();
	    jsonObj.put("name", "shubham");
		json.put("player", jsonObj);
		JsonPath player=given().auth().preemptive().basic("su", "root_pass").contentType("application/json").body(json.toString()).when().post("/player.json")
		.then().assertThat().statusCode(200).and().extract().jsonPath();
		playerid = player.getString("response.player.id"); 
	}
	
	@Test(priority=4)
	void move_player() {
		given().auth().preemptive().basic("su", "root_pass").when().get("/move/{board_id}.json?player_id={player_id}",id,playerid)
		.then().assertThat().statusCode(200);
	}
	
	@Test(priority=5)
	void get_board_details_with_id() {
		Response res=given().auth().preemptive().basic("su", "root_pass").
	    pathParam("id",id).get("/board/{id}.json");
		System.out.println(res.asString());	
	}
	
	@Test(priority=6)
	void delete_player() {
		given().auth().preemptive().basic("su", "root_pass").delete("player/{id}.json",playerid)
		.then().assertThat().statusCode(200);
	}

}
