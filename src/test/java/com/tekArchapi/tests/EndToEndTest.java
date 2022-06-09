package com.tekArchapi.tests;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.AssertJUnit;

import org.testng.Assert;

import com.test.constants.SourcePath;
import com.test.helpers.UserServiceHelperMethods;
import com.test.models.AddUsersResponsePOJO;
import com.test.models.DeleteUsersResponsePOJO;
import io.restassured.RestAssured;

public class EndToEndTest extends UserServiceHelperMethods{

			@BeforeClass
			public void setup() {
				RestAssured.baseURI= getBaseUri();
			}
			
			@Test(priority = 0, enabled = false)
			public void loginTekarchApi() {
				
				loginRequest();			
				validateJsonSchema(response,SourcePath.LOGIN_RESPONSE_SCHEMA_PATH);
				validateStatusCode(response,201);
				validateResponseTime(response,50000L);
				validateContentType(response);				
											
			}
			
			
			@Test(priority = 1,enabled = true)
			@Parameters({"accountNo","deptNo","pincode","salary"})
			public void addUsers(String accountNo,String deptNo, String pincode, String salary) {
				if(tokenValue==null) {
					getTokenValue();
				}
				getUsersRequest();
				int totalAccounts = getSizeOfResponse(response);
				
				AddUsersResponsePOJO responsePojoObj = addUsersRequest(accountNo,deptNo,pincode,salary);				
			
				validateStatusCode(response,201);
				validateResponseTime(response,50000L);
				validateContentType(response);			
				Assert.assertEquals(responsePojoObj.getStatus(),"success");	
				
				getUsersRequest();
				int numOfAccounts = getSizeOfResponse(response);
	
				Assert.assertEquals(totalAccounts+1, numOfAccounts);	
				
				System.out.println(response.body().jsonPath().get("[0].accountno"));
				String newAccountNo = getDataInJsonPath(response, "[0].accountno");
//				System.out.println("accountNo: "+accountNo);
				Assert.assertEquals(newAccountNo,accountNo );
			
				
			}

			
			@Test(priority = 3,enabled = true)
			public void getUsers() {
				if(tokenValue==null) {
					getTokenValue();
				}
				getUsersRequest();
				validateJsonSchema(response,SourcePath.GET_USERS_RESPONSE_SCHEMA_PATH);
				validateStatusCode(response,200);
				validateResponseTime(response,50000L);
				validateContentType(response);			
				
				int numOfAccounts = getSizeOfResponse(response);
				System.out.println("Number of records: "+numOfAccounts);				
//				response.body().prettyPrint();
						
			}
			
		@Test (priority=2,enabled=true)
		@Parameters({"accNoToUpdate","newDeptNo","newPinCode","newSalary"})
		public void updateUsers(String accNoToUpdate,String newDeptNo,String newPinCode,String newSalary) {
			if(tokenValue==null) {
				getTokenValue();
			}
			
			getUsersRequest();			
			String userId = getUserIdOfAnAccount(response,accNoToUpdate);
			String id = getIdOfAnAccount(response, accNoToUpdate);
			
//			System.out.println("id , userId: "+id+" "+ userId);
			
			updateUsersRequest(accNoToUpdate, newDeptNo, newPinCode, newSalary,userId,id);		
			
			validateStatusCode(response,200);
			validateResponseTime(response,50000L);
			validateContentType(response);		
			
			String result = getDataInJsonPath(response, "status");
			Assert.assertEquals(result,"success");
			
			getUsersRequest();
			String updatedDeptNo = findDataInJsonPathWithParameters(response,"accountno",accNoToUpdate,"departmentno");
			Assert.assertEquals(updatedDeptNo, newDeptNo);
			String updatedPinCode = findDataInJsonPathWithParameters(response,"accountno",accNoToUpdate,"pincode");
			Assert.assertEquals(updatedPinCode, newPinCode);
			String updatedSalary = findDataInJsonPathWithParameters(response,"accountno",accNoToUpdate,"salary");
			Assert.assertEquals(updatedSalary, newSalary);
		}
			
		@Test(priority = 4,enabled=true)
		@Parameters("accNoToDelete")
		public void deleteUsers(String accountNo) {
			
			if(tokenValue==null) {
				getTokenValue();
			}
			getUsersRequest();
			String id = getIdOfAnAccount(response, accountNo);
			String userId = getUserIdOfAnAccount(response, accountNo);
			System.out.println("id , userId: "+id+" "+ userId);
			
			DeleteUsersResponsePOJO deleteResponseObj = deleteUsersRequest(userId,id);
	
			validateStatusCode(response,200);
			validateResponseTime(response,5000L);
			validateContentType(response);		
			
			AssertJUnit.assertEquals(deleteResponseObj.getStatus(),"success");
			
		}
	}



	
	
	
	

