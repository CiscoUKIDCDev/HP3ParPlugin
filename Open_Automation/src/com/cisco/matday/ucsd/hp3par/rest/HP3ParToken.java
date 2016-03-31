package com.cisco.matday.ucsd.hp3par.rest;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.httpclient.HttpException;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.rwhitear.threeParREST.authenticate.json.LoginRequestJSON;
import com.cisco.rwhitear.threeParREST.authenticate.json.LoginResponseJSON;
import com.cisco.rwhitear.threeParREST.constants.threeParRESTconstants;
import com.rwhitear.ucsdHttpRequest.UCSDHttpRequest;
import com.rwhitear.ucsdHttpRequest.constants.HttpRequestConstants;

public class HP3ParToken {
	private String token;
	private long time;
	private HP3ParCredentials loginCredentials;
	// Hard coded for now
	public final static int TOKEN_LIFE = 3600;


	public HP3ParToken(HP3ParCredentials loginCredentials) throws HttpException, IOException {
		this.loginCredentials = loginCredentials;
		getToken(loginCredentials);
	}

	private void getToken(HP3ParCredentials loginCredentials) throws HttpException, IOException {
		UCSDHttpRequest request = new UCSDHttpRequest(loginCredentials.getArray_address(),
				loginCredentials.getProtocol(), loginCredentials.getTcp_port());
		request.addContentTypeHeader(HttpRequestConstants.CONTENT_TYPE_JSON);

		request.setUri(threeParRESTconstants.GET_SESSION_TOKEN_URI);

		request.setMethodType(HttpRequestConstants.METHOD_TYPE_POST);

		request.setBodyText(
				new LoginRequestJSON(loginCredentials.getUsername(), loginCredentials.getPassword()).convertToJSON());

		// System.out.println("User JSON: " + new
		// LoginRequestJSON(this.username, this.password).convertToJSON());

		request.execute();
		// String token = request.getHttpResponse();
		String token = new LoginResponseJSON().getSessionToken(request.getHttpResponse());
		this.token = token;
		Date d = new Date();
		this.time = d.getTime();
	}

	public String getToken() {
		Date d = new Date();
		long c = d.getTime();
		// Check if token has expired
		if ((c - this.time) > TOKEN_LIFE) {
			try {
				getToken(this.loginCredentials);
			} // If this fails ignore it because we already have a token
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}


}
