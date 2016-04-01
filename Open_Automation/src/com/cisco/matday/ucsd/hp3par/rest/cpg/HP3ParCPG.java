package com.cisco.matday.ucsd.hp3par.rest.cpg;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.rwhitear.threeParREST.constants.threeParRESTconstants;
import com.google.gson.Gson;
import com.rwhitear.ucsdHttpRequest.UCSDHttpRequest;
import com.rwhitear.ucsdHttpRequest.constants.HttpRequestConstants;

public class HP3ParCPG {

	private CPGResponse cpgResponse;

	public HP3ParCPG(HP3ParCredentials loginCredentials) throws HttpException, IOException {
		UCSDHttpRequest request = new UCSDHttpRequest(loginCredentials.getArray_address(),
				loginCredentials.getProtocol(), loginCredentials.getTcp_port());

		request.addContentTypeHeader(HttpRequestConstants.CONTENT_TYPE_JSON);

		request.addRequestHeaders(threeParRESTconstants.SESSION_KEY_HEADER, loginCredentials.getToken());

		request.setUri(threeParRESTconstants.GET_CPG_URI);

		request.setMethodType(HttpRequestConstants.METHOD_TYPE_GET);

		request.execute();
		String response = request.getHttpResponse();
		Gson gson = new Gson();
		this.cpgResponse = gson.fromJson(response, CPGResponse.class);
	}

	public CPGResponse getCpg() {
		return cpgResponse;
	}

	public void setCpg(CPGResponse systemResponse) {
		this.cpgResponse = systemResponse;
	}
}
