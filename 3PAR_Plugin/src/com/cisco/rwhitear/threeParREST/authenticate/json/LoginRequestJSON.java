package com.cisco.rwhitear.threeParREST.authenticate.json;


import com.google.gson.Gson;

// Supressing javadoc warnings as it's not my code to document
@SuppressWarnings("javadoc")
public class LoginRequestJSON {

	private String user = "";
	
	private String password = "";
	
	public LoginRequestJSON() {
		
	}
	
	public LoginRequestJSON(String user, String password) {
		this.user = user;
		this.password = password;
	}
	
	
	public String convertToJSON() {
		
		UserCredentials userCredentialsMap = new UserCredentials();
		
		userCredentialsMap.setUsername(this.user);
		userCredentialsMap.setPassword(this.password);
		
		UserCredentials jsonWrapper = new UserCredentials();
		
		jsonWrapper.setUsername( this.user );
		jsonWrapper.setPassword( this.password );
		
		Gson gson = new Gson();
		
		return gson.toJson(jsonWrapper);		
		
	}

	public String getUsername() {
		return user;
	}

	public void setUsername(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}


class UserCredentials {
	
	private String user;
	private String password;
	
	
	public String getUsername() {
		return user;
	}
	public void setUsername(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}