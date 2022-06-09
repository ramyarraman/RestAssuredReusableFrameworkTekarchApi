package com.test.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public class UpdateUsersRequestPOJO {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonPropertyOrder({
	"accountno",
	"departmentno",
	"salary",
	"pincode",
	"userid",
	"id"
	})
	@JsonProperty("accountno")
	private String accountno;
	@JsonProperty("departmentno")
	private String departmentno;
	@JsonProperty("salary")
	private String salary;
	@JsonProperty("pincode")
	private String pincode;
	@JsonProperty("userid")
	private String userid;
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("accountno")
	public String getAccountno() {
		return accountno;
	}
	@JsonProperty("departmentno")
	public String getDepartmentno() {
		return departmentno;
	}
	@JsonProperty("salary")
	public String getSalary() {
		return salary;
	}
	@JsonProperty("pincode")
	public String getPincode() {
		return pincode;
	}
	@JsonProperty("userid")
	public String getUserid() {
		return userid;
	}
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	
	@JsonProperty("accountno")
	public void setAccountno(String accno) {
		this.accountno= accno;
	}
	@JsonProperty("departmentno")
	public void setDepartmentno(String deptno) {
		this.departmentno = deptno;
	}
	@JsonProperty("salary")
	public void setSalary(String sal) {
		this.salary= sal;
	}
	@JsonProperty("pincode")
	public void setPincode(String pin) {
		this.pincode=pin;
	}
	@JsonProperty("userid")
	public void setUserid(String user) {
		this.userid = user;
	}
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}
	
}
