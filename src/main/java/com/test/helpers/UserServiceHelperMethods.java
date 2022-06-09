package com.test.helpers;


import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;

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
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;





public class UserServiceHelperMethods extends ReusableMethods{

	public static String tokenValue=null;
	public RequestSpecification request,reqSpec,requestSpec;
	public Response response;
	ObjectMapper objectMapper = new ObjectMapper();
	
	
	public void setUpRequestSpecification() {
		if(tokenValue==null) {
			getTokenValue();
		}
		reqSpec = new RequestSpecBuilder()
				.setBaseUri(getBaseUri())
				.addHeader("token", tokenValue)
				.build();
		
		requestSpec = new RequestSpecBuilder()
				.setBaseUri(getBaseUri())
				.addHeader("token", tokenValue)
				.setContentType(ContentType.JSON)
				.log(LogDetail.URI)
				.build();		
	}
	
	public void commonValidations(Response response, int expectedStatusCode, long expectedSLA ) {
		
		ResponseSpecification responseSpec = new ResponseSpecBuilder()
										.expectStatusCode(expectedStatusCode)
										.log(LogDetail.STATUS)
										.expectResponseTime(Matchers.lessThan(expectedSLA))
										.expectContentType(ContentType.JSON)
										.build();
		
		response.then().spec(responseSpec);
		
	}
	
	
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
				.baseUri(getBaseUri())
				.contentType(ContentType.JSON)
				.body(obj);
		response = request.when().post(EndPoints.LOGIN);					
	}


	public AddUsersResponsePOJO addUsersRequest(String accountNo, String deptNo, String pin, String salary)  {
		setUpRequestSpecification();
		AddUsersRequestPOJO obj =getObjectForAddUserRequestBody(accountNo, deptNo, pin, salary );
		request = RestAssured.given()
				.spec(requestSpec)
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
		setUpRequestSpecification();

		response = RestAssured.given()
				.spec(reqSpec)
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
		setUpRequestSpecification();
		UpdateUsersRequestPOJO obj = getObjectForUpdateUserRequestBody(accountNo,deptNo,pin,salary,userId,id );	

		request = RestAssured.given()
				.spec(requestSpec)
				.body(obj);
		response = request.when().put(EndPoints.UPDATE_USER);

		//UpdateUsersRequestPOJO updateUserResponseObj = response.as(UpdateUsersRequestPOJO.class);
	}

	public DeleteUsersResponsePOJO deleteUsersRequest(String userId, String id) {

		setUpRequestSpecification();
		request = RestAssured.given()
				.spec(requestSpec)
				.body(getObjectForDeleteUserRequestBody(userId, id));
		response= request.when().delete(EndPoints.DELETE_USER);
		DeleteUsersResponsePOJO deleteUserResponseObj = response.as(DeleteUsersResponsePOJO.class);
		return deleteUserResponseObj;
	}		
}
