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
 * little easier by setting defaults for various methods
 * 
 * You should not generally use any other method to communicate with UCSD except
 * via this library
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
	 * @param token
	 * @param credentials
	 */
	public void setToken(boolean token) {
		this.useToken = token;
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
			} catch (TokenExpiredException e) {
				logger.error("Could not add token to http request (Token Expired)!");
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
			} catch (TokenExpiredException e) {
				logger.debug("Could not release token");
				e.printStackTrace();
			}
		}

	}

}
