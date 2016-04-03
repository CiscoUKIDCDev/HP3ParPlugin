package com.cisco.matday.ucsd.hp3par.rest.volumes;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.InvalidHP3ParTokenException;
import com.cisco.matday.ucsd.hp3par.rest.UCSD3ParHttpWrapper;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeMessage;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeStatus;
import com.google.gson.Gson;

public class DeleteVolumeRestCall {

	public static HP3ParVolumeStatus create(HP3ParCredentials loginCredentials,
			String volumeName) throws HttpException, IOException, InvalidHP3ParTokenException {

		Gson gson = new Gson();
		HP3ParVolumeStatus status = new HP3ParVolumeStatus();

		UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(loginCredentials);
		
		String uri = "/api/v1/volumes/" + volumeName;
		request.setUri(uri);

		// Use defaults for a DELETE request
		request.setDeleteDefaults();

		request.execute();
		String response = request.getHttpResponse();

		// Shouldn't get a response if all is good... if we did it's trouble
		if (!response.equals("")) {
			HP3ParVolumeMessage message = gson.fromJson(response, HP3ParVolumeMessage.class);
			status.setError("Error code: " + message.getCode() + ": " + message.getDesc());
			status.setSuccess(false);
		} else {
			status.setSuccess(true);
		}
		// Return the same reference as passed for convenience and clarity
		return status;

	}
}
