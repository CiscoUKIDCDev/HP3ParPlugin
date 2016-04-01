package com.cisco.matday.ucsd.hp3par.rest.volumes;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeInformation;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeMessage;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeStatus;
import com.cisco.rwhitear.threeParREST.constants.threeParRESTconstants;
import com.google.gson.Gson;
import com.rwhitear.ucsdHttpRequest.UCSDHttpRequest;
import com.rwhitear.ucsdHttpRequest.constants.HttpRequestConstants;

public class CreateVolumeRestCall {
	/*
	 *
	 * JSON call { "name": "paw-api-test", "cpg": "FC_r1", "sizeMiB": 1024,
	 * "comment": "Matt Testing" }
	 */
	public static HP3ParVolumeStatus create(HP3ParCredentials loginCredentials,
			HP3ParVolumeInformation volumeInformation) throws HttpException, IOException {
		UCSDHttpRequest request = new UCSDHttpRequest(loginCredentials.getArray_address(),
				loginCredentials.getProtocol(), loginCredentials.getTcp_port());

		request.addContentTypeHeader(HttpRequestConstants.CONTENT_TYPE_JSON);

		request.addRequestHeaders(threeParRESTconstants.SESSION_KEY_HEADER, loginCredentials.getToken());

		request.setUri(threeParRESTconstants.GET_VOLUMES_URI);

		request.setMethodType(HttpRequestConstants.METHOD_TYPE_POST);

		Gson gson = new Gson();
		request.setBodyText(gson.toJson(volumeInformation));
		HP3ParVolumeStatus status = new HP3ParVolumeStatus();
		request.execute();
		String response = request.getHttpResponse();
		// Don't get a response if all is good, so why did we?
		if (!response.equals("")) {
			gson = new Gson();
			HP3ParVolumeMessage message = gson.fromJson(response, HP3ParVolumeMessage.class);
			status.setError("Error code: " + message.getCode() + ": " + message.getDesc());
			status.setCreated(false);
		}
		else {
			status.setCreated(true);
		}
		// Return the same reference as passed for convenience and clarity
		return status;

	}

}
