/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal 
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/
package com.cisco.matday.ucsd.hp3par.rest;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.exceptions.InvalidHP3ParTokenException;
import com.cisco.rwhitear.threeParREST.authenticate.json.LoginRequestJSON;
import com.cisco.rwhitear.threeParREST.authenticate.json.LoginResponseJSON;
import com.cisco.rwhitear.threeParREST.constants.threeParRESTconstants;
import com.rwhitear.ucsdHttpRequest.constants.HttpRequestConstants;

/**
 * Provides mechanisms to obtain and release tokens from a 3PAR array.
 * 
 * Generally you should not access this directly but instead use the
 * UCSD3ParHttpWrapper class to do it for you - this helps avoid having to
 * manage releasing the token when done with it
 * 
 * @author Matt Day
 *
 */
public class HP3ParToken {
	private String token;
	HP3ParCredentials loginCredentials;
	private static Logger logger = Logger.getLogger(HP3ParToken.class);

	/**
	 * Obtain a token from the array. Note, you <b>must</b> call release() when
	 * done with it.
	 * 
	 * @param loginCredentials
	 * @throws HttpException
	 * @throws IOException
	 */
	public HP3ParToken(HP3ParCredentials loginCredentials) throws HttpException, IOException {
		this.loginCredentials = loginCredentials;
		getToken(loginCredentials);
	}

	private void getToken(HP3ParCredentials credentials) throws HttpException, IOException {

		UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(credentials);
		request.addContentTypeHeader(HttpRequestConstants.CONTENT_TYPE_JSON);
		request.setUri(threeParRESTconstants.GET_SESSION_TOKEN_URI);
		request.setMethodType(HttpRequestConstants.METHOD_TYPE_POST);
		request.setBodyText(new LoginRequestJSON(credentials.getUsername(), credentials.getPassword()).convertToJSON());
		// Explicitly don't try and use a token:
		request.setToken(false);
		request.execute();
		String initToken = new LoginResponseJSON().getSessionToken(request.getHttpResponse());
		this.token = initToken;
	}

	/**
	 * Get the token
	 * 
	 * @return HP3Par authentication Token
	 * @throws InvalidHP3ParTokenException
	 *             If the token could not be obtained (e.g. credentials are
	 *             invalid)
	 */
	public String getToken() throws InvalidHP3ParTokenException {
		if (this.token == null) {
			throw new InvalidHP3ParTokenException("Token has expired or has been released");
		}
		logger.debug("Acquired token: " + this.token);
		return this.token;
	}

	/**
	 * Release the token when done. This <b>must</b> be called when a token is
	 * no longer in use.
	 * 
	 * @throws HttpException
	 * @throws IOException
	 * @throws InvalidHP3ParTokenException
	 *             If the token has already been released
	 */
	public void release() throws HttpException, IOException, InvalidHP3ParTokenException {
		if (this.token == null) {
			throw new InvalidHP3ParTokenException("Token has expired or was already released");
		}
		logger.debug("Released token: " + this.token);
		UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(this.loginCredentials);
		request.addContentTypeHeader(HttpRequestConstants.CONTENT_TYPE_JSON);
		String uri = "/api/v1/credentials/" + this.token;
		request.setUri(uri);
		request.setMethodType(HttpRequestConstants.METHOD_TYPE_DELETE);
		// Explicitly don't try and use a token:
		request.setToken(false);
		request.execute();
		/*
		 * Don't care about the output here, let the 3PAR system worry if the
		 * token was deleted or not, just null the reference on our end and let
		 * the gc pick it up
		 */
		this.token = null;
	}

	/**
	 * DO NOT rely on this! Use release() instead when done!
	 */
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (this.token != null) {
			logger.warn("Warning - token wasn't released - GC is releasing...");
			this.release();
		}
	}
}
