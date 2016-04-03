/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package com.cisco.matday.ucsd.hp3par.rest;

import java.io.IOException;


import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.rwhitear.threeParREST.authenticate.json.LoginRequestJSON;
import com.cisco.rwhitear.threeParREST.authenticate.json.LoginResponseJSON;
import com.cisco.rwhitear.threeParREST.constants.threeParRESTconstants;
import com.rwhitear.ucsdHttpRequest.constants.HttpRequestConstants;

public class HP3ParToken {
	private String token;
	HP3ParCredentials loginCredentials;
	private static Logger logger = Logger.getLogger(HP3ParToken.class);

	public HP3ParToken(HP3ParCredentials loginCredentials) throws HttpException, IOException {
		this.loginCredentials = loginCredentials;
		getToken(loginCredentials);
	}

	private void getToken(HP3ParCredentials loginCredentials) throws HttpException, IOException {
		
		UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(loginCredentials);
		request.addContentTypeHeader(HttpRequestConstants.CONTENT_TYPE_JSON);
		request.setUri(threeParRESTconstants.GET_SESSION_TOKEN_URI);
		request.setMethodType(HttpRequestConstants.METHOD_TYPE_POST);
		request.setBodyText(
				new LoginRequestJSON(loginCredentials.getUsername(), loginCredentials.getPassword()).convertToJSON());
		request.execute();
		String token = new LoginResponseJSON().getSessionToken(request.getHttpResponse());
		this.token = token;
	}

	public String getToken() throws TokenExpiredException {
		if (this.token == null) {
			throw new TokenExpiredException("Token has expired or has been released");
		}
		logger.debug("Acquired token: " + token);
		return this.token;
	}

	public void release() throws HttpException, IOException, TokenExpiredException {
		if (token == null) {
			throw new TokenExpiredException("Token has expired or was already released");
		}
		logger.debug("Released token: " + token);
		UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(loginCredentials);
		request.addContentTypeHeader(HttpRequestConstants.CONTENT_TYPE_JSON);
		String uri = "/api/v1/credentials/" + token;
		request.setUri(uri);
		request.setMethodType(HttpRequestConstants.METHOD_TYPE_DELETE);
		request.execute();		
		this.token = null;
	}
	/**
	 * DO NOT rely on this! Use release() instead when done!
	 */
	protected void finalize() throws Throwable {
		super.finalize();
		this.release();
	}

	public void setToken(String token) {
		this.token = token;
	}

}
