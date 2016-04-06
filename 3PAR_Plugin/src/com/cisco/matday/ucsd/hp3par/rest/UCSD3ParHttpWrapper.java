/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
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
import java.io.OutputStream;
import java.io.PrintStream;

import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.rwhitear.threeParREST.constants.threeParRESTconstants;
import com.rwhitear.ucsdHttpRequest.UCSDHttpRequest;
import com.rwhitear.ucsdHttpRequest.constants.HttpRequestConstants;

/**
 * Essentially adds a token method to Russ Whitear's UCSDHttpRequest, makes it a
 * little easier by setting defaults for various methods needing less
 * boilerplate on each REST call
 * 
 * You should not use any other method to communicate with UCSD except via this
 * library, especially if you're using tokens as this handles
 * obtaining/releasing them. The latter is especially important (see the docs)
 * 
 * @author matt
 *
 */

public class UCSD3ParHttpWrapper extends UCSDHttpRequest {
	boolean useToken = false;
	HP3ParCredentials credentials = null;
	private static Logger logger = Logger.getLogger(UCSD3ParHttpWrapper.class);

	/**
	 * Only initialise from credentials to allow token later
	 * 
	 * @param credentials
	 */
	public UCSD3ParHttpWrapper(HP3ParCredentials credentials) {
		super(credentials.getArray_address(), credentials.getProtocol(), credentials.getTcp_port());
		this.credentials = credentials;
	}

	/**
	 * Set if the token should be used on the request
	 * 
	 * @param useToken
	 */
	public void setToken(boolean useToken) {
		this.useToken = useToken;
	}

	/**
	 * Sets HP3Par defaults for a GET method
	 */
	public void setGetDefaults() {
		super.setMethodType(HttpRequestConstants.METHOD_TYPE_GET);
		super.addContentTypeHeader(HttpRequestConstants.CONTENT_TYPE_JSON);
		this.setToken(true);
	}

	/**
	 * Sets HP3Par defaults for a POST method
	 */
	public void setPostDefaults(String body) {
		super.setMethodType(HttpRequestConstants.METHOD_TYPE_POST);
		super.addContentTypeHeader(HttpRequestConstants.CONTENT_TYPE_JSON);
		super.setBodyText(body);
		this.setToken(true);
	}

	/**
	 * Set HP3Par defaults for a DELETE method
	 */
	public void setDeleteDefaults() {
		this.setMethodType(HttpRequestConstants.METHOD_TYPE_DELETE);
		super.addContentTypeHeader(HttpRequestConstants.CONTENT_TYPE_JSON);
		this.setToken(true);
	}

	/**
	 * Override parent execute to include token if set
	 * 
	 * @throws IOException
	 * @throws HttpException
	 */
	public void execute() throws HttpException, IOException {
		HP3ParToken token = null;
		if ((this.useToken) && (this.credentials != null)) {
			try {
				token = new HP3ParToken(credentials);
				logger.debug("Requested token: " + token.getToken());
				super.addRequestHeaders(threeParRESTconstants.SESSION_KEY_HEADER, token.getToken());
			}
			catch (InvalidHP3ParTokenException e) {
				logger.error("Could not add token to http request (token expired or invalid credentials)!");
				e.printStackTrace();
			}
		}
		// Disable System.out.println from library
		PrintStream originalStream = System.out;

		PrintStream emptyStream = new PrintStream(new OutputStream() {
			public void write(int b) {
				// NO-OP
			}
		});
		System.setOut(emptyStream);
		// Call parent
		super.execute();
		// Re-allow
		System.setOut(originalStream);

		// Now clean up the token:
		if ((this.useToken) && (token != null)) {
			try {
				logger.debug("Released token: " + token.getToken());
				token.release();
			}
			catch (InvalidHP3ParTokenException e) {
				logger.debug("Could not release token");
				e.printStackTrace();
			}
		}

	}

}
