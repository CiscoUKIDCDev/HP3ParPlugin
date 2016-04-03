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
package com.cisco.matday.ucsd.hp3par.rest.volumes;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.InvalidHP3ParTokenException;
import com.cisco.matday.ucsd.hp3par.rest.UCSD3ParHttpWrapper;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeInformation;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeMessage;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeStatus;
import com.cisco.rwhitear.threeParREST.constants.threeParRESTconstants;
import com.google.gson.Gson;

public class CreateVolumeRestCall {
	/*
	 *
	 * JSON call { "name": "paw-api-test", "cpg": "FC_r1", "sizeMiB": 1024,
	 * "comment": "Matt Testing" }
	 */
	public static HP3ParVolumeStatus create(HP3ParCredentials loginCredentials,
			HP3ParVolumeInformation volumeInformation) throws HttpException, IOException, InvalidHP3ParTokenException {

		Gson gson = new Gson();
		HP3ParVolumeStatus status = new HP3ParVolumeStatus();

		UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(loginCredentials);

		// Use defaults for a POST request
		request.setPostDefaults(gson.toJson(volumeInformation));
		request.setUri(threeParRESTconstants.GET_VOLUMES_URI);

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
