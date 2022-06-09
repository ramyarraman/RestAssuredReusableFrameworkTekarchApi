package com.test.helpers;

import java.io.File;
import java.util.Map;

import org.hamcrest.Matchers;

import com.test.models.AddUsersRequestPOJO;
import com.test.models.DeleteUsersRequestPOJO;
import com.test.models.LoginRequestPOJO;
import com.test.models.UpdateUsersRequestPOJO;
import com.test.utils.ReadPropertiesFile;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class ReusableMethods {	
	
	public String getBaseUri() {
		return ReadPropertiesFile.getProperty("baseUri");
	}
	
	public LoginRequestPOJO getObjectForLoginRequestBody() {
		LoginRequestPOJO obj = new LoginRequestPOJO();
		obj.setUsername(ReadPropertiesFile.getProperty("username"));
		obj.setpassword(ReadPropertiesFile.getProperty("password"));
		return obj;
	}
	public AddUsersRequestPOJO getObjectForAddUserRequestBody(String accountNo, String deptNo, String pin, String salary ) {
		AddUsersRequestPOJO obj = new AddUsersRequestPOJO();
		obj.setAccountno(accountNo);
		obj.setDepartmentno(deptNo);
		obj.setPincode(pin);
		obj.setSalary(salary);
		return obj;
	}
	public UpdateUsersRequestPOJO getObjectForUpdateUserRequestBody(String accountNo, String deptNo, String pin, String salary, String userId, String id ) {
		UpdateUsersRequestPOJO obj = new UpdateUsersRequestPOJO(); 
		obj.setAccountno(accountNo);
		obj.setUserid(userId);
		obj.setId(id);
		obj.setDepartmentno(deptNo);
		obj.setSalary(salary);
		obj.setPincode(pin);
		return obj;
	}	
	
	public DeleteUsersRequestPOJO getObjectForDeleteUserRequestBody(String userId, String id) {
	DeleteUsersRequestPOJO obj = new DeleteUsersRequestPOJO();
	obj.setUserid(userId);
	obj.setId(id);
	return obj;
	}
	
	public void validateJsonSchema(Response response, String schemaFilepath) {
		response.then()
				.body(JsonSchemaValidator.matchesJsonSchema(new File(schemaFilepath)));
		
	}
	public void validateStatusCode(Response response, int expectedStatuscode) {
		response.then().assertThat().statusCode(expectedStatuscode)
				.log().status();		
	}
	
	public void validateResponseTime(Response response,long time) {
		response.then().time(Matchers.lessThan(time));
	}
	public void validateContentType(Response response) {
		response.then().contentType(ContentType.JSON);
	}
	public  String getDataInJsonPath(Response response, String path) {
		return response.jsonPath().get(path);
	}
	public  String findDataInJsonPathWithParameters(Response response,String parameter,String parameterValue, String variableToFind ) {
		Map<String, String> accountList = response.body().jsonPath().param("parameterValue",parameterValue).get("find(it->it.accountno == parameterValue)");
//		System.out.println("accountlist: "+accountList);
		String value =  accountList.get(variableToFind);
//		System.out.println("value "+value);
		return value;
		
	}
	public String getUserIdOfAnAccount(Response response, String accountNo) {
		String userId = findDataInJsonPathWithParameters(response,"accountno",accountNo,"userid");
		return userId;
	}
	public String getIdOfAnAccount(Response response, String accountNo) {
		String id = findDataInJsonPathWithParameters(response,"accountno",accountNo,"id");
		return id;
	}
	public int getSizeOfResponse(Response response) {
		return response.jsonPath().get("$.size()");
	}
	
}
