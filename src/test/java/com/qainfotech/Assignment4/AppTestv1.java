package com.qainfotech.Assignment4;

import static com.jayway.restassured.RestAssured.*;
import static org.testng.Assert.assertEquals;

import java.util.List;
import org.testng.*;

import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

/**
 * Unit test for simple App.
 */
public class AppTestv1  {

	Response response;
	int id;
	String playerid;
	
	@BeforeClass
	void initialize() {
		RestAssured.baseURI = "http://10.0.1.86/snl";
		RestAssured.basePath = "/rest/v1";
	}
	
	
@Test(priority=1)
void TestBoard() {
	given().when().get("/board.json").then().assertThat().statusCode(200);
}

@Test(priority=2)
void get_list_of_boards_and_size() {
	List<Integer> id=given().when().get("/board.json").then().extract().path("response.board.id");
	//System.out.println(id);
	System.out.println("Board size= "+id.size());
}

@Test(priority=3)
void Create_new_board_and_get_id() {
	id=given().when().get("/board/new.json").then().assertThat().statusCode(200).extract().path("response.board.id");	
	System.out.println("id= "+id);
}

@Test(priority=6)
void get_board_details_with_id() {
	Response res=given().
    pathParam("id",id).get("/board/{id}.json");
	System.out.println(res.asString());	
}

@SuppressWarnings("unchecked")
@Test(priority=4)
void add_new_player() {
	JSONObject json = new JSONObject();
    json.put("board", id);
    JSONObject jsonObj = new JSONObject();
    jsonObj.put("name", "shubham");
	json.put("player", jsonObj);
	JsonPath player=given().contentType("application/json").body(json.toString()).when().post("/player.json")
	.then().assertThat().statusCode(200).and().extract().jsonPath();
	playerid = player.getString("response.player.id"); 
}

@Test(priority=5)
void move_player() {
	given().get("/move/{board_id}.json?player_id={player_id}",id,playerid)
	.then().assertThat().statusCode(200);
}

@Test(priority=7)
void delete_player() {
	given().delete("player/{id}.json",playerid)
	.then().assertThat().statusCode(200);
}
  
/*@Test(priority=5)
void register_player_with_same_name() {
	JSONObject json = new JSONObject();
    json.put("board", id);
    JSONObject jsonObj = new JSONObject();
    jsonObj.put("name", "shubham");
	json.put("player", jsonObj);
	given().contentType("application/json").body(json.toString()).when().post("/player.json")
	.then().assertThat().statusCode(200);
}*/


}
