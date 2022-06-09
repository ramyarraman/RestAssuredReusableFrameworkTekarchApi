package com.tekArchapi.tests;

import org.testng.annotations.Test;
import org.testng.annotations.Parameters;

import org.testng.Assert;

import com.test.constants.SourcePath;
import com.test.helpers.UserServiceHelperMethods;
import com.test.models.AddUsersResponsePOJO;
import com.test.models.DeleteUsersResponsePOJO;


public class EndToEndTest extends UserServiceHelperMethods{
	
	
	@Test(priority = 0, enabled = true)
	public void loginTekarchApi() {
		loginRequest();			
		validateJsonSchema(response,SourcePath.LOGIN_RESPONSE_SCHEMA_PATH);
		commonValidations(response,201,5000L);	

	}


	@Test(priority = 1,enabled = true)
	@Parameters({"accountNo","deptNo","pincode","salary"})
	public void addUsers(String accountNo,String deptNo, String pincode, String salary) {
		getUsersRequest();
		int totalAccounts = getSizeOfResponse(response);

		AddUsersResponsePOJO responsePojoObj = addUsersRequest(accountNo,deptNo,pincode,salary);				

		commonValidations(response,201,5000L);	
		Assert.assertEquals(responsePojoObj.getStatus(),"success");	

		getUsersRequest();
		int numOfAccounts = getSizeOfResponse(response);

		Assert.assertEquals(totalAccounts+1, numOfAccounts);	

		String newAccountNo = getDataInJsonPath(response, "[0].accountno");
		//				System.out.println("accountNo: "+accountNo);
		Assert.assertEquals(newAccountNo,accountNo );


	}


	@Test(priority = 3,enabled = true)
	public void getUsers() {
	
		getUsersRequest();
		validateJsonSchema(response,SourcePath.GET_USERS_RESPONSE_SCHEMA_PATH);
		commonValidations(response,200,50000L);

		int numOfAccounts = getSizeOfResponse(response);
		System.out.println("Number of records: "+numOfAccounts);				
		//				response.body().prettyPrint();

	}

	@Test (priority=2,enabled=true)
	@Parameters({"accNoToUpdate","newDeptNo","newPinCode","newSalary"})
	public void updateUsers(String accNoToUpdate,String newDeptNo,String newPinCode,String newSalary) {
		
		getUsersRequest();			
		String userId = getUserIdOfAnAccount(response,accNoToUpdate);
		String id = getIdOfAnAccount(response, accNoToUpdate);

		//			System.out.println("id , userId: "+id+" "+ userId);

		updateUsersRequest(accNoToUpdate, newDeptNo, newPinCode, newSalary,userId,id);		

		commonValidations(response,200,5000L);

		String result = getDataInJsonPath(response, "status");
		Assert.assertEquals(result,"success");

		getUsersRequest();
		String updatedDeptNo = findDataInJsonPathWithParameters(response,accNoToUpdate,"departmentno");
		Assert.assertEquals(updatedDeptNo, newDeptNo);
		String updatedPinCode = findDataInJsonPathWithParameters(response,accNoToUpdate,"pincode");
		Assert.assertEquals(updatedPinCode, newPinCode);
		String updatedSalary = findDataInJsonPathWithParameters(response,accNoToUpdate,"salary");
		Assert.assertEquals(updatedSalary, newSalary);
	}

	@Test(priority = 4,enabled=true)
	@Parameters("accNoToDelete")
	public void deleteUsers(String accountNo) {

		getUsersRequest();
		String id = getIdOfAnAccount(response, accountNo);
		String userId = getUserIdOfAnAccount(response, accountNo);
		//			System.out.println("id , userId: "+id+" "+ userId);

		DeleteUsersResponsePOJO deleteResponseObj = deleteUsersRequest(userId,id);

		commonValidations(response,200,5000L);

		Assert.assertEquals(deleteResponseObj.getStatus(),"success");

	}
}








