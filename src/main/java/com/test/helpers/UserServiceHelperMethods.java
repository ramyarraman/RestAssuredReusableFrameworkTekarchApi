package com.test.helpers;


import java.util.List;
import java.util.Map;

import com.test.models.AddUsersRequestPOJO;
import com.test.models.AddUsersResponsePOJO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.models.DeleteUsersResponsePOJO;
import com.test.models.UpdateUsersRequestPOJO;
import com.test.constants.EndPoints;
import com.test.models.LoginRequestPOJO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;



public class UserServiceHelperMethods extends ReusableMethods{

	public static String tokenValue=null;
	public RequestSpecification request;
	public Response response;
	ObjectMapper objectMapper = new ObjectMapper();

	public void getTokenValue() {
		
		loginRequest();
		List<Map<String,String>> loginResponseListOfMap=null;
		try {
			
			loginResponseListOfMap = objectMapper.readValue(response.asString(), new TypeReference<List<Map<String,String>>>(){});
		
		} catch (JsonMappingException e) {		
			e.printStackTrace();
		} catch (JsonProcessingException e) {			
			e.printStackTrace();
		}
		tokenValue = loginResponseListOfMap.get(0).get("token");			
	}


	public void loginRequest(){

		LoginRequestPOJO obj = getObjectForLoginRequestBody();

		request = RestAssured.given()
				.contentType(ContentType.JSON)
				.body(obj);
		//									.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("LoginRequestSchema.json"));
		response = request.when().post(EndPoints.LOGIN);					
	}


	public AddUsersResponsePOJO addUsersRequest(String accountNo, String deptNo, String pin, String salary)  {
		if(tokenValue==null) {
			getTokenValue();
		}
		AddUsersRequestPOJO obj =getObjectForAddUserRequestBody(accountNo, deptNo, pin, salary );
		request = RestAssured.given()
				.header("token", tokenValue)
				.contentType(ContentType.JSON)
//				.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("AddUsersRequestSchema.json"))
				.body(obj)
				.log().body();

		response = request.when()	
				.post(EndPoints.ADD_USER);

		AddUsersResponsePOJO responseAsObject = null;
		try {
			responseAsObject = objectMapper.readValue(response.asString(), AddUsersResponsePOJO.class);
		} catch (JsonMappingException e) {
			
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		return responseAsObject;

	}

	public void getUsersRequest() {
		if(tokenValue==null) {
			getTokenValue();
		}
		Header header1 = new Header("token", tokenValue);
		response = RestAssured.given()
				.header(header1)
				.when()
				.get(EndPoints.GET_USERS);

//		List<GetUsersResponsePOJO> getUsersResponselist = null;
//		try {
////			GetUsersResponsePOJO[] getUsersResponseArray = response.as(GetUsersResponsePOJO[].class);
//			getUsersResponselist = objectMapper.readValue(response.asString(), new TypeReference<List<GetUsersResponsePOJO>>(){});
//		} catch (JsonMappingException e) {
//
//			e.printStackTrace();
//		} catch (JsonProcessingException e) {
//
//			e.printStackTrace();
//		}
//		return getUsersResponselist;
	}

	public void updateUsersRequest(String accountNo, String deptNo, String pin, String salary, String userId, String id ) {
		if(tokenValue==null) {
			getTokenValue();
		}
		UpdateUsersRequestPOJO obj = getObjectForUpdateUserRequestBody(accountNo,deptNo,pin,salary,userId,id );	

		request = RestAssured.given()
				.header("token", tokenValue)
				.contentType(ContentType.JSON)
				.body(obj);
		response = request.when().put(EndPoints.UPDATE_USER);

		//UpdateUsersRequestPOJO updateUserResponseObj = response.as(UpdateUsersRequestPOJO.class);
	}

	public DeleteUsersResponsePOJO deleteUsersRequest(String userId, String id) {

		if(tokenValue==null) {
			getTokenValue();
		}
		request = RestAssured.given()
				.contentType(ContentType.JSON)
				.header("token", tokenValue)
				.body(getObjectForDeleteUserRequestBody(userId, id));
		response= request.when().delete(EndPoints.DELETE_USER);
		DeleteUsersResponsePOJO deleteUserResponseObj = response.as(DeleteUsersResponsePOJO.class);
		return deleteUserResponseObj;
	}		
}
