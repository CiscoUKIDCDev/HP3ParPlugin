package com.cisco.matday.ucsd.hp3par.rest;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.rwhitear.threeParREST.constants.threeParRESTconstants;
import com.rwhitear.ucsdHttpRequest.UCSDHttpRequest;
import com.rwhitear.ucsdHttpRequest.constants.HttpRequestConstants;

/**
 * This class wraps most requests to/from 3PAR. Amongst other things it
 * guarantees tokens are deleted
 * 
 * @author matt
 *
 */

public class HP3ParRestWrapper {
	private UCSDHttpRequest request;
	private boolean need_token = true;
	private HP3ParCredentials credentials;

	public HP3ParRestWrapper(HP3ParCredentials credentials, String uri, String method_type, String body,
			boolean need_token) {
		init_request(credentials, uri, method_type, body, need_token);

	}

	public HP3ParRestWrapper(HP3ParCredentials credentials, String uri, String method_type, String body) {
		init_request(credentials, uri, method_type, body, true);
	}
	
	public HP3ParRestWrapper(HP3ParCredentials credentials, String uri, String method_type) {
		init_request(credentials, uri, method_type, null, true);
	}
	public HP3ParRestWrapper(HP3ParCredentials credentials, String uri, String method_type, boolean need_token) {
		init_request(credentials, uri, method_type, null, need_token);
	}
	
	
	private void init_request(HP3ParCredentials credentials, String uri, String method_type, String body,
			boolean need_token) {

		// Store state of credentials and token
		this.credentials = credentials;
		this.need_token = need_token;

		request = new UCSDHttpRequest(credentials.getArray_address(), credentials.getProtocol(),
				credentials.getTcp_port());
		request.addContentTypeHeader(HttpRequestConstants.CONTENT_TYPE_JSON);
		if (body != null) {
			request.setBodyText(body);
		}
		request.setUri(uri);
		request.setMethodType(method_type);
	}

	public String execute() throws HttpException, IOException, TokenExpiredException {
		HP3ParToken token = null;
		if (need_token == true) {
			token = new HP3ParToken(credentials);
			request.addRequestHeaders(threeParRESTconstants.SESSION_KEY_HEADER, token.getToken());
		}
		request.execute();
		// Release the token once done
		if ((need_token == true) && (token != null)) {
			token.release();
		}
		return request.getHttpResponse();
	}

}
